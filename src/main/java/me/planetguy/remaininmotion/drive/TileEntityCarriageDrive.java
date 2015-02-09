package me.planetguy.remaininmotion.drive;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.BlockPosition;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.CarriageMatchers;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriageObstructionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.base.TileEntityCamouflageable;
import me.planetguy.remaininmotion.base.TileEntityRiM;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration.CarriageMotion;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive.Types;
import me.planetguy.remaininmotion.network.RenderPacket;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import me.planetguy.remaininmotion.spectre.BlockSpectre;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.WorldUtil;
import me.planetguy.remaininmotion.util.transformations.ArrayRotator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import cofh.api.energy.IEnergyHandler;

//@Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore")
public abstract class TileEntityCarriageDrive extends TileEntityCamouflageable implements IEnergyHandler {
	public boolean		Continuous;

	public boolean[]	SideClosed		= new boolean[Directions.values().length];

	public boolean		Signalled;

	public int			CooldownRemaining;

	public boolean		Active;

	public int			Tier;

	public int			energyStored	= 0;

	public EntityPlayer	lastUsingPlayer;
	
	public Directions	CarriageDirection;

	private Directions	SignalDirection;

	@Override
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		TagCompound.setBoolean("Continuous", Continuous);

		for (Directions Direction : Directions.values()) {
			TagCompound.setBoolean("SideClosed" + Direction.ordinal(), SideClosed[Direction.ordinal()]);
		}

		TagCompound.setBoolean("Active", Active);

