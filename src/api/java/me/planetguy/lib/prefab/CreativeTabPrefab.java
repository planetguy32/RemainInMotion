package me.planetguy.lib.prefab;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabPrefab extends CreativeTabs {

	ItemStack item;
	
	public CreativeTabPrefab(String name, ItemStack obj) {
		super(name);
		this.item=obj;
	}

	@Override
	public ItemStack getIconItemStack() {
		return item;
	}

	@Override
	public Item getTabIconItem() {
		return null;
	}

}
