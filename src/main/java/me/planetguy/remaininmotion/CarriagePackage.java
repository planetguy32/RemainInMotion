package me.planetguy.remaininmotion ;

import java.util.Set;
import java.util.TreeSet;

import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.Lang;
import me.planetguy.lib.util.Reflection;
import me.planetguy.remaininmotion.api.ISpecialMoveBehavior;
import me.planetguy.remaininmotion.base.RIMBlock;
import me.planetguy.remaininmotion.carriage.Carriage;
import me.planetguy.remaininmotion.carriage.CarriageEntity;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.Mod;
import me.planetguy.remaininmotion.core.ModInteraction;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.drive.CarriageDriveEntity;
import me.planetguy.remaininmotion.drive.CarriageTranslocatorEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.WorldServer;


public class CarriagePackage
{
	public boolean blacklistByRotation=false;
	
	public BlockPosition RenderCacheKey ;

	public CarriageTranslocatorEntity Translocator ;

	public net . minecraft . world . WorldServer World ;

	public final BlockRecord DriveRecord ;

	public boolean DriveIsAnchored ;

	public BlockRecord AnchorRecord ;

	public Directions MotionDirection ;
	
	public int axis;
	
	public CarriagePackage ( CarriageDriveEntity Drive , net.minecraft.tileentity.TileEntity Anchor , Directions MotionDirection )
	{
		World = ( net . minecraft . world . WorldServer ) Drive.getWorldObj() ;

		DriveRecord = new BlockRecord ( Drive . xCoord , Drive . yCoord , Drive . zCoord ) ;

		DriveRecord . Identify ( World ) ;

		DriveIsAnchored = Drive . Anchored ( ) ;

		AnchorRecord = new BlockRecord ( Anchor . xCoord , Anchor . yCoord , Anchor . zCoord ) ;

		AnchorRecord . Identify ( World ) ;

		this . MotionDirection = MotionDirection ;
	}

	public boolean MatchesCarriageType ( BlockRecord record )
	{
		return CarriageMatchers.matches(record.block, record.Meta, record.Entity, this);
	}

	public BlockRecordSet Body = new BlockRecordSet ( ) ;

	public static int MaxBlockCount ;

	public int MinX = Integer . MAX_VALUE ;
	public int MinY = Integer . MAX_VALUE ;
	public int MinZ = Integer . MAX_VALUE ;

	public int MaxX = Integer . MIN_VALUE ;
	public int MaxY = Integer . MIN_VALUE ;
	public int MaxZ = Integer . MIN_VALUE ;

	public BlockRecordSet NewPositions = new BlockRecordSet ( ) ;

	public BlockRecordSet Carriages = new BlockRecordSet ( ) ;
	public BlockRecordSet Cargo = new BlockRecordSet ( ) ;

	private double Mass = 0 ;
	
	public BlockRecord lastRecord;

