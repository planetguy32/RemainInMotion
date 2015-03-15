package me.planetguy.remaininmotion.api.event;

import net.minecraft.tileentity.TileEntity;

public class TEPlaceEvent extends AbstractBlockMoveEvent{
	
    public final TileEntity spectre;
	
	public TEPlaceEvent(TileEntity spectre, IBlockPos location) {
		super(location);
        this.spectre = spectre;
	}

}
