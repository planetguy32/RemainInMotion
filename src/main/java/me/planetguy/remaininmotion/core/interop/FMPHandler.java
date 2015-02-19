package me.planetguy.remaininmotion.core.interop;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

//Wraps all FMP functions with no FMP references
public interface FMPHandler {
	
	public boolean isMultipart(TileEntity part);
	
	public void saveMultipartTick(TileEntity te, NBTTagCompound record);
	
	public void loadMultipartTick(TileEntity part, NBTTagCompound te);

}