	public void AddBlock ( BlockRecord Record ) throws CarriageMotionException
	{
		
	
		if ( ( MotionDirection == Directions . PosY ) && ( Record . Y >= 254 ) )
		{
			throw ( new CarriageObstructionException ( "cannot move carriage above height limit" , Record . X , Record . Y , Record . Z ) ) ;
		}

		if ( ( MotionDirection == Directions . NegY ) && ( Record . Y <= 0 ) )
		{
			throw ( new CarriageObstructionException ( "cannot move carriage below depth limit" , Record . X , Record . Y , Record . Z ) ) ;
		}

		if ( BlacklistManager . lookup (BlacklistManager.blacklistHard, Record ))
		{
			throw ( new CarriageObstructionException ( Lang.translate(Mod.Handle+".bannedBlock") , Record . X , Record . Y , Record . Z ) ) ;
		}
		
		if ( blacklistByRotation && BlacklistManager . lookup (BlacklistManager.blacklistRotation, Record ))
		{
			throw ( new CarriageObstructionException ( Lang.translate(Mod.Handle+".bannedTurningBlock") , Record . X , Record . Y , Record . Z ) ) ;
		}
		
		if(BlacklistManager.lookup(BlacklistManager.blacklistSoft,Record)){
			return;
		}

		Body . add ( Record ) ;

		if ( MaxBlockCount > 0 )
		{
			if ( Body . size ( ) > MaxBlockCount )
			{
				throw ( new CarriageMotionException ( "carriage exceeds maximum size of " + MaxBlockCount + " blocks" ) ) ;
			}
		}

		if ( MotionDirection == null )
		{
			NewPositions . add ( new BlockRecord ( Record . X - DriveRecord . X , Record . Y - DriveRecord . Y , Record . Z - DriveRecord . Z ) ) ;
		}
		else
		{
			NewPositions . add ( Record . NextInDirection ( MotionDirection ) ) ;
		}

		MinX = Math . min ( MinX , Record . X ) ;
		MinY = Math . min ( MinY , Record . Y ) ;
		MinZ = Math . min ( MinZ , Record . Z ) ;

		MaxX = Math . max ( MaxX , Record . X ) ;
		MaxY = Math . max ( MaxY , Record . Y ) ;
		MaxZ = Math . max ( MaxZ , Record . Z ) ;

		if ( Record . Entity != null )
		{
			Record . EntityRecord = new net . minecraft . nbt . NBTTagCompound ( ) ;
			
			if(Record.Entity instanceof ISpecialMoveBehavior && !(lastRecord!=null && lastRecord.equals(Record)))
				((ISpecialMoveBehavior)Record.Entity).onAdded(this, Record.EntityRecord);
			else
				Record . Entity . writeToNBT ( Record . EntityRecord ) ;
		}

		if ( Configuration . HardmodeActive )
		{
			if ( Record .block == RIMBlocks . Carriage)
			{
				Carriages . add ( Record ) ;

				setMass(getMass() + Carriage . Types . values ( ) [ Record . Meta ] . Burden * Carriage . Tiers . values ( ) [ ( ( CarriageEntity ) Record . Entity ) . Tier ] . CarriageBurdenFactor);
			}
			else
			{

				Cargo . add ( Record ) ;

				net.minecraft.block.Block b=Record.block;
				
				//take least of block's hardness and TNT resistance
				double massFactor=Math.min(
						b.getBlockHardness(Record.World, Record.X, Record.Y, Record.Z), b.getExplosionResistance(null));
				//Debug.dbg("For "+b.getLocalizedName()+", factor="+massFactor+", lf="+Math.log(massFactor));
				//always add 0.1 to weight, sometimes more if hard block to move
				setMass(getMass() + Math.max(1,Math.log(massFactor)));
				
			}
		}
		lastRecord=Record;
	}

	public void FailBecauseObstructed ( BlockRecord Record , String Type ) throws CarriageMotionException
	{
		throw ( new CarriageObstructionException ( "carriage motion obstructed by " + Type , Record . X , Record . Y , Record . Z ) ) ;
	}

	public static boolean ObstructedByLiquids ;

	public static boolean ObstructedByFragileBlocks ;

	public void AssertNotObstruction ( BlockRecord Record ) throws CarriageMotionException
	{		
		if ( Body . contains ( Record ) )
		{
			return ;
		}

		if ( World . isAirBlock ( Record . X , Record . Y , Record . Z ) )
		{
			return ;
		}

		if (World . getBlock ( Record . X , Record . Y , Record . Z ).getMaterial() . isLiquid ( ) )
		{
			if ( ObstructedByLiquids )
			{
				FailBecauseObstructed ( Record , "liquid" ) ;
			}
			
			return ;
		}

		if ( World.getBlock(Record . X , Record . Y , Record . Z ).canBeReplacedByLeaves(World, Record . X , Record . Y , Record . Z ))
		{
			if ( ObstructedByFragileBlocks )
			{
				FailBecauseObstructed ( Record , "fragile block" ) ;
			}

			return ;
		}

		FailBecauseObstructed ( Record , "block" ) ;
	}

