package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;

public class TEPostPlaceEvent extends TEMoveEvent{

	public TEPostPlaceEvent(IBlockPos location) {
		super(location);
	}

}
