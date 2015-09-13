package me.planetguy.remaininmotion.api.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class RotatingTEPreUnpackEvent extends TEPreUnpackEvent {
	
	public ForgeDirection axis;

}
