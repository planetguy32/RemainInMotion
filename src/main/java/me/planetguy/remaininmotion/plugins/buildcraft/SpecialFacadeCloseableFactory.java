package me.planetguy.remaininmotion.plugins.buildcraft;

import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.api.ConnectabilityState;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.pluggable.IFacadePluggable;
import buildcraft.api.transport.pluggable.PipePluggable;

public class SpecialFacadeCloseableFactory implements ICloseableFactory {

	@Override
	public ICloseable retrieve(TileEntity entity1) {
		if (entity1 instanceof IPipeTile) {
			final IPipeTile pipe = (IPipeTile) entity1;
			return new ICloseable() {

				@Override
				public ConnectabilityState isSideClosed(int side) {
					PipePluggable plug = pipe.getPipePluggable(ForgeDirection.values()[side]);
					if (plug instanceof IFacadePluggable) {
						IFacadePluggable facade = (IFacadePluggable) plug;
						if (facade.getCurrentBlock() == RIMBlocks.plainFrame && facade.getCurrentMetadata() == 0) { // frame
							return ConnectabilityState.OPEN;
						}
					} else {
						for (int i = 0; i < 6; i++) {
							// exclude opposite and this side.
							if (i != side && i != Directions.values()[side].Opposite) { 
								PipePluggable plug2 = pipe.getPipePluggable(ForgeDirection.values()[i]);
								if (plug2 instanceof IFacadePluggable) {
									IFacadePluggable facade = (IFacadePluggable) plug2;
									if (facade.getCurrentBlock() == RIMBlocks.plainFrame && facade.getCurrentMetadata() == 0) {
										return ConnectabilityState.FRAMES_ONLY; 
									}
								}
								
								
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
		return IPipeTile.class;
	}

}
