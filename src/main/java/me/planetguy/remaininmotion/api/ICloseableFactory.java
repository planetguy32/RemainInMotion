package me.planetguy.remaininmotion.api;

import net.minecraft.tileentity.TileEntity;

public interface ICloseableFactory {

	public ICloseable retrieve(TileEntity te);

	public Class<?> validClass();

}
