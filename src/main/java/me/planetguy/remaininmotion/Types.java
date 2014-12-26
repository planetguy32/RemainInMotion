package me.planetguy.remaininmotion;

import me.planetguy.remaininmotion.core.RiMItems;
import net.minecraft.util.IIcon;

public enum Types {
	CarriageCrosspiece, CarriagePanel, CarriageFramework;

	public IIcon	Icon;

	public net.minecraft.item.ItemStack Stack() {
		return (Stack.New(RiMItems.SimpleItemSet, this));
	}
}