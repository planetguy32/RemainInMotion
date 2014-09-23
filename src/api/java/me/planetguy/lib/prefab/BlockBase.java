package me.planetguy.lib.prefab;

import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BlockBase extends Block implements IPrefabBlock{

	public static String modIDCache;
	
	public static void prepare(String modID){
		modIDCache=modID;
	}
	
	public static IPrefabItem load(Class<? extends BlockBase> clazz, HashMap<String, IPrefabItem> map){
		BlockBase block;
		try {
			block = clazz.newInstance();
			block.modID=modIDCache;
			GameRegistry.registerBlock(block, ItemBlockBase.class, block.getName());
			map.put(block.getName(), block);
			return block;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private final String name;

	private String modID;
	
	protected BlockBase(Material p_i45394_1_, String name) {
		super(p_i45394_1_);
		this.name=name;
		this.setBlockName(name);
	}
	
	public void registerBlockIcons(IIconRegister ir){
		this.blockIcon=ir.registerIcon(modID+":"+getName());
	}

	public int countTooltipLines() {
		return 2;
	}
	
	public void loadCrafting() {}

	public String getName() {
		return name;
	}
	
}
