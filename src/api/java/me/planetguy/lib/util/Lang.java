package me.planetguy.lib.util;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import me.planetguy.lib.prefab.IPrefabItem;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Lang {
	
	public static String translate(String s){
		return LanguageRegistry.instance().getStringLocalization(s);
	}
	
}
