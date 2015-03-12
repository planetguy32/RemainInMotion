package me.planetguy.remaininmotion.api.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.tileentity.TileEntity;

public class UnpackStartEvent extends Event {
	
	public final TileEntity unpackingEntity;
	
	public UnpackStartEvent(TileEntity unpacker) {
		this.unpackingEntity=unpacker;
	}

}
