package me.planetguy.remaininmotion.api.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class RotatingTEPreUnpackEvent extends TEPreUnpackEvent {
	
	public final ForgeDirection axis;

	public RotatingTEPreUnpackEvent(TileEntity spectre, IBlockPos location, ForgeDirection axis) {
		super(spectre, location);
		this.axis=axis;
	}

}
