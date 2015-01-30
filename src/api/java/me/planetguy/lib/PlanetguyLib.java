package me.planetguy.lib;

import java.io.File;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import me.planetguy.lib.util.LibProperties;
import me.planetguy.lib.util.Reflection;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LibProperties.modID, version = "1.4")
public class PlanetguyLib {

	@Instance(LibProperties.modID)
	public static PlanetguyLib	instance;

	public File					configFolder;
	
	public static boolean doPLLogging=false;

	public PlanetguyLib() {
		instance = this;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent pie) {
		Reflection.init();
		configFolder = new File(pie.getModConfigurationDirectory().getAbsoluteFile()+File.separator+"plItemRestrict");
		configFolder.mkdir();
		Configuration cfg=new Configuration(pie.getSuggestedConfigurationFile());
		
		cfg.getBoolean("muteLogging", Configuration.CATEGORY_GENERAL, doPLLogging, "Suppresses all log output. Turn off if you get lots of lines starting with \"[PL]\" and server lag.");
	}

}
