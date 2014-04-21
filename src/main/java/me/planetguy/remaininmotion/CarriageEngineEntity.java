package me.planetguy.remaininmotion ;

<<<<<<< HEAD:src/me/planetguy/remaininmotion/CarriageEngineEntity.java
import net.minecraft.tileentity.TileEntity;
=======
import me.planetguy.remaininmotion.util.CarriageMotionException;
>>>>>>> 98ce934fa8adaa9996508250d82fa78ffb003353:src/main/java/me/planetguy/remaininmotion/CarriageEngineEntity.java

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

		TEAccessUtil.fillPackage(Package, carriage ) ;
		
		Package . Finalize ( ) ;

		return ( Package ) ;
	}

	@Override
	public boolean Anchored ( )
	{
		return ( false ) ;
	}
}
