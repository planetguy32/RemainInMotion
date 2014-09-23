package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.ModInteraction;
import me.planetguy.remaininmotion.drive.CarriageTranslocatorEntity;
import me.planetguy.remaininmotion.util.WorldUtil;
import net.minecraft.block.Block;

public class TeleportativeSpectreEntity extends MotiveSpectreEntity
{
	public boolean Source ;

	public int ShiftX ;
	public int ShiftY ;
	public int ShiftZ ;

	public int MinX ;
	public int MinY ;
	public int MinZ ;
	public int MaxX ;
	public int MaxY ;
	public int MaxZ ;

	public int TargetDimension ;
	
	@Override
	public void WriteCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		super . WriteCommonRecord ( TagCompound ) ;

		TagCompound . setBoolean ( "Source" , Source ) ;

		TagCompound . setInteger ( "ShiftX" , ShiftX ) ;
		TagCompound . setInteger ( "ShiftY" , ShiftY ) ;
		TagCompound . setInteger ( "ShiftZ" , ShiftZ ) ;

		TagCompound . setInteger ( "MinX" , MinX ) ;
		TagCompound . setInteger ( "MinY" , MinY ) ;
		TagCompound . setInteger ( "MinZ" , MinZ ) ;
		TagCompound . setInteger ( "MaxX" , MaxX ) ;
		TagCompound . setInteger ( "MaxY" , MaxY ) ;
		TagCompound . setInteger ( "MaxZ" , MaxZ ) ;

