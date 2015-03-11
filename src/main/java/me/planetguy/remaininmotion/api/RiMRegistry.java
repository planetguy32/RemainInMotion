package me.planetguy.remaininmotion.api;

import cpw.mods.fml.common.eventhandler.EventBus;
import me.planetguy.remaininmotion.CarriageMatchers;
import me.planetguy.remaininmotion.Closeables;
import me.planetguy.remaininmotion.FrameCarriageMatchers;

public class RiMRegistry {
	
	public static final EventBus blockMoveBus=new EventBus();

	/**
	 * Register carriage matchers with RiM. To register something as a frame
	 * carriage, use FrameCarriageMatcher instead - otherwise other registered
	 * frame carriages will not be interoperable with this.
	 * 
	 * @param m
	 *            the matcher to register
	 */

	public static void registerMatcher(CarriageMatcher m) {
		CarriageMatchers.register(m);
	}

	/**
	 * Registers a way to get Closeables from a block. Closeables specify which
	 * sides to treat as closed.
	 * 
	 * @param factory
	 */
	public static void registerCloseableFactory(ICloseableFactory factory) {
		Closeables.register(factory);
	}

	/**
	 * Registers a type of carriage as a frame carriage.
	 * 
	 * @param m
	 */
	public static void registerFrameCarriageMatcher(FrameCarriageMatcher m) {
		FrameCarriageMatchers.register(m);
	}
	
	/**
	 * Registers a BlockMoveEvent handler with RemIM's event bus
	 */
	public static void registerEventHandler(Object o) {
		RiMRegistry.blockMoveBus.register(o);
	}

}
