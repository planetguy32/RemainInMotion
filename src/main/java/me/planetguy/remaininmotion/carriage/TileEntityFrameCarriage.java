package me.planetguy.remaininmotion.carriage;

import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFrameCarriage extends TileEntityCarriage {
	public TileEntityFrameCarriage() {
		RiMRegistry.registerCloseableFactory(new ICloseableFactory() {
			@Override
			public ICloseable retrieve(TileEntity te) {
				return (ICloseable) te;
			}

			@Override
			public Class validClass() {
				return TileEntityFrameCarriage.class;
			}
		});
	}

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