	public BlockRecordSet PotentialObstructions = new BlockRecordSet ( ) ;

	public void AddPotentialObstruction ( BlockRecord Record )
	{
		PotentialObstructions . add ( Record ) ;
	}

	public net . minecraft . nbt . NBTTagList PendingBlockUpdates = new net . minecraft . nbt . NBTTagList ( ) ;

	public void StorePendingBlockUpdateRecord ( net . minecraft . world . NextTickListEntry PendingBlockUpdate , long WorldTime )
	{
		net . minecraft . nbt . NBTTagCompound PendingBlockUpdateRecord = new net . minecraft . nbt . NBTTagCompound ( ) ;

		PendingBlockUpdateRecord . setInteger ( "X" , PendingBlockUpdate . xCoord ) ;
		PendingBlockUpdateRecord . setInteger ( "Y" , PendingBlockUpdate . yCoord ) ;
		PendingBlockUpdateRecord . setInteger ( "Z" , PendingBlockUpdate . zCoord ) ;

		PendingBlockUpdateRecord . setInteger ( "Id" , RIMBlock.getIdFromBlock(PendingBlockUpdate.func_151351_a() ) );

		PendingBlockUpdateRecord . setInteger ( "Delay" , ( int ) ( PendingBlockUpdate . scheduledTime - WorldTime ) ) ;

		PendingBlockUpdateRecord . setInteger ( "Priority" , PendingBlockUpdate . priority ) ;

		PendingBlockUpdates . appendTag ( PendingBlockUpdateRecord ) ;
	}

	java . util . TreeMap < BlockRecord , Double > CargoBurdenFactors = new java . util . TreeMap < BlockRecord , Double > ( ) ;

	public void ApplyCargoBurdenFactor ( BlockRecord Position , double Factor )
	{
		Double CurrFactor = CargoBurdenFactors . get ( Position ) ;

		if ( CurrFactor == null )
		{
			CargoBurdenFactors . put ( Position , Factor ) ;
		}
		else
		{
			CargoBurdenFactors . put ( Position , CurrFactor * Factor ) ;
		}
	}

	public void Finalize ( ) throws CarriageMotionException
	{
		updateHardModeData();
		
		for ( BlockRecord PotentialObstruction : PotentialObstructions )
		{
			AssertNotObstruction ( PotentialObstruction ) ;
		}

		long WorldTime = World.getWorldInfo() . getWorldTotalTime ( ) ;

		try
		{
			TreeSet<NextTickListEntry> ticks=(TreeSet<NextTickListEntry>) Reflection.get(
					WorldServer.class, World, "pendingTickListEntriesTreeSet");
			
			Set ticksHash=(Set) Reflection.get(
					WorldServer.class, World, "pendingTickListEntriesHashSet");
			
			java . util . Iterator PendingBlockUpdateSetIterator = ticks . iterator ( ) ;

			while ( PendingBlockUpdateSetIterator . hasNext ( ) )
			{
				NextTickListEntry PendingBlockUpdate = ( net . minecraft . world . NextTickListEntry ) PendingBlockUpdateSetIterator . next ( ) ;

				if ( Body . contains ( new BlockRecord ( PendingBlockUpdate . xCoord , PendingBlockUpdate . yCoord , PendingBlockUpdate . zCoord ) ) )
				{
					PendingBlockUpdateSetIterator . remove ( ) ;

					ticks . remove ( PendingBlockUpdate ) ;
					
					ticksHash.remove(PendingBlockUpdate);

					StorePendingBlockUpdateRecord ( PendingBlockUpdate , WorldTime ) ;
				}
			}
		}
		catch ( Throwable VanillaThrowable ) //Bad to catch throwable, but need to catch NoSuchFieldError to continue into MCPC+ handling
		{
			if(VanillaThrowable instanceof ThreadDeath){
				throw((ThreadDeath)VanillaThrowable);
			}
			try
			{
				java . util . Set PendingBlockUpdateSet = ( java . util . Set ) ModInteraction . PendingBlockUpdateSetField . get ( World ) ;

				while ( true )
				{
					net . minecraft . world . NextTickListEntry PendingBlockUpdate = null ;

					for ( Object Target : PendingBlockUpdateSet )
					{
						net . minecraft . world . NextTickListEntry TargetPendingBlockUpdate = ( net . minecraft . world . NextTickListEntry ) Target ;

						if ( Body . contains ( new BlockRecord ( TargetPendingBlockUpdate . xCoord , TargetPendingBlockUpdate . yCoord , TargetPendingBlockUpdate . zCoord ) ) )
						{
							PendingBlockUpdate = TargetPendingBlockUpdate ;

							break ;
						}
					}

					if ( PendingBlockUpdate == null )
					{
						break ;
					}

					StorePendingBlockUpdateRecord ( PendingBlockUpdate , WorldTime ) ;

					ModInteraction . RemovePendingBlockUpdate . invoke ( World , PendingBlockUpdate ) ;
				}
			}
			catch ( Throwable McpcThrowable )
			{
				McpcThrowable . printStackTrace ( ) ;

				VanillaThrowable . printStackTrace ( ) ;
			}
		}
		
		PotentialObstructions = null ;

		Carriages = null ;
		Cargo = null ;

		CargoBurdenFactors = null ;
	}
	
