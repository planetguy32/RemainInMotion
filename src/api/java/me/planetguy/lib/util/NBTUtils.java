package me.planetguy.lib.util;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils {
	
	static Field NBTTagCompound_map;
	
	static {
		init();
	}
	
	
	public static void init() {
		Field f=null;
		try {
			for(Field maybeMap:NBTTagCompound.class.getDeclaredFields()) {
				if(Map.class.isAssignableFrom(maybeMap.getType())) {
					f=maybeMap;
					f.setAccessible(true);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		NBTTagCompound_map=f;
	}
	

	public static void copySubTags(NBTTagCompound from, NBTTagCompound to) {
		try {
			init();
			Map<String, NBTBase> map= (Map<String, NBTBase>) NBTTagCompound_map.get(from);
			for(String s:map.keySet()) {
				to.setTag(s, map.get(s));
			}
		}catch(Exception e) {
			
		}
	}

}
