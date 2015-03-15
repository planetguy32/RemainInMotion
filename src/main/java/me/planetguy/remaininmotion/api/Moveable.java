package me.planetguy.remaininmotion.api;

import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;

/**
 * 
 */
public interface Moveable {

	public abstract void fillPackage(CarriagePackage _package) throws CarriageMotionException;

}
