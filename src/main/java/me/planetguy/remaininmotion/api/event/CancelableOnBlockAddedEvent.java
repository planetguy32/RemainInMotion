package me.planetguy.remaininmotion.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

/**
 * Can be used to stop calls to onBlockAdded (eg. to make Carpenter's Blocks not reset their states)
 */
@Cancelable
public class CancelableOnBlockAddedEvent extends Event {

    public World worldObj;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    
    public CancelableOnBlockAddedEvent(World world, int x, int y, int z) {
        worldObj = world;
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }
    
    public boolean isCancelable(){
    	return true;
    }
}
