package me.planetguy.remaininmotion.core.interop;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

//if no FMP installed, use this to avoid nulls or errors
public class FMPHandlerDummy implements FMPHandler{

	@Override
	public boolean isMultipart(TileEntity part) {
		return false;
	}

	@Override
	public void saveMultipartTick(TileEntity te, NBTTagCompound record) {
	}

	@Override
	public void loadMultipartTick(TileEntity part, NBTTagCompound te) {
	}
	

}