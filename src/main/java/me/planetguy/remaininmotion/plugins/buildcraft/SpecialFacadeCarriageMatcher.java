package me.planetguy.remaininmotion.plugins.buildcraft;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import me.planetguy.remaininmotion.api.CarriageMatcher;
import me.planetguy.remaininmotion.api.Moveable;

public class SpecialFacadeCarriageMatcher implements CarriageMatcher {

	@Override
	public boolean matches(Block block1, int meta1, TileEntity entity1,
			Block bloc2k, int meta2, TileEntity entity2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Moveable getCarriage(Block block, int meta, TileEntity te) {
		// TODO Auto-generated method stub
		return null;
	}

}
