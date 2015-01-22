package me.planetguy.remaininmotion.plugins.fmp;

import me.planetguy.remaininmotion.api.CarriageMatcher;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.carriage.TileEntityFrameCarriage;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

public class FMPCarriageMatcher implements CarriageMatcher {

	@Override
	public boolean matches(Block block1, int meta1, TileEntity entity1, Block bloc2k, int meta2, TileEntity entity2) {
		return entity1 instanceof TileEntityFrameCarriage && isFmpCarriage(entity2)
				|| (entity2 instanceof TileEntityFrameCarriage && isFmpCarriage(entity1));

	}

	@Override
	public Moveable getCarriage(Block block, int meta, TileEntity te) {
		return te instanceof TileMultipart ? getFMPCarriage((TileMultipart) te) : null;
	}

	public static boolean isFmpCarriage(TileEntity te) {
		if (te instanceof TileMultipart) {
			TileMultipart tm = (TileMultipart) te;
			for (TMultiPart part : tm.jPartList()) {
				if (part instanceof BlockCarriageFMP) { return true; }
			}
		}
		return false;
	}

	public static BlockCarriageFMP getFMPCarriage(TileMultipart tmp) {
		BlockCarriageFMP result = null;
		for (TMultiPart part : tmp.jPartList()) {
			if (part instanceof BlockCarriageFMP) {
				result = (BlockCarriageFMP) part;
			}
		}
		return result;
	}

}
