package me.planetguy.remaininmotion.plugins.fmp;

import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.plugins.RemIMPluginsCommon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FMPCarriagePlugin {

	public static Item		hollowCarriage;

	public static void tryLoad() {
			Debug.dbg("FMP carriage: loading");
			hollowCarriage = new ItemCarriageFMP();

			hollowCarriage.setUnlocalizedName("hollowCarriage");
			GameRegistry.registerItem(hollowCarriage, "Hollow Carriage");
			
			FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(RemIMPluginsCommon.getFrameBlock()));
		// Attempting to fix FMP crashing when trying to set creative tab
	}

	public static void init() {
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(RemIMPluginsCommon.getFrameBlock(), 0),
				"tile.hollowCarriage.open");
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(RemIMPluginsCommon.getFrameBlock(), 1),
				"tile.hollowCarriage.closed");
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(RemIMPluginsCommon.getFrameBlock(), 2),
				"tile.hollowCarriage.corners");

		MultiPartRegistry.registerParts(new IPartFactory() {
			@Override
			public TMultiPart createPart(String arg0, boolean arg1) {
				if (arg0.equals("FMPCarriage")) { return new PartCarriageFMP(); }
				return null;
			}

		}, new String[] { "FMPCarriage" });
		
		RiMRegistry.registerFrameCarriageMatcher(new FMPCarriageMatcher());

		RiMRegistry.registerCloseableFactory(new FMPCloseableRetriever());

	}

	public static void postInit() {
		hollowCarriage.setCreativeTab(CreativeTab.Instance);
		GameRegistry.addRecipe(new ItemStack(FMPCarriagePlugin.hollowCarriage, 8), "ccc", "c c", "ccc",
				Character.valueOf('c'), new ItemStack(RemIMPluginsCommon.getFrameBlock(), 1, 0));

		GameRegistry.addShapelessRecipe(new ItemStack(RemIMPluginsCommon.getFrameBlock()), new ItemStack(
				FMPCarriagePlugin.hollowCarriage));
	}

}
