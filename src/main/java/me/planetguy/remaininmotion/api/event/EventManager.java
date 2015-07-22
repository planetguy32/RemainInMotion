package me.planetguy.remaininmotion.api.event;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.RiMRegistry;
import cpw.mods.fml.common.eventhandler.EventBus;

public class EventManager {
	
	public static final EventBus blockMoveBus=new EventBus();
	
	public static void registerEventHandler(Object o) {
		try {
			blockMoveBus.register(o);
		}catch(Error e) {
			Debug.dbg("Failed to register plugin for "+o);
		}
	}

}
