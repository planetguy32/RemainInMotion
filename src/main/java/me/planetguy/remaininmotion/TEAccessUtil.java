package me.planetguy.remaininmotion;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import me.planetguy.remaininmotion.util.CarriageMotionException;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TEAccessUtil {

	public static void fillPackage(CarriagePackage package1, TileEntity carriage) throws CarriageMotionException {
		if(carriage instanceof me.planetguy.remaininmotion.CarriageEntity){
			((CarriageEntity)carriage).FillPackage(package1);
		}else if(carriage instanceof TileMultipart){
			if(getFMPCarriage((TileMultipart) carriage)!=null)
				fillFramePackage(package1, carriage.getWorldObj());
		}
	}

	public static FMPCarriage getFMPCarriage(TileMultipart tmp){
		FMPCarriage result=null;
		for(TMultiPart part:((TileMultipart) tmp).jPartList()){
			if(part instanceof FMPCarriage){
				result=(FMPCarriage) part;
			}
		}
		return result;
	}

	public static boolean isAnchored(TileEntity drive) {
		return false;
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
