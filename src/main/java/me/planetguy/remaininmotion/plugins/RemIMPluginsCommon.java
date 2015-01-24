package me.planetguy.remaininmotion.plugins;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import me.planetguy.lib.prefab.BlockBase;
import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.plugins.buildcraft.BCFacadePlugin;
import me.planetguy.remaininmotion.plugins.fmp.FMPCarriagePlugin;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

@Mod(modid = me.planetguy.remaininmotion.core.ModRiM.Handle + "_HollowCarriages", dependencies = "required-after:JAKJ_RedstoneInMotion")
public class RemIMPluginsCommon {
	
	private static Block frameBlock;
	
	public static Block getFrameBlock() {
		if(frameBlock==null) {
			frameBlock=new Block(Material.wood) {

				IIcon[]	icons;

				@Override
				public void registerBlockIcons(IIconRegister ir) {
					icons = new IIcon[] {
							ir.registerIcon(me.planetguy.remaininmotion.core.ModRiM.Handle + ":" + Registry.TexturePrefix
									+ "FMPCarriage_Open"),
									ir.registerIcon(me.planetguy.remaininmotion.core.ModRiM.Handle + ":" + Registry.TexturePrefix
											+ "FMPCarriage_Closed"),
											ir.registerIcon(me.planetguy.remaininmotion.core.ModRiM.Handle + ":" + Registry.TexturePrefix
													+ "FMPCarriage_Corners"), };
				}

				@Override
				public IIcon getIcon(int side, int meta) {
					return icons[meta];
				}

			};
			
			GameRegistry.registerBlock(frameBlock, ItemBlock.class, "tile.hollowCarriage", ModRiM.Handle);
			
			frameBlock.setBlockName("hollowCarriage");
			
			GameRegistry.addSmelting(new ItemStack(RIMBlocks.Carriage, 0), new ItemStack(frameBlock, 0), 0);
			//GameRegistry.addSmelting(new ItemStack(frameBlock, 0), new ItemStack(frameBlock, 1), 0);
			GameRegistry.addSmelting(new ItemStack(frameBlock, 0), new ItemStack(RIMBlocks.Carriage), 0);
		}
		return frameBlock;
	}
	
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		FMPCarriagePlugin.tryLoad();

		BCFacadePlugin.tryLoad();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent ev) {
		FMPCarriagePlugin.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		FMPCarriagePlugin.postInit();
	}

}