	public void updateHardModeData() throws CarriageMotionException{
		if ( Configuration . HardmodeActive )
		{
			for ( BlockRecord CarriageRecord : Carriages )
			{
				int Tier = ( ( CarriageEntity ) CarriageRecord . Entity ) . Tier ;

				double Factor = Carriage . Tiers . values ( ) [ Tier ] . CargoBurdenFactor ;

				if ( Tier == 0 )
				{
					for ( Directions Direction : Directions . values ( ) )
					{
						BlockRecord Position = CarriageRecord . NextInDirection ( Direction ) ;

						ApplyCargoBurdenFactor ( Position , Factor ) ;
					}
				}
				else
				{
					for ( int Distance = 1 ; Distance <= Tier ; Distance ++ , Factor = Math . sqrt ( Factor ) )
					{
						int MinX = CarriageRecord . X - Distance ;
						int MinY = CarriageRecord . Y - Distance ;
						int MinZ = CarriageRecord . Z - Distance ;

						int MaxX = CarriageRecord . X + Distance ;
						int MaxY = CarriageRecord . Y + Distance ;
						int MaxZ = CarriageRecord . Z + Distance ;

						for ( int X = MinX ; X <= MaxX ; X ++ )
						{
							for ( int Y = MinY ; Y <= MaxY ; Y ++ )
							{
								for ( int Z = MinZ ; Z <= MaxZ ; Z ++ )
								{
									if ( ( X == MinX ) || ( X == MaxX ) || ( Y == MinY ) || ( Y == MaxY ) || ( Z == MinZ ) || ( Z == MaxZ ) )
									{
										ApplyCargoBurdenFactor ( new BlockRecord ( X , Y , Z ) , Factor ) ;
									}
								}
							}
						}
					}
				}
			}

			for ( BlockRecord CargoRecord : Cargo )
			{
				Double BurdenFactor = CargoBurdenFactors . get ( CargoRecord ) ;

				double Burden = GetBaseBurden ( CargoRecord ) ;

				if ( BurdenFactor == null )
				{
					setMass(getMass() + Burden) ;
				}
				else
				{
					setMass(getMass() + Burden * BurdenFactor) ;
				}
			}
		}
		
		CarriageDriveEntity drive=(CarriageDriveEntity) this.DriveRecord.Entity;
		
		drive.removeUsedEnergy(this);
		
		NBTTagCompound tag=new NBTTagCompound();
		drive.writeToNBT(tag);
		
		this.DriveRecord.EntityRecord=tag;
	}

	public double GetBaseBurden ( BlockRecord Record )
	{
		return ( 1 ) ;
	}

	public double getMass() {
		return Mass;
	}

	public void setMass(double mass) {
		Mass = mass;
	}
}
