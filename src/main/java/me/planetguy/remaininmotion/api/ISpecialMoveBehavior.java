package me.planetguy.remaininmotion.api;

import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Allows providing a custom NBT tag
 * 
 * Deprecated due to its dependency on internal class CarriagePackage. Use the event-based
 * API instead.
 * 
 * @author planetguy
 * 
 */
@Deprecated
public interface ISpecialMoveBehavior {

	public void onAdded(CarriagePackage pkg, NBTTagCompound tag) throws CarriageMotionException;

}
