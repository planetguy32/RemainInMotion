package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.Event;

public class TileEntitySaveToMoveEvent extends Event{
	
	public final IBlockPos location;
	
	public NBTTagCompound savedData;
	
	public TileEntitySaveToMoveEvent(IBlockPos location, NBTTagCompound savedData) {
		this.location=location;
		this.savedData=savedData;
	}

}
