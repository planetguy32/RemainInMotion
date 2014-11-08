package me.planetguy.remaininmotion.drive ;

import me.planetguy.endgamerf.EndgameRFMod;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.BlockPosition;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.CarriageMatchers;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriageObstructionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.MotiveSpectreEntity;
import me.planetguy.remaininmotion.Spectre;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.base.RIMBlock;
import me.planetguy.remaininmotion.base.TileEntity;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.Mod;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.network.RenderPacket;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;

//@Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore")
public abstract class CarriageDriveEntity extends TileEntity implements IEnergyHandler
{
	public boolean Continuous ;

	public boolean [ ] SideClosed = new boolean [ Directions . values ( ) . length ] ;

	public boolean Signalled ;

	public int CooldownRemaining ;

	public boolean Active ;

	public int Tier ;

	public int energyStored=0;

	public EntityPlayer lastUsingPlayer;

	@Override
	public void WriteCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		Debug.mark();
		TagCompound . setBoolean ( "Continuous" , Continuous ) ;

		for ( Directions Direction : Directions . values ( ) )
		{
			TagCompound . setBoolean ( "SideClosed" + Direction . ordinal ( ) , SideClosed [ Direction . ordinal ( ) ] ) ;
		}

		TagCompound . setBoolean ( "Active" , Active ) ;

