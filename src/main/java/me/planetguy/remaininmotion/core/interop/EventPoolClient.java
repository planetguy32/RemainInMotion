package me.planetguy.remaininmotion.core.interop;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import me.planetguy.remaininmotion.api.event.PostRenderDuringMovementEvent;
import me.planetguy.remaininmotion.api.event.PreRenderDuringMovementEvent;

public class EventPoolClient {

	private static PreRenderDuringMovementEvent thePreRenderDuringMovementEvent=new PreRenderDuringMovementEvent();
	private static PostRenderDuringMovementEvent thePostRenderDuringMovementEvent=new PostRenderDuringMovementEvent();
	
	public static boolean postPreRenderDuringMovementEvent(RenderBlocks blockRenderer, int x, int y, int z, TileEntity entity, int pass) {
		thePreRenderDuringMovementEvent.renderBlocks=blockRenderer;
		thePreRenderDuringMovementEvent.x=x;
		thePreRenderDuringMovementEvent.y=y;
		thePreRenderDuringMovementEvent.z=z;
		thePreRenderDuringMovementEvent.tile=entity;
		thePreRenderDuringMovementEvent.pass=pass;
		EventPool.postEvent(thePreRenderDuringMovementEvent);
		return thePreRenderDuringMovementEvent.isCanceled();
	}
	
	public static void postPostRenderDuringMovementEvent(RenderBlocks blockRenderer, int x, int y, int z, TileEntity entity, int pass) {
		thePostRenderDuringMovementEvent.renderBlocks=blockRenderer;
		thePostRenderDuringMovementEvent.x=x;
		thePostRenderDuringMovementEvent.y=y;
		thePostRenderDuringMovementEvent.z=z;
		thePostRenderDuringMovementEvent.tile=entity;
		thePostRenderDuringMovementEvent.pass=pass;
		EventPool.postEvent(thePostRenderDuringMovementEvent);
	}

}
