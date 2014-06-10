package me.planetguy.remaininmotion.util;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.BlockRecordSet;
import me.planetguy.remaininmotion.CarriageMatchers;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.base.RIMBlock;
import me.planetguy.remaininmotion.carriage.FrameCarriageEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public abstract class MultiTypeCarriageUtil {
	
	public static void fillPackage(CarriagePackage package1, TileEntity carriage) throws CarriageMotionException {
		CarriageMatchers.getMover(package1.AnchorRecord.Id, package1.AnchorRecord.Meta, package1.AnchorRecord.Entity).fillPackage(package1);
	}

	public static void fillFramePackage ( CarriagePackage Package, World worldObj ) throws CarriageMotionException
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

				if(CarriageRecord.Entity instanceof FrameCarriageEntity)

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
