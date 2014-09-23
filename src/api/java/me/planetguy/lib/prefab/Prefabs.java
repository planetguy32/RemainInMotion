package me.planetguy.lib.prefab;

public class Prefabs {
	
	public static void initialize(String modID){
		ItemBase.prepare(modID);
		BlockBase.prepare(modID);
	}

}
