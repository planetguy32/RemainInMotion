package me.planetguy.remaininmotion.plugins.buildcraft;

import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.pluggable.IFacadePluggable;
import buildcraft.api.transport.pluggable.PipePluggable;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;
import me.planetguy.remaininmotion.core.RIMBlocks;

public class SpecialFacadeCloseableFactory implements ICloseableFactory {

	@Override
	public ICloseable retrieve(TileEntity entity1) {
		if( entity1 instanceof IPipeTile) {
			final IPipeTile pipe=(IPipeTile) entity1;
			return new ICloseable() {

				@Override
				public boolean isSideClosed(int side) {
					PipePluggable plug=pipe.getPipePluggable(ForgeDirection.values()[side]);
					if(plug instanceof IFacadePluggable) {
						IFacadePluggable facade=(IFacadePluggable) plug;
						if(facade.getCurrentBlock()==RIMBlocks.Carriage
								&& facade.getCurrentMetadata() ==0) { //frame carriage facade
							return false;	 //if facade then not closed					
						}
					}
					return true;
						
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
