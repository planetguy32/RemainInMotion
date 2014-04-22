package me.planetguy.remaininmotion;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
<<<<<<< HEAD:src/main/java/me/planetguy/remaininmotion/TEAccessUtil.java
import me.planetguy.remaininmotion.util.CarriageMotionException;
=======
import cpw.mods.fml.common.Optional;
import me.planetguy.remaininmotion.fmp.FMPCarriage;
>>>>>>> 6eb56ea... FMP dependency is now "soft": It won't crash if you don't have FMP. (Development will still require it, though.):src/me/planetguy/remaininmotion/TEAccessUtil.java
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TEAccessUtil {

	public static void fillPackage(CarriagePackage package1, TileEntity carriage) throws CarriageMotionException {
		if(carriage instanceof me.planetguy.remaininmotion.CarriageEntity){
			((CarriageEntity)carriage).FillPackage(package1);
<<<<<<< HEAD:src/main/java/me/planetguy/remaininmotion/TEAccessUtil.java
		}else if(carriage instanceof TileMultipart){
			if(getFMPCarriage((TileMultipart) carriage)!=null)
				fillFramePackage(package1, carriage.getWorldObj());
		}
=======
		}else
			try{
				if(carriage instanceof TileMultipart){
					if(getFMPCarriage((TileMultipart) carriage)!=null)
						fillFramePackage(package1, carriage.worldObj);
				}
			}catch(Error noFmpStuff){

			}
>>>>>>> 6eb56ea... FMP dependency is now "soft": It won't crash if you don't have FMP. (Development will still require it, though.):src/me/planetguy/remaininmotion/TEAccessUtil.java
	}

	@Optional.Method(modid = "ForgeMultipart")
	public static FMPCarriage getFMPCarriage(TileMultipart tmp){
		FMPCarriage result=null;
		for(TMultiPart part:((TileMultipart) tmp).jPartList()){
			if(part instanceof FMPCarriage){
				result=(FMPCarriage) part;
			}
		}
		return result;
	}
	
	public static boolean isFmpCarriage(TileEntity te){
		try{
			if(te instanceof TileMultipart){
				TileMultipart tm=(TileMultipart) te;
				for(TMultiPart part:tm.jPartList()){
					if(part instanceof FMPCarriage){
						return true;
					}
				}
			}
		}catch(Error e){
		}
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
