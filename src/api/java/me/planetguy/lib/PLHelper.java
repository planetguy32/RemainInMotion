package me.planetguy.lib;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import me.planetguy.lib.prefab.BlockBase;
import me.planetguy.lib.prefab.BlockContainerBase;
import me.planetguy.lib.prefab.IPrefabItem;
import me.planetguy.lib.prefab.ItemBase;
import me.planetguy.lib.prefab.ItemBlockBase;
import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.Reflection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class PLHelper {
	
	private final String modID;
	
	private Configuration cfg=null;
	
	public PLHelper(String modID){
		this.modID=modID;
		File f=new File(PlanetguyLib.instance.configFolder, this.modID+".cfg");
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Debug.dbg("PL config file for mod "+modID+": "+f.exists());
		cfg=new Configuration(f);
	}
	
	public PLHelper(String modID, Configuration banConfig){
		this(modID);
		//cfg=banConfig;
	}
	
	public void playSound(World w, double x, double y, double z, String name, float volume, float pitch){
		playUnNamespacedSound(w,x,y,z,modID+":"+name, volume,pitch);
	}
	
	public void playUnNamespacedSound(World w, double x, double y, double z, String name, float volume, float pitch){
		for(Object o:(Iterable) Reflection.get(World.class, w, "worldAccesses")){
			IWorldAccess iwa=(IWorldAccess) o;
			iwa.playSound(name, x, y, z, volume, pitch);
		}
	}
	
	public IPrefabItem loadBlock(Class<? extends BlockBase> clazz, HashMap<String, IPrefabItem> map){
		if(!shouldLoad(clazz))
			return null;
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
		if(!shouldLoad(clazz))
			return null;
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
		if(!shouldLoad(clazz))
			return null;
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
		Debug.mark();
		if(!shouldLoad(c))
			return;
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
	
	public boolean shouldLoad(Class c){
		cfg.load();
		boolean b=cfg.getBoolean("Allow "+c.getSimpleName(),"itemRestrict", true, "");
		Debug.dbg(b+" : "+c);
		cfg.save();
		return b;
	}

}
