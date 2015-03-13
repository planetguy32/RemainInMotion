package me.planetguy.remaininmotion.core.interop;

import net.minecraft.tileentity.TileEntity;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.api.IMotionCallback;
import me.planetguy.remaininmotion.api.ISpecialMoveBehavior;
import me.planetguy.remaininmotion.api.event.IBlockPos;
import me.planetguy.remaininmotion.api.event.MotionFinalizeEvent;
import me.planetguy.remaininmotion.api.event.BlockSelectForMoveEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerAPI {
	
	@SubscribeEvent
	public void handle(MotionFinalizeEvent e) {
		IBlockPos record=e.location;
		if(record.entity() instanceof IMotionCallback) {
            ((IMotionCallback) record.entity()).onPlacedFromMotion();
        }
	}
	
	@SubscribeEvent
	public void handle(BlockSelectForMoveEvent e) {
		TileEntity te=e.location.entity();
		if(te instanceof IMotionCallback) {
			int result=(((IMotionCallback) te).onSelectedForMotion());
			if(result == 1)
				e.exclude();
			else if(result == 2)
				e.cancel("");
		}
		if (te instanceof ISpecialMoveBehavior)
			try {
				((ISpecialMoveBehavior) te).onAdded(CarriagePackage.activePackage, e.location.entityTag());
			} catch (CarriageMotionException e1) {
				e.cancel(e1.getLocalizedMessage());
			}
		
	}

}
