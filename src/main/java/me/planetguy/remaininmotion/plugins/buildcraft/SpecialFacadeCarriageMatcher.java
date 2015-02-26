package me.planetguy.remaininmotion.plugins.buildcraft;

import me.planetguy.remaininmotion.api.FrameCarriageMatcher;
import me.planetguy.remaininmotion.plugins.RemIMPluginsCommon;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.pluggable.IFacadePluggable;
import buildcraft.api.transport.pluggable.PipePluggable;

public class SpecialFacadeCarriageMatcher implements FrameCarriageMatcher {

	@Override
	public boolean isFrameCarriage(Block block1, int meta1, TileEntity entity1) {
		if (entity1 instanceof IPipeTile) {
			IPipeTile pipe = (IPipeTile) entity1;
			for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
				PipePluggable plug = pipe.getPipePluggable(direction);
				if (plug instanceof IFacadePluggable) {
					IFacadePluggable facade = (IFacadePluggable) plug;
					if (facade.getCurrentBlock() == RemIMPluginsCommon.getFrameBlock()
							&& facade.getCurrentMetadata() == 0) { // frame carriage facade
						return true;
					}
				}
			}
		}
		return false;
	}

}
