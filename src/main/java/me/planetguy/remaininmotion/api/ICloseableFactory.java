package me.planetguy.remaininmotion.api;

import java.util.List;

import net.minecraft.tileentity.TileEntity;

public interface ICloseableFactory {
	
	public ICloseable retrieve(TileEntity te);
	
	public List<Class<? extends TileEntity>> validClasses();

}
