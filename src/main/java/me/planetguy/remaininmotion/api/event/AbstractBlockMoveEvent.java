package me.planetguy.remaininmotion.api.event;

import cpw.mods.fml.common.eventhandler.Event;

public abstract class AbstractBlockMoveEvent extends Event{
	
	public final IBlockPos location;
	
	public AbstractBlockMoveEvent(IBlockPos loc) {
		this.location=loc;
	}


}
