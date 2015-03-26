package me.planetguy.remaininmotion.drive;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.planetguy.remaininmotion.api.event.BlockPreMoveEvent;
import me.planetguy.remaininmotion.api.event.MotionFinalizeEvent;

public class TranslocatorMoveListener {
	
	@SubscribeEvent
	public void onPreMove(BlockPreMoveEvent e) {
		if(e.location.entity() instanceof TileEntityCarriageTranslocator){
			TileEntityCarriageTranslocator te=(TileEntityCarriageTranslocator) e.location.entity();
			te.unregisterLabel();
		}
	}

	@SubscribeEvent
	public void onPostMove(MotionFinalizeEvent e) {
		if(e.location.entity() instanceof TileEntityCarriageTranslocator){
			TileEntityCarriageTranslocator te=(TileEntityCarriageTranslocator) e.location.entity();
			te.registerLabel();
		}
	}

}
