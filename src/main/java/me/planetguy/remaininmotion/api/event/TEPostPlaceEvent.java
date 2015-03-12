package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TEPostPlaceEvent extends TEPlaceEvent{

	public TEPostPlaceEvent(TileEntity spectre, IBlockPos location) {
		super(spectre, location);
	}

}
