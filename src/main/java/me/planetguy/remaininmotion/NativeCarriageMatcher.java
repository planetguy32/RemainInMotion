package me.planetguy.remaininmotion;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import me.planetguy.remaininmotion.api.CarriageMatcher;
import me.planetguy.remaininmotion.api.Moveable;

public class NativeCarriageMatcher implements CarriageMatcher {

	@Override
	public boolean matches(Block block1, int meta1, TileEntity entity1,
			Block bloc2k, int meta2, TileEntity entity2) {
		return block1==bloc2k 
				&& meta1==meta2 
				&& entity1 instanceof Moveable 
				&& entity2 instanceof Moveable;
	}

	@Override
	public Moveable getCarriage(Block block, int meta, TileEntity te) {
		if(te instanceof Moveable)
			return (Moveable) te;
		else
			return null;
	}

}
