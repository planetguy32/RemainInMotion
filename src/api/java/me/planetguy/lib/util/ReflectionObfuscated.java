package me.planetguy.lib.util;

import java.util.HashMap;

public class ReflectionObfuscated extends Reflection{

	public ReflectionObfuscated(){
		Debug.dbg("Obfuscated Minecraft detected!");
		
		map("getOrCreateChunkWatcher", "func_72690_a");
		map("playersWatchingChunk","field_73263_b");
		map("pendingTickListEntriesTreeSet","field_73065_O");
		map("pendingTickListEntriesHashSet","field_73064_N");
		map("isDrawing","field_78415_z");
		map("storageArrays","field_76652_q");
		map("field_147481_N","field_147481_N");
		map("addedTileEntityList", "field_147484_a");
	}
	
	public HashMap<String, String> obfRemapper=new HashMap<String, String>();
	
	public String remap(String s){
		if(obfRemapper.containsKey(s))
			return obfRemapper.get(s);
		else
			return s;
	}
	
	public void map(String dev, String obf){
		obfRemapper.put(dev, obf);
	}

}
