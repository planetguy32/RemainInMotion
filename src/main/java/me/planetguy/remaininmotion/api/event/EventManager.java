package me.planetguy.remaininmotion.api.event;

import me.planetguy.remaininmotion.api.RiMRegistry;
import cpw.mods.fml.common.eventhandler.EventBus;

public class EventManager {
	
	public static final EventBus blockMoveBus=new EventBus();
	
	public static void registerEventHandler(Object o) {
		blockMoveBus.register(o);
	}

}
