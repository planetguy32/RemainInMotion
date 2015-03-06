package me.planetguy.lib;

import java.io.File;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import me.planetguy.lib.safedraw.SafeDraw;
import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.LibProperties;
import me.planetguy.lib.util.Reflection;
import me.planetguy.lib.util.impl.CommandEditBlacklist;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = LibProperties.modID, version = "1.5")
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
		configFolder = new File(pie.getModConfigurationDirectory().getAbsoluteFile()+File.separator+"planetguyLib");
		configFolder.mkdir();
		Configuration cfg=new Configuration(pie.getSuggestedConfigurationFile());
		cfg.load();
		doPLLogging=cfg.getBoolean("muteLogging", Configuration.CATEGORY_GENERAL, doPLLogging, "Turn on or off all logging through PlanetguyLib. Set this to true if asked by a modder.");
		cfg.save();
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent evt) {
		Debug.mark();
		evt.registerServerCommand(new CommandEditBlacklist());
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			SafeDraw.init();
		}
	}
	
}
