package me.planetguy.remaininmotion.plugins.fmp;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMItems;
import me.planetguy.remaininmotion.plugins.RemIMPluginsCommon;
import me.planetguy.remaininmotion.plugins.buildcraft.BCFacadePlugin;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class FMPCarriagePlugin {

	public static Item	hollowCarriage;

	private static boolean load;
	
	public static void tryLoad() {
		load=Loader.isModLoaded("ForgeMultipart");
		if(load) {
			Debug.dbg("FMP carriage: loading");			
			hollowCarriage = new ItemCarriageFMP();

			hollowCarriage.setUnlocalizedName("hollowCarriage");
			GameRegistry.registerItem(hollowCarriage, "Hollow Carriage");
		}else {
			Debug.dbg("FMP carriage: not loading");	
		}
		// Attempting to fix FMP crashing when trying to set creative tab
	}

	public static void init() {
		if(!load)return;
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(RemIMPluginsCommon.getFrameBlock(), 0), "tile.hollowCarriage.open");
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(RemIMPluginsCommon.getFrameBlock(), 1), "tile.hollowCarriage.closed");
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(RemIMPluginsCommon.getFrameBlock(), 2), "tile.hollowCarriage.corners");
		
		MultiPartRegistry.registerParts(new IPartFactory() {
			@Override
			public TMultiPart createPart(String arg0, boolean arg1) {
				if (arg0.equals("FMPCarriage")) { return new BlockCarriageFMP(); }
				return null;
			}

		}, new String[] { "FMPCarriage" });

		RiMRegistry.registerFrameCarriageMatcher(new FMPCarriageMatcher());

		RiMRegistry.registerCloseableFactory(new FMPCloseableRetriever());

	}

	public static void postInit() {
		if(!load)return;
		hollowCarriage.setCreativeTab(CreativeTab.Instance);
		GameRegistry.addRecipe(new ItemStack(FMPCarriagePlugin.hollowCarriage, 8), 
				"ccc", 
				"c c", 
				"ccc",
				Character.valueOf('c'), new ItemStack(RemIMPluginsCommon.getFrameBlock(), 1, 0));
		
		GameRegistry.addShapelessRecipe(new ItemStack(RemIMPluginsCommon.getFrameBlock()), 
				new ItemStack(FMPCarriagePlugin.hollowCarriage));
	}

}
