package me.planetguy.lib.util;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class Lang {

	public static String translate(String s) {
		return LanguageRegistry.instance().getStringLocalization(s);
	}

}
