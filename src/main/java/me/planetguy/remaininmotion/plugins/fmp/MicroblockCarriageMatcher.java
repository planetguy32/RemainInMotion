package me.planetguy.remaininmotion.plugins.fmp;

import scala.collection.JavaConversions;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry.IMicroMaterial;
import codechicken.microblock.Microblock;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import me.planetguy.remaininmotion.api.FrameCarriageMatcher;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.plugins.RemIMPluginsCommon;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class MicroblockCarriageMatcher implements FrameCarriageMatcher {

	@Override
	public boolean isFrameCarriage(Block block1, int meta1, TileEntity entity1) {
		if(entity1 instanceof TileMultipart) {
			for(TMultiPart part: JavaConversions.asJavaIterable(((TileMultipart) entity1).partList())) {
				if(isMicroblockOfSimpleFrame(part)) {
					return true;
				}
			}
		}
		
		return false;
	}
	

	public static boolean isMicroblockOfSimpleFrame(TMultiPart part) {
		if(part instanceof Microblock) {
			Microblock mbPart=(Microblock) part;
			IMicroMaterial mat=mbPart.getIMaterial();
			if(mat instanceof BlockMicroMaterial) {
				if(((BlockMicroMaterial) mat).block()==RIMBlocks.plainFrame) {
					return true;
				}
			}
		}
		return false;
	}

}
