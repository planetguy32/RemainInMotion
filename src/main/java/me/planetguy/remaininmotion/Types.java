package me.planetguy.remaininmotion;

import me.planetguy.remaininmotion.core.RiMItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public enum Types {
	CarriageCrosspiece, CarriagePanel, CarriageFramework;

	public IIcon	Icon;

	public ItemStack Stack() {
		return (Stack.New(RiMItems.SimpleItemSet, this));
	}
}