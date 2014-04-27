package me.planetguy.remaininmotion.fmp;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import me.planetguy.remaininmotion.FrameCarriageEntity;
import me.planetguy.remaininmotion.api.CarriageMatcher;
import me.planetguy.remaininmotion.api.Moveable;

public class FMPCarriageMatcher implements CarriageMatcher{

	@Override
	public boolean matches(Block block1, int meta1, TileEntity entity1,
			Block bloc2k, int meta2, TileEntity entity2) {
		return entity1 instanceof FrameCarriageEntity
				&&isFmpCarriage(entity2)
				||(entity2 instanceof FrameCarriageEntity
				&&isFmpCarriage(entity1));

	}

	@Override
	public Moveable getCarriage(Block block, int meta, TileEntity te) {
		return te instanceof TileMultipart ? getFMPCarriage((TileMultipart)te) : null;
	}

	public static boolean isFmpCarriage(TileEntity te){
		if(te instanceof TileMultipart){
			TileMultipart tm=(TileMultipart) te;
			for(TMultiPart part:tm.jPartList()){
				if(part instanceof FMPCarriage){
					return true;
				}
			}
		}
		return false;
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

}
