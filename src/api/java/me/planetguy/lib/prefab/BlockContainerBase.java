package me.planetguy.lib.prefab;

import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockContainerBase extends BlockContainer implements IPrefabBlock{

	public static IPrefabItem load(Class<? extends BlockContainerBase> clazz, HashMap<String, IPrefabItem> map){
		BlockContainerBase block;
		try {
			block = clazz.newInstance();
			for(Class<? extends TileEntity> c:block.teClasses){
				GameRegistry.registerTileEntity(c, block.modid+".te."+c.getSimpleName());
			}
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
	
	private final String modid;
	
	private Class[] teClasses;
	
	public BlockContainerBase(Material p_i45394_1_, String name, Class<? extends TileEntity>... tileEntities) {
		super(p_i45394_1_);
		this.name=name;
		this.setBlockName(name);
		this.teClasses=tileEntities;
		this.modid=BlockBase.modIDCache;
	}
	
	public void registerBlockIcons(IIconRegister ir){
		this.blockIcon=ir.registerIcon(modid+":"+getName());
	}

	public int countTooltipLines() {
		return 2;
	}
	
	public void loadCrafting() {}

	public String getName() {
		return name;
	}

	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		try {
			return (TileEntity) teClasses[meta].newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
