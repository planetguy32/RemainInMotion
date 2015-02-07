package me.planetguy.remaininmotion.api;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public interface FrameCarriageMatcher {
	
	public boolean isFrameCarriage(Block block1, int meta1, TileEntity entity1);

}
