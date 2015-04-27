package me.planetguy.remaininmotion.core.interop;

import buildcraft.factory.BlockTank;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.planetguy.remaininmotion.api.event.BlockPreMoveEvent;
import me.planetguy.remaininmotion.api.event.BlockSelectForMoveEvent;
import me.planetguy.remaininmotion.api.event.BlocksReplacedEvent;
import me.planetguy.remaininmotion.api.event.CancelableOnBlockAddedEvent;
import me.planetguy.remaininmotion.api.event.EventManager;
import me.planetguy.remaininmotion.api.event.IBlockPos;
import me.planetguy.remaininmotion.api.event.MotionFinalizeEvent;
import me.planetguy.remaininmotion.api.event.TEPostPlaceEvent;
import me.planetguy.remaininmotion.api.event.TEPrePlaceEvent;
import me.planetguy.remaininmotion.api.event.TEPreUnpackEvent;
import me.planetguy.remaininmotion.util.position.BlockRecord;

public class EventHandlerDebugInspector {
	
	public void filter(IBlockPos pos){
		if(pos.world()==null){
			nop();
		}else if(pos.world().getBlock(pos.x(), pos.y(), pos.z()) instanceof BlockTank) {
			nop();
		}
		
	}
	
	public void nop(){}
	
	@SubscribeEvent
	public void onBlockSelectForMoveEvent(BlockSelectForMoveEvent e){
		filter(e.location);
	}
	
	@SubscribeEvent
	public void onBlockPreMoveEvent(BlockPreMoveEvent e){
		filter(e.location);
	}
	
	@SubscribeEvent
	public void onTEPreUnpackEvent(TEPreUnpackEvent e){
		filter(e.location);
	}
	
	@SubscribeEvent
	public void onTEPrePlaceEvent(TEPrePlaceEvent e){
		filter(e.location);
	}
	
	@SubscribeEvent
	public void onTEPostPlaceEvent(TEPostPlaceEvent e){
		filter(e.location);
	}
	
	@SubscribeEvent
	public void onCancelableOnBlockAddedEvent(CancelableOnBlockAddedEvent e){
		BlockRecord pos=new BlockRecord(e.xCoord, e.yCoord, e.zCoord);
		pos.Identify(e.worldObj);
		filter(pos);
	}
	
	@SubscribeEvent
	public void onMotionFinalizeEvent(MotionFinalizeEvent e){
		filter(e.location);
	}
	
	

}
