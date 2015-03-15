package me.planetguy.remaininmotion.plugins.fmp;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import me.planetguy.remaininmotion.api.ConnectabilityState;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class MicroblockCloseableFactory implements ICloseableFactory {

	@Override
	public ICloseable retrieve(TileEntity entity1) {
		if(entity1 instanceof TileMultipart) {
			final TileMultipart tile = (TileMultipart) entity1;
			return new ICloseable() {

				@Override
				public ConnectabilityState isSideClosed(int side) {
					TMultiPart part=tile.partMap(side);
					if(MicroblockCarriageMatcher.isMicroblockOfSimpleFrame(part)) {
						return ConnectabilityState.OPEN; // if facade then
					}else {
						for(int i=0; i<6; i++) {
							if(i != side && i != ForgeDirection.OPPOSITES[i]) {
								if(MicroblockCarriageMatcher.isMicroblockOfSimpleFrame(tile.partMap(i)))
									return ConnectabilityState.FRAMES_ONLY; // if facade then
							}
						}
					}
					return ConnectabilityState.CLOSED;

				}

			};
		}
		return null;
	}

	@Override
	public Class<?> validClass() {
		return TileMultipart.class;
	}

}
