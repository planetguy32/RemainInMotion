package me.planetguy.remaininmotion;

import me.planetguy.remaininmotion.api.CarriageMatcher;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.carriage.TileEntityFrameCarriage;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class DefaultCarriageMatcher implements CarriageMatcher {

	@Override
	public boolean matches(Block block1, int meta1, TileEntity entity1, Block bloc2k, int meta2, TileEntity entity2) {
		return entity1 instanceof TileEntityFrameCarriage && entity2 instanceof TileEntityFrameCarriage;
	}

	@Override
	public Moveable getCarriage(Block block, int meta, TileEntity te) {
		if (te instanceof Moveable) {
			return (Moveable) te;
		} else {
			return null;
		}
	}

}
