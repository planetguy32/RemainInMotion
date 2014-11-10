package me.planetguy.lib;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import me.planetguy.lib.util.LibProperties;
import me.planetguy.lib.util.Reflection;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=LibProperties.modID, version="1.2")
public class PlanetguyLib {
	
	@Instance(LibProperties.modID)
	public static PlanetguyLib instance;
	
	public File configFolder;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent pie){
		Reflection.init();
		configFolder=pie.getSuggestedConfigurationFile();
	}
	
	

}
