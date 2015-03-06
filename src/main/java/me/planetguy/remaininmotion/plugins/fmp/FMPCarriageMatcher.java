package me.planetguy.remaininmotion.plugins.fmp;

import me.planetguy.remaininmotion.api.FrameCarriageMatcher;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

public class FMPCarriageMatcher implements FrameCarriageMatcher {

	@Override
	public boolean isFrameCarriage(Block block1, int meta1, TileEntity entity1) {
		return isFmpCarriage(entity1);
	}

	public static boolean isFmpCarriage(TileEntity te) {
		if (te instanceof TileMultipart) {
			TileMultipart tm = (TileMultipart) te;
			for (TMultiPart part : tm.jPartList()) {
				if (part instanceof PartCarriageFMP) { return true; }
			}
		}
		return false;
	}

	public static PartCarriageFMP getFMPCarriage(TileMultipart tmp) {
		PartCarriageFMP result = null;
		for (TMultiPart part : tmp.jPartList()) {
			if (part instanceof PartCarriageFMP) {
				result = (PartCarriageFMP) part;
			}
		}
		return result;
	}

}
