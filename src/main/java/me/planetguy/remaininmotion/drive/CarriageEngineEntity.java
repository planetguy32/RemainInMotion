package me.planetguy.remaininmotion.drive ;

import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.tileentity.TileEntity;

public class CarriageEngineEntity extends CarriageDriveEntity
{
	@Override
	public CarriagePackage GeneratePackage ( TileEntity carriage , Directions CarriageDirection , Directions MotionDirection ) throws CarriageMotionException
	{
		CarriagePackage Package = new CarriagePackage ( this , carriage , MotionDirection ) ;

		Package . AddBlock ( Package . DriveRecord ) ;

		if ( MotionDirection != CarriageDirection )
		{
			Package . AddPotentialObstruction ( Package . DriveRecord . NextInDirection ( MotionDirection ) ) ;
		}

		MultiTypeCarriageUtil.fillPackage(Package, carriage ) ;
		
		Package . Finalize ( ) ;

		return ( Package ) ;
	}

	@Override
	public boolean Anchored ( )
	{
		return ( false ) ;
	}

}
