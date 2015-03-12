package me.planetguy.remaininmotion.api.event;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.eventhandler.Event;

public class TEPlaceEvent extends AbstractBlockMoveEvent{
	
    public final TileEntity spectre;
	
	public TEPlaceEvent(TileEntity spectre, IBlockPos location) {
		super(location);
        this.spectre = spectre;
	}

}
