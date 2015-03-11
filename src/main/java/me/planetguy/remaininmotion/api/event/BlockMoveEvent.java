package me.planetguy.remaininmotion.api.event;

import cpw.mods.fml.common.eventhandler.Event;

public class BlockMoveEvent extends Event{
	
	public final IBlockPos location;
	
	public BlockMoveEvent(IBlockPos loc) {
		this.location=loc;
	}


}
