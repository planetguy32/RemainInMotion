package me.planetguy.remaininmotion.api.event;

import java.util.Set;

public class BlockPreMoveEvent extends AbstractBlockMoveEvent {

	public final IBlockPos newLoc;

	/* Changes to this may result in only partially moved blocks, as the earliest move stages have already finished. */
	public final Set<IBlockPos> blocks;

	
	public BlockPreMoveEvent(IBlockPos loc, IBlockPos possibleNextLocation, Set blocks) {
		super(loc);
		this.newLoc=possibleNextLocation;
		this.blocks=blocks;
	}

}
