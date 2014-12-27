package me.planetguy.remaininmotion.api;

import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Allows providing a custom NBT tag
 * 
 * @author planetguy
 * 
 */

public interface ISpecialMoveBehavior {

	public void onAdded(CarriagePackage pkg, NBTTagCompound tag) throws CarriageMotionException;

}
