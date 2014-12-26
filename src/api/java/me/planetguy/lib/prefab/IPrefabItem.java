package me.planetguy.lib.prefab;

import net.minecraft.creativetab.CreativeTabs;

public interface IPrefabItem {

	void loadCrafting();

	Object setCreativeTab(CreativeTabs tab);

	String getName();

}
