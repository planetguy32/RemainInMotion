package me.planetguy.remaininmotion.fmp;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMItems;
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

@Mod(modid = me.planetguy.remaininmotion.core.Mod.Handle + "_HollowCarriages", dependencies = "after:JAKJ_RedstoneInMotion;after:ForgeMultipart")
public class ModHollowCarriages {

	boolean				alive;
	public static Item	hollowCarriage;

	Block				baseBlock;

	@Optional.Method(modid = "ForgeMultipart")
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		alive = Loader.isModLoaded("ForgeMultipart");
		Debug.dbg("FMP carriage: " + (alive ? "loading" : "not loading"));
		if (!alive) { return; }
		hollowCarriage = new ItemCarriageFMP();

		hollowCarriage.setUnlocalizedName("hollowCarriage");
		GameRegistry.registerItem(hollowCarriage, "Hollow Carriage");

		// Attempting to fix FMP crashing when trying to set creative tab

		MultiPartRegistry.registerParts(new IPartFactory() {

			@Override
			public TMultiPart createPart(String arg0, boolean arg1) {
				if (arg0.equals("FMPCarriage")) { return new BlockCarriageFMP(); }
				return null;
			}

		}, new String[] { "FMPCarriage" });

		RiMRegistry.registerMatcher(new FMPCarriageMatcher());

		RiMRegistry.registerCloseableFactory(new FMPCloseableRetriever());

		baseBlock = new Block(Material.wood) {

			IIcon[]	icons;

			@Override
			public void registerBlockIcons(IIconRegister ir) {
				icons = new IIcon[] {
						ir.registerIcon(me.planetguy.remaininmotion.core.Mod.Handle + ":" + Registry.TexturePrefix
								+ "FMPCarriage_Open"),
								ir.registerIcon(me.planetguy.remaininmotion.core.Mod.Handle + ":" + Registry.TexturePrefix
										+ "FMPCarriage_Closed"),
										ir.registerIcon(me.planetguy.remaininmotion.core.Mod.Handle + ":" + Registry.TexturePrefix
												+ "FMPCarriage_Corners"), };
			}

			@Override
			public IIcon getIcon(int side, int meta) {
				return icons[meta];
			}

		};

		GameRegistry.registerBlock(baseBlock, "tile.hollowCarriage");
	}

	@Optional.Method(modid = "ForgeMultipart")
	@EventHandler
	public void init(FMLInitializationEvent ev) {
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(baseBlock, 0), "tile.hollowCarriage.open");
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(baseBlock, 1), "tile.hollowCarriage.closed");
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(baseBlock, 2), "tile.hollowCarriage.corners");
	}

	@Optional.Method(modid = "ForgeMultipart")
	@EventHandler
	public void postInit(FMLPostInitializationEvent ev) {
		if (!alive) { return; }
		hollowCarriage.setCreativeTab(CreativeTab.Instance);
		GameRegistry.addRecipe(new ItemStack(ModHollowCarriages.hollowCarriage, 8), "ccc", "c c", "ccc",
				Character.valueOf('c'), new ItemStack(RIMBlocks.Carriage, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(RiMItems.SimpleItemSet), new ItemStack(
				ModHollowCarriages.hollowCarriage));
	}

}
