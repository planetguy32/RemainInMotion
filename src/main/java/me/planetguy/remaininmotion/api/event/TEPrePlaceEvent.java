package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;

public class TEPrePlaceEvent extends TileEntityMoveEvent {

	public TEPrePlaceEvent(IBlockPos location, NBTTagCompound savedData) {
		super(location, savedData);
	}

}
