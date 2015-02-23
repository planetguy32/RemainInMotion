package me.planetguy.remaininmotion.api;

public interface IMotionCallback {
	
	/**
	 * Called when RemIM is planning to pick up a block. 
	 * 
	 * @return whether the block can be moved. Return 0 here to continue motion,
	 * 1 to cancel moving only the current block or 2 to cancel the whole move operation.
	 */
	public int onSelectedForMotion();
	
	/**
	 * Called when RemIM has finished moving a block and the tile entity has been
	 * set up in the world.
	 */
	public void onPlacedFromMotion();
	
}
