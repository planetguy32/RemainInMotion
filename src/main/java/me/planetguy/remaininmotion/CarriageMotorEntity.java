package me.planetguy.remaininmotion ;

<<<<<<< HEAD:src/me/planetguy/remaininmotion/CarriageMotorEntity.java
import net.minecraft.tileentity.TileEntity;
=======
import me.planetguy.remaininmotion.util.CarriageMotionException;
import me.planetguy.remaininmotion.util.CarriageObstructionException;
>>>>>>> 98ce934fa8adaa9996508250d82fa78ffb003353:src/main/java/me/planetguy/remaininmotion/CarriageMotorEntity.java

public class CarriageMotorEntity extends CarriageDriveEntity
{
	@Override
	public CarriagePackage GeneratePackage ( TileEntity carriage , Directions CarriageDirection , Directions MotionDirection ) throws CarriageMotionException
	{
		if ( MotionDirection == CarriageDirection )
		{
			throw ( new CarriageMotionException ( "motor cannot push carriage away from itself" ) ) ;
		}

		if ( MotionDirection == CarriageDirection . Opposite ( ) )
		{
			throw ( new CarriageMotionException ( "motor cannot pull carriage into itself" ) ) ;
		}

		CarriagePackage Package = new CarriagePackage ( this , carriage , MotionDirection ) ;

		TEAccessUtil.fillPackage(Package, carriage ) ;

		if ( Package . Body . contains ( Package . DriveRecord ) )
		{
			throw ( new CarriageMotionException ( "carriage is attempting to move motor" ) ) ;
		}

		if ( Package . Body . contains ( Package . DriveRecord . NextInDirection ( MotionDirection . Opposite ( ) ) ) )
		{
			throw ( new CarriageObstructionException ( "carriage motion is obstructed by motor" , xCoord , yCoord , zCoord ) ) ;
		}

		Package . Finalize ( ) ;

		return ( Package ) ;
	}

	@Override
	public boolean Anchored ( )
	{
		return ( true ) ;
	}
}