		TagCompound . setInteger ( "Tier" , Tier ) ;
		TagCompound . setInteger ( "energyStored" , energyStored ) ;

	}

	@Override
	public void WriteServerRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		TagCompound . setBoolean ( "Signalled" , Signalled ) ;

		TagCompound . setInteger ( "CooldownRemaining" , CooldownRemaining ) ;
	}

	@Override
	public void ReadCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		Continuous = TagCompound . getBoolean ( "Continuous" ) ;

		for ( Directions Direction : Directions . values ( ) )
		{
			SideClosed [ Direction . ordinal ( ) ] = TagCompound . getBoolean ( "SideClosed" + Direction . ordinal ( ) ) ;
		}

		Active = TagCompound . getBoolean ( "Active" ) ;

		Tier = TagCompound . getInteger ( "Tier" ) ;

		energyStored = TagCompound . getInteger ( "energyStored" ) ;

	}

	@Override
	public void ReadServerRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		Signalled = TagCompound . getBoolean ( "Signalled" ) ;

		CooldownRemaining = TagCompound . getInteger ( "CooldownRemaining" ) ;
	}

	@Override
	public void EmitDrops ( RIMBlock Block , int Meta )
	{
		EmitDrop ( Block , CarriageDriveItem . Stack ( Meta , Tier ) ) ;
	}

	@Override
	public void Setup ( net . minecraft . entity . player . EntityPlayer Player , net . minecraft . item . ItemStack Item )
	{
		lastUsingPlayer=Player;
		Tier = CarriageDriveItem . GetTier ( Item ) ;
	}

	public void HandleToolUsage ( int Side , boolean Sneaking )
	{
		if ( Sneaking )
		{
			SideClosed [ Side ] = ! SideClosed [ Side ] ;
		}
		else
		{
			Continuous = ! Continuous ;
		}

		Propagate ( ) ;
	}

	public void ToggleActivity ( )
	{
		if ( Active && Continuous )
		{
			CooldownRemaining = Configuration . CarriageDrive . ContinuousCooldown ;
		}

		Active = ! Active ;

		Propagate ( ) ;
	}

	public boolean Stale = true ;

	@Override
	public void Initialize ( )
	{
		Stale = true ;
	}

	public Directions CarriageDirection ;

	public Directions SignalDirection ;

	public void HandleNeighbourBlockChange ( )
	{
		Stale = false ;

		CarriageDirection = null ;

		boolean CarriageDirectionValid = true ;

		SignalDirection = null ;

		boolean SignalDirectionValid = true ;

		for ( Directions Direction : Directions . values ( ) )
		{
			int X = xCoord + Direction . DeltaX ;
			int Y = yCoord + Direction . DeltaY ;
			int Z = zCoord + Direction . DeltaZ ;

			if ( worldObj . isAirBlock ( X , Y , Z ) )
			{
				continue ;
			}
			
			if(SideClosed[Direction.ordinal()]){
				continue;
			}

			net.minecraft.block.Block Id = worldObj . getBlock ( X , Y , Z ) ;
			net.minecraft.tileentity.TileEntity te=worldObj.getTileEntity(X,Y,Z);

			Moveable m=CarriageMatchers.getMover(Id, worldObj.getBlockMetadata(X, Y, Z), te);
			if(m!=null)
				if ( CarriageDirection != null )
				{
					CarriageDirectionValid = false ;
				}
				else
				{
					CarriageDirection = Direction ;
				}
			if ( Id . isProvidingWeakPower ( worldObj , X , Y , Z , Direction . ordinal ( ) ) > 0 )
			{
				if ( SignalDirection != null )
				{
					SignalDirectionValid = false ;
				}
				else
				{
					SignalDirection = Direction ;
				}
			}
		}

		if ( ! CarriageDirectionValid )
		{
			CarriageDirection = null ;
		}

		if ( ! SignalDirectionValid )
		{
			SignalDirection = null ;
		}
	}

	@Override
	public void updateEntity ( )
	{
		if ( worldObj . isRemote )
		{
			return ;
		}

		if ( Stale )
		{
			HandleNeighbourBlockChange ( ) ;
		}

		if ( CooldownRemaining > 0 )
		{
			CooldownRemaining -- ;

			MarkServerRecordDirty ( ) ;

			return ;
		}

		if ( Active )
		{
			return ;
		}

		if ( SignalDirection == null )
		{
			if ( Signalled )
			{
				Signalled = false ;

				MarkServerRecordDirty ( ) ;
			}

			return ;
		}

		if ( CarriageDirection == null )
		{
			return ;
		}

		if ( Signalled )
		{
			if ( ! Continuous )
			{
				return ;
			}
		}
		else
		{
			Signalled = true ;

			MarkServerRecordDirty ( ) ;
		}

		try
		{
			InitiateMotion ( PreparePackage ( SignalDirection . Opposite ( ) ) ) ;
		}
		catch ( CarriageMotionException Exception )
		{


			String Message = "Drive at (" + xCoord + "," + yCoord + "," + zCoord + ") in dimension " + worldObj . provider . dimensionId + " failed to move carriage: " + Exception . getMessage ( ) ;

			if ( Exception instanceof CarriageObstructionException )
			{
				CarriageObstructionException ObstructionException = ( CarriageObstructionException ) Exception ;

				Message += " - (" + ObstructionException . X + "," + ObstructionException . Y + "," + ObstructionException . Z + ")" ;
			}

			if ( Configuration . Debug . LogMotionExceptions ){
				Debug . dbg ( Message ) ;
			}

			if(this.lastUsingPlayer!=null){
				this.lastUsingPlayer.addChatComponentMessage(new ChatComponentText(Message));
			}
		}
	}
	
	public CarriagePackage PreparePackage ( Directions dir ) throws CarriageMotionException
	{
		return prepareDefaultPackage(dir);
	}
	
	public CarriagePackage prepareDefaultPackage(Directions MotionDirection) throws CarriageMotionException{

		Moveable mv = CarriageMatchers.getMover(worldObj . getBlock ( xCoord + CarriageDirection . DeltaX , yCoord + CarriageDirection . DeltaY , zCoord + CarriageDirection . DeltaZ ),
				worldObj . getBlockMetadata ( xCoord + CarriageDirection . DeltaX , yCoord + CarriageDirection . DeltaY , zCoord + CarriageDirection . DeltaZ )
				, worldObj . getTileEntity ( xCoord + CarriageDirection . DeltaX , yCoord + CarriageDirection . DeltaY , zCoord + CarriageDirection . DeltaZ )) ;

		CarriagePackage _package = GeneratePackage(worldObj . getTileEntity ( xCoord + CarriageDirection . DeltaX , yCoord + CarriageDirection . DeltaY , zCoord + CarriageDirection . DeltaZ ), CarriageDirection, MotionDirection);
		
		if ( Configuration . HardmodeActive )
		{
			int Type = worldObj . getBlockMetadata ( xCoord , yCoord , zCoord ) ;

			{
				double MaxBurden = CarriageDrive . Types . values ( ) [ Type ] . MaxBurden * CarriageDrive . Tiers . values ( ) [ Tier ] . MaxBurdenFactor ;

				//System.out.println("Package mass: "+Package.Mass+", max burden "+ CarriageDrive . Types . values ( ) [ Type ] . MaxBurden+" * "+CarriageDrive.Tiers. values ( ) [ Tier ] . MaxBurdenFactor +" = "+MaxBurden);

				if ( _package . Mass > MaxBurden )
				{
					throw ( new CarriageMotionException ( "(HARDMODE) carriage too massive (by roughly " + ( ( int ) ( _package . Mass - MaxBurden ) ) + " units) for drive to handle" ) ) ;
				}
			}
			
			double EnergyRequired = _package . Mass * CarriageDrive . Types . values ( ) [ Type ] . EnergyConsumption * CarriageDrive . Tiers . values ( ) [ Tier ] . EnergyConsumptionFactor ;

			int powerConsumed=(int) Math.ceil(EnergyRequired*Configuration.PowerConsumptionFactor);

			//System.out.println("Moving carriage from "+Package.AnchorRecord.toString()+" containing "+Package.Mass+" blocks, using "+powerConsumed+" energy");

			if(powerConsumed>this.energyStored){
				throw ( new CarriageMotionException ( "(HARDMODE) not enough power to move carriage (have "+energyStored+", need "+powerConsumed));
			}else{
				Debug.dbg(this.energyStored);
				this.energyStored-=powerConsumed;
				Debug.dbg(this.energyStored);
			}
			
		}
		return ( _package ) ;
	}

	public BlockPosition GeneratePositionObject ( )
	{
		return ( new BlockPosition ( xCoord , yCoord , zCoord , worldObj . provider . dimensionId ) ) ;
	}

	public void InitiateMotion ( CarriagePackage Package )
	{
		
		ToggleActivity ( ) ;

		Package . RenderCacheKey = GeneratePositionObject ( ) ;

		RenderPacket . Dispatch ( Package ) ;

		EstablishPlaceholders ( Package ) ;

		RefreshWorld ( Package ) ;

		EstablishSpectre ( Package );
		
	}

	public void EstablishPlaceholders ( CarriagePackage Package )
	{
		for ( BlockRecord Record : Package . Body )
		{
			if ( Package . NewPositions . contains ( Record ) )
			{
				SneakyWorldUtil . SetBlock ( worldObj , Record . X , Record . Y , Record . Z , RIMBlocks . Spectre , Spectre . Types . Supportive . ordinal ( ) ) ;
			}
			else
			{
				SneakyWorldUtil . SetBlock ( worldObj , Record . X , Record . Y , Record . Z , RIMBlocks.air , 0 ) ;
			}
		}
	}

	public void RefreshWorld ( CarriagePackage Package )
	{
		for ( BlockRecord Record : Package . Body )
		{
			SneakyWorldUtil . RefreshBlock ( worldObj , Record . X , Record . Y , Record . Z , Record .block , Blocks.air ) ;
		}
	}

	public void EstablishSpectre ( CarriagePackage Package )
	{
		int CarriageX = Package . AnchorRecord . X + Package . MotionDirection . DeltaX ;
		int CarriageY = Package . AnchorRecord . Y + Package . MotionDirection . DeltaY ;
		int CarriageZ = Package . AnchorRecord . Z + Package . MotionDirection . DeltaZ ;

		WorldUtil . SetBlock ( worldObj , CarriageX , CarriageY , CarriageZ , RIMBlocks . Spectre , Spectre . Types . Motive . ordinal ( ) ) ;
		
		worldObj.setTileEntity(CarriageX, CarriageY, CarriageZ, new MotiveSpectreEntity());
		
		( ( MotiveSpectreEntity ) worldObj . getTileEntity ( CarriageX , CarriageY , CarriageZ ) ) . Absorb ( Package ) ;
	}

	public abstract CarriagePackage GeneratePackage ( net.minecraft.tileentity.TileEntity carriage , Directions CarriageDirection , Directions MotionDirection ) throws CarriageMotionException ;

	public abstract boolean Anchored ( ) ;


	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		int toRecieve=Math.min(Configuration.powerCapacity-energyStored, maxReceive);
		if(!simulate)
			energyStored+=toRecieve;
		return toRecieve;
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate){
		return 0;
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return Configuration.HardmodeActive;
	}

	public int getEnergyStored(ForgeDirection from){
		return this.energyStored;
	}

	public int getMaxEnergyStored(ForgeDirection from) {
		return Configuration.powerCapacity;
	}
}
