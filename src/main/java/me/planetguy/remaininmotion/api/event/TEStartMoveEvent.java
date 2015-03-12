package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.Event;

public class TEStartMoveEvent extends TEMoveEvent {
	
	private boolean excluded=false;
	
	private String cancelled=null;
	
	public TEStartMoveEvent(IBlockPos location) {
		super(location);
	}
	
	public void cancel(String message) {
		if(message==null)
			cancelled="unspecified";
		else
			cancelled=message;
	}
	
	public String getCancelMessag() {
		return cancelled;
	}
	
	public void exclude() {
		excluded=true;
	}
	
	public boolean isExcluded() {
		return excluded;
	}
	
}
