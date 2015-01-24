package me.planetguy.remaininmotion.plugins;

import cpw.mods.fml.common.registry.GameRegistry;
import me.planetguy.lib.prefab.BlockBase;
import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class RemIMPluginsCommon {
	
	private static Block frameBlock;
	
	public static Block getFrameBlock() {
		if(frameBlock==null) {
			frameBlock=new BlockBase(Material.wood, "specialFrame") {

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
			GameRegistry.registerBlock(frameBlock, "tile.hollowCarriage");
			
			GameRegistry.addSmelting(new ItemStack(RIMBlocks.Carriage, 0), new ItemStack(frameBlock, 0), 0);
			//GameRegistry.addSmelting(new ItemStack(frameBlock, 0), new ItemStack(frameBlock, 1), 0);
			GameRegistry.addSmelting(new ItemStack(frameBlock, 0), new ItemStack(RIMBlocks.Carriage), 0);
		}
		return frameBlock;
	}

}
