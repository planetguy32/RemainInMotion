package me.planetguy.remaininmotion.plugins;

import cpw.mods.fml.common.Loader;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.interop.ModInteraction;
import me.planetguy.remaininmotion.plugins.buildcraft.BCFacadePlugin;
import me.planetguy.remaininmotion.plugins.fmp.FMPCarriagePlugin;
import net.minecraft.block.Block;

//@Mod(modid = me.planetguy.remaininmotion.core.ModRiM.Handle + "_HollowCarriages", dependencies = "required-after:JAKJ_RedstoneInMotion")
public class RemIMPluginsCommon {

    private static Block	frameBlock;

    public static RemIMPluginsCommon instance=new RemIMPluginsCommon();

    public static Block getFrameBlock() {
        if (frameBlock == null) {
            frameBlock=RIMBlocks.plainFrame;
        }
        return frameBlock;
    }

    public void preInit() {
        try {
            if(Loader.isModLoaded("ForgeMultipart")) FMPCarriagePlugin.tryLoad();
        }catch(NoClassDefFoundError e) {
        	e.printStackTrace();
        }
        try {
            if(Loader.isModLoaded("BuildCraft|Transport")) BCFacadePlugin.tryLoad();
        }catch(NoClassDefFoundError noClassDefFoundError) {
        	
        }
    }

    public void init() {
        if(Loader.isModLoaded("ForgeMultipart")) FMPCarriagePlugin.init();
    }

	public static void postInit() {
		if(Loader.isModLoaded("ForgeMultipart")) FMPCarriagePlugin.postInit();
	}

}
