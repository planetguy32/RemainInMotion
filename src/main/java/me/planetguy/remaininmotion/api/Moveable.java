package me.planetguy.remaininmotion.api;

import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;

/**
 * 
 */
public interface Moveable {

	public abstract void fillPackage ( CarriagePackage _package ) throws CarriageMotionException;
	
}
