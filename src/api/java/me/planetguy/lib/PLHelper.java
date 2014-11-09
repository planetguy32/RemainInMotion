package me.planetguy.lib;

import java.util.HashMap;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import me.planetguy.lib.prefab.BlockBase;
import me.planetguy.lib.prefab.BlockContainerBase;
import me.planetguy.lib.prefab.IPrefabItem;
import me.planetguy.lib.prefab.ItemBase;
import me.planetguy.lib.prefab.ItemBlockBase;
import me.planetguy.lib.util.Debug;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;

public class PLHelper {
	
	private final String modID;
	
	public PLHelper(String modID){
		this.modID=modID;
	}
	
	public void playSound(World w, double x, double y, double z, String name, float volume, float pitch){
		for(Object o:w.worldAccesses){
			IWorldAccess iwa=(IWorldAccess) o;
			iwa.playSound(modID+":"+name, x, y, z, volume, pitch);
		}
	}
	
	public IPrefabItem loadBlock(Class<? extends BlockBase> clazz, HashMap<String, IPrefabItem> map){
		BlockBase block;
		try {
			block = clazz.newInstance();
			block.modID=modID;
			GameRegistry.registerBlock(block, ItemBlockBase.class, block.getName());
			map.put(block.getName(), block);
			return block;
		} catch (InstantiationException e) { //Debug: Throw these exceptions
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public IPrefabItem loadItem(Class<? extends ItemBase> clazz, HashMap<String, IPrefabItem> map){
		ItemBase item;
		try {
			item = clazz.newInstance();
			item.modID=modID;
			GameRegistry.registerItem(item, item.name);
			map.put(item.name, item);
			return item;
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public IPrefabItem loadContainer(Class<? extends BlockContainerBase> clazz, HashMap<String, IPrefabItem> map){
		BlockContainerBase block;
		try {
			block = clazz.newInstance();
			block.modid=modID;
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
	
	public void load(Class c, HashMap<String, IPrefabItem> content){
		if(ItemBase.class.isAssignableFrom(c)){
			loadItem(c, content);
		}else if(BlockBase.class.isAssignableFrom(c)){
			loadBlock(c, content);
		}else if(BlockContainerBase.class.isAssignableFrom(c)){
			loadContainer(c, content);
		}else{
			throw new RuntimeException("Failed to load "+c+": Not a legal class type!");
		}
	}
	
	public String translate(String nonNamespacedKey){
		return LanguageRegistry.instance().getStringLocalization(modID+"."+nonNamespacedKey);
	}

}
