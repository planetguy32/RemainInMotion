package me.planetguy.remaininmotion.base;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public interface ICamouflageable {

	public abstract IIcon getIconCamouflaged(IBlockAccess world, int x, int y,
			int z, int side);

}