		TagCompound . setInteger ( "TargetDimension" , TargetDimension ) ;
	}

	@Override
	public void ReadCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		super . ReadCommonRecord ( TagCompound ) ;

		Source = TagCompound . getBoolean ( "Source" ) ;

		ShiftX = TagCompound . getInteger ( "ShiftX" ) ;
		ShiftY = TagCompound . getInteger ( "ShiftY" ) ;
		ShiftZ = TagCompound . getInteger ( "ShiftZ" ) ;

		MinX = TagCompound . getInteger ( "MinX" ) ;
		MinY = TagCompound . getInteger ( "MinY" ) ;
		MinZ = TagCompound . getInteger ( "MinZ" ) ;
		MaxX = TagCompound . getInteger ( "MaxX" ) ;
		MaxY = TagCompound . getInteger ( "MaxY" ) ;
		MaxZ = TagCompound . getInteger ( "MaxZ" ) ;

		TargetDimension = TagCompound . getInteger ( "TargetDimension" ) ;
	}

	public void AbsorbCommon ( CarriagePackage Package )
	{
		ShiftX = - Package . DriveRecord . X + Package . Translocator . xCoord ;
		ShiftY = - Package . DriveRecord . Y + Package . Translocator . yCoord ;
		ShiftZ = - Package . DriveRecord . Z + Package . Translocator . zCoord ;

		MinX = Package . MinX ;
		MinY = Package . MinY ;
		MinZ = Package . MinZ ;
		MaxX = Package . MaxX ;
		MaxY = Package . MaxY ;
		MaxZ = Package . MaxZ ;

		TargetDimension = Package . Translocator.getWorldObj() . provider . dimensionId ;

		MotionDirection = Directions . values ( ) [ 0 ] ;
	}

	public void AbsorbSource ( CarriagePackage Package )
	{
		AbsorbCommon ( Package ) ;

		Source = true ;

		RenderCacheKey = Package . RenderCacheKey ;

		DriveRecord = new BlockRecord ( Package . DriveRecord ) ;

		{
			Body = new BlockRecordSet ( ) ;

			for ( BlockRecord Record : Package . Body )
			{
				Body . add ( new BlockRecord ( Record ) ) ;
			}
		}

		PendingBlockUpdates = new net . minecraft . nbt . NBTTagList ( ) ;
	}

	public void AbsorbSink ( CarriagePackage Package )
	{
		Absorb ( Package ) ;

		AbsorbCommon ( Package ) ;

		DriveRecord = new BlockRecord ( Package . Translocator . xCoord , Package . Translocator . yCoord , Package . Translocator . zCoord ) ;
	}

	@Override
	public void ShiftBlockPosition ( BlockRecord Record )
	{
		Record . X += ShiftX ;
		Record . Y += ShiftY ;
		Record . Z += ShiftZ ;
	}

	@Override
	public void ScheduleShiftedBlockUpdate ( net . minecraft . nbt . NBTTagCompound PendingBlockUpdateRecord )
	{
		worldObj . func_147446_b
		(
			PendingBlockUpdateRecord . getInteger ( "X" ) + ShiftX ,
			PendingBlockUpdateRecord . getInteger ( "Y" ) + ShiftY ,
			PendingBlockUpdateRecord . getInteger ( "Z" ) + ShiftZ ,

			Block.getBlockById(PendingBlockUpdateRecord . getInteger ( "Id" )) ,

			PendingBlockUpdateRecord . getInteger ( "Delay" ) ,

			PendingBlockUpdateRecord . getInteger ( "Priority" )
		) ;
	}

	@Override
	public void updateEntity ( )
	{
		TicksExisted ++ ;

		if ( worldObj . isRemote )
		{
			return ;
		}

		if ( TicksExisted < Configuration . CarriageMotion . TeleportationDuration )
		{
			return ;
		}

		if ( Source )
		{
			try
			{
				( ( CarriageTranslocatorEntity ) worldObj . getTileEntity ( DriveRecord . X , DriveRecord . Y , DriveRecord . Z ) ) . ToggleActivity ( ) ;
			}
			catch ( Throwable Throwable )
			{
				Throwable . printStackTrace ( ) ;
			}

			for ( BlockRecord Record : Body )
			{
				WorldUtil . ClearBlock ( worldObj , Record . X , Record . Y , Record . Z ) ;
			}

			CaptureEntities ( MinX , MinY , MinZ , MaxX , MaxY , MaxZ ) ;

			return ;
		}

		Release ( ) ;
	}

	@Override
	public boolean ShouldCaptureEntity ( net . minecraft . entity . Entity Entity )
	{
		if ( ! Configuration . CarriageMotion . TeleportEntities )
		{
			return ( false ) ;
		}

		return ( super . ShouldCaptureEntity ( Entity ) ) ;
	}

	@Override
	public void ProcessCapturedEntity ( net . minecraft . entity . Entity Entity )
	{
		if ( Entity . riddenByEntity == null )
		{
			TeleportEntity ( Entity ) ;
		}
	}

	public net . minecraft . entity . Entity TeleportEntity ( net . minecraft . entity . Entity Entity )
	{
		net . minecraft . server . MinecraftServer Server = cpw . mods . fml . common . FMLCommonHandler . instance ( ) . getMinecraftServerInstance ( ) ;

		net . minecraft . world . WorldServer HomeWorld = ( net . minecraft . world . WorldServer ) worldObj ;

		net . minecraft . world . WorldServer TargetWorld = Server . worldServerForDimension ( TargetDimension ) ;

		TeleportativeSpectreTeleporter Teleporter = new TeleportativeSpectreTeleporter ( worldObj ) ;

		boolean Transdimensional = ( HomeWorld . provider . dimensionId != TargetWorld . provider . dimensionId ) ;

		double X = Entity . posX + ShiftX ;
		double Y = Entity . posY + ShiftY ;
		double Z = Entity . posZ + ShiftZ ;
		float Yaw = Entity . rotationYaw ;
		float Pitch = Entity . rotationPitch ;

		net . minecraft . entity . Entity Mount = Entity . ridingEntity ;

		if ( Mount != null )
		{
			Entity . mountEntity ( null ) ;

			Mount = TeleportEntity ( Mount ) ;
		}

		if ( Entity instanceof net . minecraft . entity . player . EntityPlayerMP )
		{
			net . minecraft . entity . player . EntityPlayerMP Player = ( net . minecraft . entity . player . EntityPlayerMP ) Entity ;

			if ( Transdimensional )
			{
				Server . getConfigurationManager ( ) . transferPlayerToDimension ( Player , TargetDimension , Teleporter ) ;
			}

			Player . playerNetServerHandler . setPlayerLocation ( X , Y , Z , Yaw , Pitch ) ;

			Player . setLocationAndAngles ( X , Y , Z , Yaw , Pitch ) ;
		}
		else
		{
			if ( Transdimensional )
			{
				Entity . dimension = TargetDimension ;

				HomeWorld . removeEntity ( Entity ) ;

				Entity . isDead = false ;

				Server . getConfigurationManager ( ) . transferEntityToWorld ( Entity , TargetDimension , HomeWorld , TargetWorld ) ;

				net . minecraft . entity . Entity NewEntity = net . minecraft . entity . EntityList . createEntityByName ( net . minecraft . entity . EntityList . getEntityString ( Entity ) , TargetWorld ) ;

				NewEntity . copyDataFrom ( Entity , true ) ;

				NewEntity . setLocationAndAngles ( X , Y , Z , Yaw , Pitch ) ;

				TargetWorld . spawnEntityInWorld ( NewEntity ) ;

				Entity . isDead = true ;

				HomeWorld . resetUpdateEntityTick ( ) ;

				TargetWorld . resetUpdateEntityTick ( ) ;

				Entity = NewEntity ;
			}
			else
			{
				Entity . setLocationAndAngles ( X , Y , Z , Yaw , Pitch ) ;

				TargetWorld . updateEntityWithOptionalForce ( Entity , false ) ;
			}
		}

		if ( Mount != null )
		{
			Entity . mountEntity ( Mount ) ;
		}

		return ( Entity ) ;
	}
}
