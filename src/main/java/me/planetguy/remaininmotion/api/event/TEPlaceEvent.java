package me.planetguy.remaininmotion.api.event;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.Event;

public class TEPlaceEvent extends BlockMoveEvent{
	
	public final BlockRecord record;

    public final TileEntityMotiveSpectre spectre;
	
	public TEPlaceEvent(TileEntityMotiveSpectre spectre, IBlockPos location, BlockRecord record) {
		super(location);
		this.record = record;
        this.spectre = spectre;
	}

}
