package me.planetguy.remaininmotion.api.event;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IBlockPos {
	
	public World world();
	public int x();
	public int y(); 
	public int z();
	
	//May return null
	public TileEntity entity();
	public NBTTagCompound entityTag();

}
