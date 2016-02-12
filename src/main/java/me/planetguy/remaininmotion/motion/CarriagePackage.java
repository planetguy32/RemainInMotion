package me.planetguy.remaininmotion.motion;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import me.planetguy.lib.util.Lang;
import me.planetguy.lib.util.Reflection;
import me.planetguy.remaininmotion.util.position.BlockPosition;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.position.BlockRecordSet;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.api.event.BlockSelectForMoveEvent;
import me.planetguy.remaininmotion.api.event.BlockSelectForRotateEvent;
import me.planetguy.remaininmotion.carriage.BlockCarriage;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.core.interop.EventPool;
import me.planetguy.remaininmotion.core.interop.ModInteraction;
import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;
import me.planetguy.remaininmotion.drive.TileEntityCarriageTranslocator;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.WorldServer;

public class CarriagePackage {
	public boolean							blacklistByRotation	= false;

	public BlockPosition RenderCacheKey;

	public TileEntityCarriageTranslocator	Translocator;

	public WorldServer world;

	public final BlockRecord driveRecord;

	public boolean							DriveIsAnchored;

	public BlockRecord						AnchorRecord;

	public Directions MotionDirection;

	public int								axis;
	
	public static volatile CarriagePackage activePackage;

	public CarriagePackage(TileEntityCarriageDrive Drive, TileEntity Anchor, Directions MotionDirection) {
		world = (WorldServer) Drive.getWorldObj();

		driveRecord = new BlockRecord(Drive.xCoord, Drive.yCoord, Drive.zCoord);

		driveRecord.Identify(world);

		DriveIsAnchored = Drive.Anchored();

		AnchorRecord = new BlockRecord(Anchor.xCoord, Anchor.yCoord, Anchor.zCoord);

		AnchorRecord.Identify(world);

		this.MotionDirection = MotionDirection;
	}

	public boolean MatchesCarriageType(BlockRecord record) {
		if(record.entity == null) {
			record.entity=new TileEntity();
		}
		return CarriageMatchers.matches(record.block, record.Meta, record.entity, this);
	}

	public BlockRecordSet Body			= new BlockRecordSet();
    public BlockRecordSet spectersToDestroy = new BlockRecordSet();

	public static int		MaxBlockCount;

	public int				MinX			= Integer.MAX_VALUE;
	public int				MinY			= Integer.MAX_VALUE;
	public int				MinZ			= Integer.MAX_VALUE;

	public int				MaxX			= Integer.MIN_VALUE;
	public int				MaxY			= Integer.MIN_VALUE;
	public int				MaxZ			= Integer.MIN_VALUE;

	public BlockRecordSet	NewPositions	= new BlockRecordSet();

	public BlockRecordSet	Carriages		= new BlockRecordSet();
	public BlockRecordSet	Cargo			= new BlockRecordSet();

	private double			Mass			= 0;

	public boolean AddBlock(BlockRecord record) throws CarriageMotionException {
		boolean couldAddBlock=AddBlock_impl(record);
		if(!couldAddBlock) {
			//this.AddPotentialObstruction(record);
            return RiMConfiguration.CarriageMotion.SupportCarriageRecurseSoftBL;
		}
		return couldAddBlock;
	}

