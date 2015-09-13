package me.planetguy.remaininmotion.api.event;

import java.util.Set;

public class BlockPreMoveEvent extends AbstractBlockMoveEvent {

	public IBlockPos newLoc;

	/* Changes to this may result in only partially moved blocks, as the earliest move stages have already finished. */
	public Set<IBlockPos> blocks;

}
