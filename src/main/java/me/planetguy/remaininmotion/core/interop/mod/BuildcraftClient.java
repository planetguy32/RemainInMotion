package me.planetguy.remaininmotion.core.interop.mod;

import java.util.HashMap;

import me.planetguy.remaininmotion.api.event.PostRenderDuringMovementEvent;
import me.planetguy.remaininmotion.api.event.PreRenderDuringMovementEvent;
import net.minecraft.util.ChunkCoordinates;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.TravelerSet;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;

public class BuildcraftClient {
	
	private HashMap<ChunkCoordinates, TravelerSet> cachedItems = new HashMap<ChunkCoordinates, TravelerSet>();
	
    @SubscribeEvent
    public void onPreRender(PreRenderDuringMovementEvent event) {
        if(FMLCommonHandler.instance().getSide() == Side.SERVER) return;
        if(event.pass != 0) return;
        if(event.tile != null && event.tile instanceof TileGenericPipe) {
            if(((TileGenericPipe) event.tile).pipe.transport instanceof PipeTransportItems) {
                cachedItems.put(new ChunkCoordinates(event.x, event.y, event.z), ((PipeTransportItems) ((TileGenericPipe) event.tile).pipe.transport).items);
                ((PipeTransportItems) ((TileGenericPipe) event.tile).pipe.transport).items.clear();
            }// else if(((TileGenericPipe) event.tile).pipe.transport instanceof PipeTransportFluids) {

            //}
        }
    }

    @SubscribeEvent
    public void onPostRender(PostRenderDuringMovementEvent event) {
        if(FMLCommonHandler.instance().getSide() == Side.SERVER) return;
        if(event.pass != 0) return;
        if(event.tile != null && event.tile instanceof TileGenericPipe) {
            if(((TileGenericPipe) event.tile).pipe.transport instanceof PipeTransportItems) {
                if(cachedItems != null && !cachedItems.isEmpty()) {
                    ((PipeTransportItems) ((TileGenericPipe) event.tile).pipe.transport).items.addAll(cachedItems.get(new ChunkCoordinates(event.x,event.y,event.z)));
                    cachedItems.remove(new ChunkCoordinates(event.x,event.y,event.z));
                }
            }// else if(((TileGenericPipe) event.tile).pipe.transport instanceof PipeTransportFluids) {

            //}
        }
    }

}
