package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.Event;

public abstract class TEMoveEvent extends BlockMoveEvent{
	
	public TEMoveEvent(IBlockPos location) {
		super(location);
	}

}
