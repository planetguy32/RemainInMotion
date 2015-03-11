package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;

public class TEPrePlaceEvent extends TEMoveEvent {

	public TEPrePlaceEvent(IBlockPos location) {
		super(location);
	}

}
