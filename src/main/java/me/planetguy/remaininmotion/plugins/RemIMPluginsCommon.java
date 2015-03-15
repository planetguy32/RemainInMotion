package me.planetguy.remaininmotion.plugins;

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
            if(ModInteraction.MPInstalled) FMPCarriagePlugin.tryLoad();
        }catch(NoClassDefFoundError noClassDefFoundError) {}
        try {
            if(ModInteraction.BCInstalled) BCFacadePlugin.tryLoad();
        }catch(NoClassDefFoundError noClassDefFoundError) {
        	
        }
    }

    public void init() {
        if(ModInteraction.MPInstalled) FMPCarriagePlugin.init();
    }

    public void postInit() {
        if(ModInteraction.MPInstalled) FMPCarriagePlugin.postInit();
    }

}
