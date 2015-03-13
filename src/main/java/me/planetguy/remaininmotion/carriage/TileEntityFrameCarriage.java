package me.planetguy.remaininmotion.carriage;

import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.item.ItemStack;

public class TileEntityFrameCarriage extends TileEntityCarriage {
	public TileEntityFrameCarriage() {}

	@Override
	public void fillPackage(CarriagePackage Package) throws CarriageMotionException {
		MultiTypeCarriageUtil.fillFramePackage(Package, worldObj);
	}
	
	public void EmitDrops(BlockRiM Block, int Meta)  {
		if(this.getDecoration() == null) {
			EmitDrop(Block, new ItemStack(RIMBlocks.plainFrame));
		}else {
			super.EmitDrops(Block, Meta) ;
		}
	}

}
