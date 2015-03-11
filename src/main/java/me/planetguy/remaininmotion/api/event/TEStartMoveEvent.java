package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.Event;

public class TEStartMoveEvent extends TEMoveEvent {
	
	private boolean cancelled, excluded;
	
	public TEStartMoveEvent(IBlockPos location) {
		super(location);
	}
	
	public void cancel() {
		cancelled=true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void exclude() {
		excluded=true;
	}
	
	public boolean isExcluded() {
		return excluded;
	}
	
}
