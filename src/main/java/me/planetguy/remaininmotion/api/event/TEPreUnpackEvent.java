package me.planetguy.remaininmotion.api.event;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TEPreUnpackEvent extends TEPlaceEvent {

	public TEPreUnpackEvent(TileEntity spectre, IBlockPos location) {
		super(spectre, location);
	}

}
