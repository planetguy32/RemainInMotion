package me.planetguy.remaininmotion.api.event;

public class BlockPreMoveEvent extends BlockMoveEvent {

	public final IBlockPos newLoc;
	
	public BlockPreMoveEvent(IBlockPos loc, IBlockPos possibleNextLocation) {
		super(loc);
		this.newLoc=possibleNextLocation;
	}

}
