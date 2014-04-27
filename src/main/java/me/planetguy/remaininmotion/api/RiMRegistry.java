package me.planetguy.remaininmotion.api;

import me.planetguy.remaininmotion.CarriageMatchers;

public class RiMRegistry {
	
	/**
	 * Register carriage matchers with RiM.
	 * @param m the matcher to register
	 */
	
	public static void registerMatcher(CarriageMatcher m){
		CarriageMatchers.register(m);
	}
	
	

}
