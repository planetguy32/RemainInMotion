package me.planetguy.remaininmotion.api.event;

import net.minecraft.tileentity.TileEntity;

public class TEPreUnpackEvent extends TEPlaceEvent {

	public TEPreUnpackEvent(TileEntity spectre, IBlockPos location) {
		super(spectre, location);
	}

}
