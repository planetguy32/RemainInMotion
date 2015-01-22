package me.planetguy.remaininmotion.plugins.buildcraft;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.RiMRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="remainInMotion-BCFacadePlugin", dependencies="required-after:JAKJ_RedstoneInMotion;after:BuildCraft|Transport")
public class BCFacadePlugin {
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if(Loader.isModLoaded("BuildCraft")) {
			Debug.dbg("Buildcraft special facade: loading");
			
			RiMRegistry.registerMatcher(new SpecialFacadeCarriageMatcher());
			
		}else {
			Debug.dbg("Buildcraft special facade: not loading");
		}
	}
	
}