	private boolean AddBlock_impl(BlockRecord record) throws CarriageMotionException {

		if ((MotionDirection == Directions.PosY) && (record.Y >= 254)) { throw (new CarriageObstructionException(
				"cannot move carriage above height limit", record.X, record.Y, record.Z)); }

		if ((MotionDirection == Directions.NegY) && (record.Y <= 0)) { throw (new CarriageObstructionException(
				"cannot move carriage below depth limit", record.X, record.Y, record.Z)); }

		if (BlacklistManager.lookup(BlacklistManager.blacklistHard, record)) { throw (new CarriageObstructionException(
				Lang.translate(ModRiM.Handle + ".bannedBlock"), record.X, record.Y, record.Z)); }

		if (blacklistByRotation && BlacklistManager.lookup(BlacklistManager.blacklistRotation, record)) { throw (new CarriageObstructionException(
				Lang.translate(ModRiM.Handle + ".bannedTurningBlock"), record.X, record.Y, record.Z)); }

		if (BlacklistManager.lookup(BlacklistManager.blacklistSoft, record)) { return false; }

        if(record.X == driveRecord.X && record.Y == driveRecord.Y && record.Z == driveRecord.Z)
        {
            if(record.entity instanceof TileEntityCarriageTranslocator) return false;
        }
        
		if (record.entity != null) {
			
			record.entityRecord = new NBTTagCompound();

			record.entity.writeToNBT(record.entityRecord);
			
		}

		String result=EventPool.postBlockSelectForMoveEvent(this,blacklistByRotation, record, axis);
		if(result!=null && result.equals("<skipme>")) {
			return false;
		}else if(result!=null) {
			throw new CarriageMotionException("motion killed by block at "+record+": "+result);
		}

		Body.add(record);

		if (MaxBlockCount > 0) {
			if (Body.size() > MaxBlockCount) { throw (new CarriageMotionException("carriage exceeds maximum size of "
					+ MaxBlockCount + " blocks")); }
		}

		if (MotionDirection == null) {
			BlockRecord record1 = new BlockRecord(record.X - driveRecord.X, record.Y - driveRecord.Y, record.Z
					- driveRecord.Z);
			record1.block = record.block;
			NewPositions.add(record1);
		} else {
			NewPositions.add(record.NextInDirection(MotionDirection));
		}

		MinX = Math.min(MinX, record.X);
		MinY = Math.min(MinY, record.Y);
		MinZ = Math.min(MinZ, record.Z);

		MaxX = Math.max(MaxX, record.X);
		MaxY = Math.max(MaxY, record.Y);
		MaxZ = Math.max(MaxZ, record.Z);

		if (RiMConfiguration.HardMode.HardmodeActive) {
			if (record.block == RIMBlocks.Carriage) {
				Carriages.add(record);

				setMass(getMass() + BlockCarriage.Types.values()[record.Meta].Burden);
			} else {

				Cargo.add(record);

				net.minecraft.block.Block b = record.block;

				double massFactor = Math.min(
						b.getBlockHardness(record.World, record.X, record.Y, record.Z),
						b.getExplosionResistance(null)
						);
				
				setMass(getMass() + Math.max(1, Math.log(massFactor)));

			}
		}
		
		return true;
	}

	public void FailBecauseObstructed(BlockRecord Record, String Type) throws CarriageMotionException {
		throw (new CarriageObstructionException("Carriage motion obstructed by " + Type, Record.X, Record.Y, Record.Z));
	}

	public static boolean	ObstructedByLiquids;

	public static boolean	ObstructedByFragileBlocks;

	public void AssertNotObstruction(BlockRecord record) throws CarriageMotionException {
		if (Body.contains(record)) { return; }

        // Now we only modify TileEntityCarriageDrive.targetBlockReplaceable
		int i = TileEntityCarriageDrive.isBlockReplaceable(world, record);
        switch(i)
        {
            case 0: return;
            case 1: FailBecauseObstructed(record, "fragile"); break;
            case 2: FailBecauseObstructed(record, "liquid"); break;
            case 3: FailBecauseObstructed(record, "block"); break;
        }
	}

	public BlockRecordSet	PotentialObstructions	= new BlockRecordSet();

	public void AddPotentialObstruction(BlockRecord Record) {
		PotentialObstructions.add(Record);
	}

	public net.minecraft.nbt.NBTTagList	PendingBlockUpdates	= new net.minecraft.nbt.NBTTagList();

	public void StorePendingBlockUpdateRecord(NextTickListEntry PendingBlockUpdate, long WorldTime) {
		NBTTagCompound PendingBlockUpdateRecord = new NBTTagCompound();

		PendingBlockUpdateRecord.setInteger("X", PendingBlockUpdate.xCoord);
		PendingBlockUpdateRecord.setInteger("Y", PendingBlockUpdate.yCoord);
		PendingBlockUpdateRecord.setInteger("Z", PendingBlockUpdate.zCoord);

		PendingBlockUpdateRecord.setInteger("Id", Block.getIdFromBlock(PendingBlockUpdate.func_151351_a()));

		PendingBlockUpdateRecord.setInteger("Delay", (int) (PendingBlockUpdate.scheduledTime - WorldTime));

		PendingBlockUpdateRecord.setInteger("Priority", PendingBlockUpdate.priority);

		PendingBlockUpdates.appendTag(PendingBlockUpdateRecord);
	}

	java.util.TreeMap<BlockRecord, Double>	CargoBurdenFactors	= new java.util.TreeMap<BlockRecord, Double>();

	public void ApplyCargoBurdenFactor(BlockRecord Position, double Factor) {
		Double CurrFactor = CargoBurdenFactors.get(Position);

		if (CurrFactor == null) {
			CargoBurdenFactors.put(Position, Factor);
		} else {
			CargoBurdenFactors.put(Position, CurrFactor * Factor);
		}
	}

