package me.planetguy.lib.prefab;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPrefabItem {
	
	void loadCrafting();

	Object setCreativeTab(CreativeTabs tab);
	
	String getName();
	
}
