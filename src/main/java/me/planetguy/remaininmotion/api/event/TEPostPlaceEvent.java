package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;

public class TEPostPlaceEvent extends TileEntityMoveEvent{

	public TEPostPlaceEvent(IBlockPos location,
			NBTTagCompound savedData) {
		super(location, savedData);
	}

}
