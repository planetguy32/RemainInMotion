package me.planetguy.remaininmotion ;

public class CarriageEngineEntity extends CarriageDriveEntity
{
	@Override
	public CarriagePackage GeneratePackage ( CarriageEntity Carriage , Directions CarriageDirection , Directions MotionDirection ) throws CarriageMotionException
	{
		CarriagePackage Package = new CarriagePackage ( this , Carriage , MotionDirection ) ;

		Package . AddBlock ( Package . DriveRecord ) ;

		if ( MotionDirection != CarriageDirection )
		{
			Package . AddPotentialObstruction ( Package . DriveRecord . NextInDirection ( MotionDirection ) ) ;
		}

		Carriage . FillPackage ( Package ) ;

		Package . Finalize ( ) ;

		return ( Package ) ;
	}

	@Override
	public boolean Anchored ( )
	{
		return ( false ) ;
	}
}
