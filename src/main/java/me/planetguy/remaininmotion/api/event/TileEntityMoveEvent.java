package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.Event;

public class TileEntityMoveEvent extends BlockMoveEvent{
	
	public final NBTTagCompound savedData;
	
	public TileEntityMoveEvent(IBlockPos location, NBTTagCompound savedData) {
		super(location);
		this.savedData=savedData;
	}

}
