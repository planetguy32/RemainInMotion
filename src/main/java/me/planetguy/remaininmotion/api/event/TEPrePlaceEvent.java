package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TEPrePlaceEvent extends TEPlaceEvent{

	public TEPrePlaceEvent(TileEntity spectre, IBlockPos location) {
		super(spectre, location);
	}

}
