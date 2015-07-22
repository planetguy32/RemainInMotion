package me.planetguy.remaininmotion.api.event;

public class BlockSelectForRotateEvent extends BlockSelectForMoveEvent {

	public final int axis;
	
	public BlockSelectForRotateEvent(IBlockPos location, int axis) {
		super(location);
		this.axis=axis;
	}

}