	public void Finalize() throws CarriageMotionException {
		updateHardModeData();

		for (BlockRecord PotentialObstruction : PotentialObstructions) {
			AssertNotObstruction(PotentialObstruction);
		}

		long WorldTime = world.getWorldInfo().getWorldTotalTime();

		try {
			TreeSet<NextTickListEntry> ticks = (TreeSet<NextTickListEntry>) Reflection.get(WorldServer.class, world,
					"pendingTickListEntriesTreeSet");

			Set ticksHash = (Set) Reflection.get(WorldServer.class, world, "pendingTickListEntriesHashSet");

			Iterator<NextTickListEntry> PendingBlockUpdateSetIterator = ticks.iterator();

			while (PendingBlockUpdateSetIterator.hasNext()) {
				NextTickListEntry PendingBlockUpdate = PendingBlockUpdateSetIterator.next();

				if (Body.contains(new BlockRecord(PendingBlockUpdate.xCoord, PendingBlockUpdate.yCoord,
						PendingBlockUpdate.zCoord))) {
					PendingBlockUpdateSetIterator.remove();

					ticks.remove(PendingBlockUpdate);

					ticksHash.remove(PendingBlockUpdate);

					StorePendingBlockUpdateRecord(PendingBlockUpdate, WorldTime);
				}
			}
		} catch (Throwable VanillaThrowable) // Bad to catch throwable, but need
		// to catch NoSuchFieldError to
		// continue into MCPC+ handling
		{
			if (VanillaThrowable instanceof ThreadDeath) { throw ((ThreadDeath) VanillaThrowable); }
			try {
				java.util.Set PendingBlockUpdateSet = (java.util.Set) ModInteraction.PendingBlockUpdateSetField
						.get(world);

				while (true) {
					NextTickListEntry PendingBlockUpdate = null;

					for (Object Target : PendingBlockUpdateSet) {
						NextTickListEntry TargetPendingBlockUpdate = (NextTickListEntry) Target;

						if (Body.contains(new BlockRecord(TargetPendingBlockUpdate.xCoord,
								TargetPendingBlockUpdate.yCoord, TargetPendingBlockUpdate.zCoord))) {
							PendingBlockUpdate = TargetPendingBlockUpdate;

							break;
						}
					}

					if (PendingBlockUpdate == null) {
						break;
					}

					StorePendingBlockUpdateRecord(PendingBlockUpdate, WorldTime);

					ModInteraction.RemovePendingBlockUpdate.invoke(world, PendingBlockUpdate);
				}
			} catch (Throwable McpcThrowable) {
				McpcThrowable.printStackTrace();

				VanillaThrowable.printStackTrace();
			}
		}

		PotentialObstructions = null;

		Carriages = null;
		Cargo = null;

		CargoBurdenFactors = null;
	}

	public void updateHardModeData() throws CarriageMotionException {
		if (RiMConfiguration.HardMode.HardmodeActive) {
			// Comment out hard mode burden calculation
			/*
			 * for (BlockRecord CarriageRecord : Carriages) {
			 * 
			 * double Factor =
			 * BlockCarriage.Tiers.values()[Tier].CargoBurdenFactor;
			 * 
			 * if (Tier == 0) { for (Directions Direction : Directions.values())
			 * { BlockRecord Position =
			 * CarriageRecord.NextInDirection(Direction);
			 * 
			 * ApplyCargoBurdenFactor(Position, Factor); } } else { for (int
			 * Distance = 1; Distance <= Tier; Distance++, Factor =
			 * Math.sqrt(Factor)) { int MinX = CarriageRecord.X - Distance; int
			 * MinY = CarriageRecord.Y - Distance; int MinZ = CarriageRecord.Z -
			 * Distance;
			 * 
			 * int MaxX = CarriageRecord.X + Distance; int MaxY =
			 * CarriageRecord.Y + Distance; int MaxZ = CarriageRecord.Z +
			 * Distance;
			 * 
			 * for (int X = MinX; X <= MaxX; X++) { for (int Y = MinY; Y <=
			 * MaxY; Y++) { for (int Z = MinZ; Z <= MaxZ; Z++) { if ((X == MinX)
			 * || (X == MaxX) || (Y == MinY) || (Y == MaxY) || (Z == MinZ) || (Z
			 * == MaxZ)) { ApplyCargoBurdenFactor(new BlockRecord(X, Y, Z),
			 * Factor); } } } } } } }
			 */

			for (BlockRecord CargoRecord : Cargo) {
				Double BurdenFactor = CargoBurdenFactors.get(CargoRecord);

				double Burden = GetBaseBurden(CargoRecord);

				if (BurdenFactor == null) {
					setMass(getMass() + Burden);
				} else {
					setMass(getMass() + Burden * BurdenFactor);
				}
			}
		}

		TileEntityCarriageDrive drive = (TileEntityCarriageDrive) driveRecord.entity;

		drive.removeUsedEnergy(this);

		NBTTagCompound tag = new NBTTagCompound();
		drive.writeToNBT(tag);

		driveRecord.entityRecord = tag;
	}

	public double GetBaseBurden(BlockRecord Record) {
		return (1);
	}

	public double getMass() {
		return Mass;
	}

	public void setMass(double mass) {
		Mass = mass;
	}
}
