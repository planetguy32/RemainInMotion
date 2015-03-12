package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.Event;

public class BlockSelectForMoveEvent extends AbstractTEMoveEvent {
	
	private boolean excluded=false;
	
	private String cancelled=null;
	
	public BlockSelectForMoveEvent(IBlockPos location) {
		super(location);
	}
	
	/**
	 * Cancels the entire motion operation.
	 * @param message
	 */
	public void cancel(String message) {
		if(message==null)
			cancelled="unspecified";
		else
			cancelled=message;
	}
	
	public String getCancelMessag() {
		return cancelled;
	}
	
	/**
	 * Excludes the given location from being moved, but allows other motion to proceed.
	 */
	public void exclude() {
		excluded=true;
	}
	
	public boolean isExcluded() {
		return excluded;
	}
	
}
