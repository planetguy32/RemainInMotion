package me.planetguy.remaininmotion.plugins.buildcraft;

import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import buildcraft.api.transport.pluggable.IFacadePluggable;

public class SpecialFacade implements IFacadePluggable {

	@Override
	public Block getCurrentBlock() {
		return RIMBlocks.Carriage;
	}

	@Override
	public int getCurrentMetadata() {
		return 0;
	}

	@Override
	public boolean isTransparent() {
		return false;
	}

	@Override
	public boolean isHollow() {
		return false;
	}

}