		TagCompound.setInteger("Tier", Tier);
		TagCompound.setInteger("energyStored", energyStored);

	}

	@Override
	public void WriteServerRecord(NBTTagCompound TagCompound) {
		TagCompound.setBoolean("Signalled", Signalled);

		TagCompound.setInteger("CooldownRemaining", CooldownRemaining);
	}

	@Override
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		Continuous = TagCompound.getBoolean("Continuous");

		for (Directions Direction : Directions.values()) {
			SideClosed[Direction.ordinal()] = TagCompound.getBoolean("SideClosed" + Direction.ordinal());
		}

		Active = TagCompound.getBoolean("Active");

		Tier = TagCompound.getInteger("Tier");

		energyStored = TagCompound.getInteger("energyStored");

	}

	@Override
	public void ReadServerRecord(NBTTagCompound TagCompound) {
		Signalled = TagCompound.getBoolean("Signalled");

		CooldownRemaining = TagCompound.getInteger("CooldownRemaining");
	}

	@Override
	public void EmitDrops(BlockRiM Block, int Meta) {
		EmitDrop(Block, ItemCarriageDrive.Stack(Meta, Tier));
	}

	@Override
	public void Setup(EntityPlayer Player, ItemStack Item) {
		super.Setup(Player, Item);
		lastUsingPlayer = Player;
		Tier = ItemCarriageDrive.GetTier(Item);
	}

	public void HandleToolUsage(int Side, boolean Sneaking) {
		if (Sneaking) {
			SideClosed[Side] = !SideClosed[Side];
		} else {
			Continuous = !Continuous;
		}

		Propagate();
	}

	public void ToggleActivity() {
		if (Active && Continuous) {
			CooldownRemaining = RiMConfiguration.CarriageDrive.ContinuousCooldown;
		}

		Active = !Active;

		Propagate();
	}

	public boolean	Stale	= true;

	@Override
	public void Initialize() {
		Stale = true;
	}

	public void HandleNeighbourBlockChange() {
		Stale = false;

		CarriageDirection = null;

		boolean CarriageDirectionValid = true;

		setSignalDirection(null);

		boolean SignalDirectionValid = true;

		for (Directions Direction : Directions.values()) {
			int X = xCoord + Direction.DeltaX;
			int Y = yCoord + Direction.DeltaY;
			int Z = zCoord + Direction.DeltaZ;

			if (worldObj.isAirBlock(X, Y, Z)) {
				continue;
			}

			if (isSideClosed(Direction.ordinal())) {
				continue;
			}

			net.minecraft.block.Block Id = worldObj.getBlock(X, Y, Z);
			net.minecraft.tileentity.TileEntity te = worldObj.getTileEntity(X, Y, Z);

			Moveable m = CarriageMatchers.getMover(Id, worldObj.getBlockMetadata(X, Y, Z), te);
			
			if (m != null) {
				if (CarriageDirection != null) {
					CarriageDirectionValid = false;
				} else {
					CarriageDirection = Direction;
				}
			}
			if (Id.isProvidingWeakPower(worldObj, X, Y, Z, Direction.ordinal()) > 0) {
				if (getSignalDirection() != null) {
					SignalDirectionValid = false;
				} else {
					setSignalDirection(Direction);
				}
			}
		}

		if (!CarriageDirectionValid) {
			CarriageDirection = null;
		}

		if (!SignalDirectionValid) {
			setSignalDirection(null);
		}
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) { return; }

		if (Stale) {
			HandleNeighbourBlockChange();
		}

		if (CooldownRemaining > 0) {
			CooldownRemaining--;

			MarkServerRecordDirty();

			return;
		}

		if (Active) { return; }

		if (getSignalDirection() == null) {
			if (Signalled) {
				Signalled = false;

				MarkServerRecordDirty();
			}

			return;
		}

		if (CarriageDirection == null) { return; }

		if (Signalled) {
			if (!Continuous) { return; }
		} else {
			Signalled = true;

			MarkServerRecordDirty();
		}

		try {
			InitiateMotion(PreparePackage(getSignalDirection().Opposite()));

			ModRiM.plHelper.playSound(worldObj, xCoord, yCoord, zCoord, CarriageMotion.SoundFile, 1.2f, 1f);
		} catch (CarriageMotionException Exception) {

			String Message = "Drive at (" + xCoord + "," + yCoord + "," + zCoord + ") in dimension "
					+ worldObj.provider.dimensionId + " failed to move carriage: " + Exception.getMessage();

			if (Exception instanceof CarriageObstructionException) {
				CarriageObstructionException ObstructionException = (CarriageObstructionException) Exception;

				Message += " - (" + ObstructionException.X + "," + ObstructionException.Y + ","
						+ ObstructionException.Z + ")";
			}

			if (RiMConfiguration.Debug.LogMotionExceptions) {
				Debug.dbg(Message);
			}

			if (lastUsingPlayer != null) {
				lastUsingPlayer.addChatComponentMessage(new ChatComponentText(Message));
			}
		}
	}

	public CarriagePackage PreparePackage(Directions dir) throws CarriageMotionException {
		return prepareDefaultPackage(dir);
	}
	
	public boolean targetBlockReplaceable(TileEntity translocator, BlockRecord record) {
		boolean flag = false;
		boolean flag2 = false;
		boolean flag3 = false;
		Block block = translocator.getWorldObj().getBlock(record.X + translocator.xCoord, record.Y + translocator.yCoord,
				record.Z + translocator.zCoord);
		if (block != null) {
			flag2 = !CarriagePackage.ObstructedByLiquids && (FluidRegistry.lookupFluidForBlock(block) != null);
			flag3 = !CarriagePackage.ObstructedByFragileBlocks && block.getMaterial().isReplaceable();
		}
		if (translocator.getWorldObj().isAirBlock(record.X + translocator.xCoord, record.Y + translocator.yCoord, record.Z
				+ translocator.zCoord)
				|| flag2 || flag3) flag = true;
		return flag;
	}

	public CarriagePackage prepareDefaultPackage(Directions MotionDirection) throws CarriageMotionException {

		Moveable mv = CarriageMatchers.getMover(
				worldObj.getBlock(xCoord + CarriageDirection.DeltaX, yCoord + CarriageDirection.DeltaY, zCoord
						+ CarriageDirection.DeltaZ),
				worldObj.getBlockMetadata(xCoord + CarriageDirection.DeltaX, yCoord + CarriageDirection.DeltaY, zCoord
						+ CarriageDirection.DeltaZ),
				worldObj.getTileEntity(xCoord + CarriageDirection.DeltaX, yCoord + CarriageDirection.DeltaY, zCoord
						+ CarriageDirection.DeltaZ));

		CarriagePackage _package = GeneratePackage(
				worldObj.getTileEntity(xCoord + CarriageDirection.DeltaX, yCoord + CarriageDirection.DeltaY, zCoord
						+ CarriageDirection.DeltaZ), CarriageDirection, MotionDirection);

		return (_package);
	}

	public void removeUsedEnergy(CarriagePackage _package) throws CarriageMotionException {

		if (RiMConfiguration.HardmodeActive) {
			int Type = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

			{
				/* non-configurable carriage package size - commented out
				double MaxBurden = BlockCarriageDrive.Types.values()[Type].MaxBurden
						* BlockCarriageDrive.Tiers.values()[Tier].MaxBurdenFactor;

				// System.out.println("Package mass: "+Package.Mass+", max burden "+
				// CarriageDrive . Types . values ( ) [ Type ] .
				// MaxBurden+" * "+CarriageDrive.Tiers. values ( ) [ Tier ] .
				// MaxBurdenFactor +" = "+MaxBurden);

				if (_package.getMass() > MaxBurden) { throw (new CarriageMotionException(
						"(HARDMODE) carriage too massive (by roughly " + ((int) (_package.getMass() - MaxBurden))
								+ " units) for drive to handle")); }
				 */
			}

			double EnergyRequired = _package.getMass() * BlockCarriageDrive.Types.values()[Type].EnergyConsumption
					* BlockCarriageDrive.Tiers.values()[Tier].EnergyConsumptionFactor;

			int powerConsumed = (int) Math.ceil(EnergyRequired * RiMConfiguration.PowerConsumptionFactor);

			// System.out.println("Moving carriage from "+Package.AnchorRecord.toString()+" containing "+Package.Mass+" blocks, using "+powerConsumed+" energy");

			if (powerConsumed > energyStored) {
				throw (new CarriageMotionException("(HARDMODE) not enough power to move carriage (have " + energyStored
						+ ", need " + powerConsumed));
			} else {
				energyStored -= powerConsumed;
			}

		}
	}

	public BlockPosition GeneratePositionObject() {
		return (new BlockPosition(xCoord, yCoord, zCoord, worldObj.provider.dimensionId));
	}

	public void InitiateMotion(CarriagePackage Package) {

		ToggleActivity();

		Package.RenderCacheKey = GeneratePositionObject();

		RenderPacket.Dispatch(Package);

		EstablishPlaceholders(Package);

		RefreshWorld(Package);

		EstablishSpectre(Package);

	}

	public void EstablishPlaceholders(CarriagePackage Package) {
		for (BlockRecord Record : Package.Body) {
			if (Package.NewPositions.contains(Record)) {
				SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.Spectre,
						BlockSpectre.Types.Supportive.ordinal());
			} else {
				SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.air, 0);
			}
		}
	}

	public void RefreshWorld(CarriagePackage Package) {
		for (BlockRecord Record : Package.Body) {
			SneakyWorldUtil.RefreshBlock(worldObj, Record.X, Record.Y, Record.Z, Record.block, Blocks.air);
		}
	}

	public void EstablishSpectre(CarriagePackage Package) {
		int CarriageX = Package.AnchorRecord.X + Package.MotionDirection.DeltaX;
		int CarriageY = Package.AnchorRecord.Y + Package.MotionDirection.DeltaY;
		int CarriageZ = Package.AnchorRecord.Z + Package.MotionDirection.DeltaZ;

		WorldUtil.SetBlock(worldObj, CarriageX, CarriageY, CarriageZ, RIMBlocks.Spectre,
				BlockSpectre.Types.Motive.ordinal());

		worldObj.setTileEntity(CarriageX, CarriageY, CarriageZ, new TileEntityMotiveSpectre());

		((TileEntityMotiveSpectre) worldObj.getTileEntity(CarriageX, CarriageY, CarriageZ)).Absorb(Package);
	}

	public abstract CarriagePackage GeneratePackage(TileEntity carriage, Directions CarriageDirection,
			Directions MotionDirection) throws CarriageMotionException;

	public abstract boolean Anchored();

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		int toRecieve = Math.min(RiMConfiguration.powerCapacity - energyStored, maxReceive);
		if (!simulate) {
			energyStored += toRecieve;
		}
		return toRecieve;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return RiMConfiguration.HardmodeActive;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energyStored;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return RiMConfiguration.powerCapacity;
	}

	public IIcon getIcon(int Side, int meta) {
		try {
			if (SideClosed[Side]) {
				if(this.getDecoration() != null)
					return this.getDecoration().getIcon(Side, this.DecorationMeta);
				else
					return (BlockCarriageDrive.InactiveIcon); 
			}

			Types Type = Types.values()[meta];

			if (Continuous) { return (Active ? Type.ContinuousActiveIcon : Type.ContinuousIcon); }

			return (Active ? Type.NormalActiveIcon : Type.NormalIcon);
		} catch (Throwable Throwable) {
			// Throwable . printStackTrace ( ) ;

			return (Blocks.iron_block.getIcon(0, 0));
		}
	}

	public boolean isSideClosed(int side) {
		return SideClosed[side];
	}

	public void setSideClosed(int side, boolean closed) {
		SideClosed[side] = closed;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}

	@Override
	public void rotateSpecial(ForgeDirection axis) {
		ArrayRotator.rotate(SideClosed, axis);
		Propagate();
	}

	public Directions getSignalDirection() {
		return SignalDirection;
	}

	public void setSignalDirection(Directions signalDirection) {
		SignalDirection = signalDirection;
	}

	public boolean onRightClicked(int side, EntityPlayer player) {
		return false;
	}

}
