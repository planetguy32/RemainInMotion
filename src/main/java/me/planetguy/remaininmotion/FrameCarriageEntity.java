package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.util.CarriageMotionException;

public class FrameCarriageEntity extends CarriageEntity
{
	@Override
	public void FillPackage ( CarriagePackage Package ) throws CarriageMotionException
	{
		int seen=0;
		
		BlockRecordSet CarriagesToCheck = new BlockRecordSet ( ) ;

		BlockRecordSet BlocksChecked = new BlockRecordSet ( ) ;

		BlocksChecked . add ( Package . DriveRecord ) ;

		BlocksChecked . add ( Package . AnchorRecord ) ;

		Package . AddBlock ( Package . AnchorRecord ) ;

		CarriagesToCheck . add ( Package . AnchorRecord ) ;

		while ( CarriagesToCheck . size ( ) > 0 )
		{
			seen++;
			BlockRecord CarriageRecord = CarriagesToCheck . pollFirst ( ) ;

			for ( Directions TargetDirection : Directions . values ( ) )
			{
				BlockRecord TargetRecord = CarriageRecord . NextInDirection ( TargetDirection ) ;

				if ( ( ( FrameCarriageEntity ) CarriageRecord . Entity ) . SideClosed [ TargetDirection . ordinal ( ) ] )
				{
					if ( TargetDirection == Package . MotionDirection )
					{
						Package . AddPotentialObstruction ( TargetRecord ) ;
					}

					continue ;
				}

				if ( ! BlocksChecked . add ( TargetRecord ) )
				{
					continue ;
				}

				if ( worldObj . isAirBlock ( TargetRecord . X , TargetRecord . Y , TargetRecord . Z ) )
				{
					continue ;
				}

				TargetRecord . Identify ( worldObj ) ;

				Package . AddBlock ( TargetRecord ) ;

				if ( Package . MatchesCarriageType ( TargetRecord ) )
				{
					CarriagesToCheck . add ( TargetRecord ) ;

					continue ;
				}

				if ( Package . MotionDirection != null )
				{
					Package . AddPotentialObstruction ( TargetRecord . NextInDirection ( Package . MotionDirection ) ) ;
				}
			}
		}
	}
}
