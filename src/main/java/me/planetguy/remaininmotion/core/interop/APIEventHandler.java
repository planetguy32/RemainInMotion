package me.planetguy.remaininmotion.core.interop;

import me.planetguy.remaininmotion.api.IMotionCallback;
import me.planetguy.remaininmotion.api.event.IBlockPos;
import me.planetguy.remaininmotion.api.event.MotionFinalizeEvent;
import me.planetguy.remaininmotion.api.event.TEStartMoveEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class APIEventHandler {
	
	@SubscribeEvent
	public void handle(MotionFinalizeEvent e) {
		IBlockPos record=e.location;
		if(record.entity() instanceof IMotionCallback) {
            ((IMotionCallback) record.entity()).onPlacedFromMotion();
        }
	}
	
	@SubscribeEvent
	public void handle(TEStartMoveEvent e) {
		
	}

}
