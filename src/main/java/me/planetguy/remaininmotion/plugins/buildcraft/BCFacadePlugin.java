package me.planetguy.remaininmotion.plugins.buildcraft;

import net.minecraft.block.Block;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.core.RIMBlocks;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

//@Mod(modid="remainInMotion-BCFacadePlugin", dependencies="required-after:JAKJ_RedstoneInMotion")
public class BCFacadePlugin {
	
	@EventHandler
	public void preInit(FMLInitializationEvent event) {
		if(Loader.isModLoaded("BuildCraft")) {
			Debug.dbg("Buildcraft special facade: loading");
			
			RiMRegistry.registerFrameCarriageMatcher(new SpecialFacadeCarriageMatcher());
			
			RiMRegistry.registerCloseableFactory(new SpecialFacadeCloseableFactory());
			
			FMLInterModComms.sendMessage("BuildCraft|Transport", "add-facade", Block.blockRegistry.getNameForObject(RIMBlocks.Carriage)+"@0");
			
		}else {
			Debug.dbg("Buildcraft special facade: not loading");
		}
	}
	
}
