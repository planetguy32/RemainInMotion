package me.planetguy.remaininmotion.api.event;

import net.minecraft.tileentity.TileEntity;

public class RotatingTEPreUnpackEvent extends TEPreUnpackEvent {

	public RotatingTEPreUnpackEvent(TileEntity spectre, IBlockPos location) {
		super(spectre, location);
	}

}
