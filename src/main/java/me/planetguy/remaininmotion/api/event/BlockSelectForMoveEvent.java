package me.planetguy.remaininmotion.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.nbt.NBTTagCompound;

@Cancelable
public class BlockSelectForMoveEvent extends AbstractBlockMoveEvent {
	
	private boolean excluded=false;
	
	private String cancelled=null;
	
	public boolean isCancelable() {
		return true;
	}
	
	/**
	 * Cancels the entire motion operation.
	 * @param message
	 */
	public void cancel(String message) {
		if(message==null)
			addCancel("unspecified");
		else
			addCancel(message);
		setCanceled(true);
	}
	
	private void addCancel(String m) {
		if(cancelled==null)
			cancelled=m;
		else
			cancelled+=", "+m;
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
