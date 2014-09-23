package me.planetguy.lib;

import net.minecraftforge.common.config.Configuration;
import me.planetguy.lib.util.LibProperties;
import me.planetguy.lib.util.Reflection;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=LibProperties.modID, version="1.0")
public class PlanetguyLib {
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent pie){
		Reflection.init();
	}
	
	

}
