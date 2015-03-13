package me.planetguy.remaininmotion.core;

import me.planetguy.remaininmotion.util.Stack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public enum ItemTypes {
	CarriageCrosspiece, CarriagePanel, CarriageFramework;

	public IIcon	Icon;

	public ItemStack Stack() {
		return (Stack.New(RiMItems.SimpleItemSet, this));
	}
}