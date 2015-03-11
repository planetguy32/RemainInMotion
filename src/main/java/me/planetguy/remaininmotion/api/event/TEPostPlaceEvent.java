package me.planetguy.remaininmotion.api.event;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import net.minecraft.nbt.NBTTagCompound;

public class TEPostPlaceEvent extends TileEntityMoveEvent{

	public TEPostPlaceEvent(TileEntityMotiveSpectre spectre, IBlockPos location,
			BlockRecord record) {
		super(spectre, location, record);
	}

}
