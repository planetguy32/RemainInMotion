package me.planetguy.lib.util;

import me.planetguy.lib.PlanetguyLib;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Lang {

	public static String translate(String s) {
		String res= LanguageRegistry.instance().getStringLocalization(s);
		if(res.equals("") && PlanetguyLib.doPLLogging){ //this is a debug feature
			throw new RuntimeException("Failed to translate "+s);
		}else{
			return res;
		}
	}

}
