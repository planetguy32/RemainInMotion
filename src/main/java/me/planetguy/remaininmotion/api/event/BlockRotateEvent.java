package me.planetguy.remaininmotion.api.event;

import net.minecraftforge.common.util.ForgeDirection;

public class BlockRotateEvent extends BlockMoveEvent{

	public final ForgeDirection axis;
	
	public BlockRotateEvent(IBlockPos loc, ForgeDirection axis) {
		super(loc);
		this.axis=axis;
	}
	
}
