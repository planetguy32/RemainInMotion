package me.planetguy.remaininmotion.core.interop.mod;

import com.carpentersblocks.block.BlockCoverable;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.planetguy.remaininmotion.api.event.CancelableOnBlockAddedEvent;
import me.planetguy.remaininmotion.api.event.TEPreUnpackEvent;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import me.planetguy.remaininmotion.util.position.BlockRecord;

public class EventHandlerCarpentersBlocks {

    @SubscribeEvent
    public void onBlockAdded(CancelableOnBlockAddedEvent e) {
        if(e.worldObj.getBlock(e.xCoord,e.yCoord,e.zCoord) instanceof BlockCoverable) e.setCanceled(true);
    }

}
