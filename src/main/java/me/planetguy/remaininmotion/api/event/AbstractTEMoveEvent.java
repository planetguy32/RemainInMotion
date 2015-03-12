package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.Event;

public abstract class AbstractTEMoveEvent extends AbstractBlockMoveEvent{
	
	public AbstractTEMoveEvent(IBlockPos location) {
		super(location);
	}

}
