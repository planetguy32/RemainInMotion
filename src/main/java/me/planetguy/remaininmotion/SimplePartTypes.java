package me.planetguy.remaininmotion;

import net.minecraft.item.ItemStack;

public enum SimplePartTypes
{
	CarriageCrosspiece ,
	CarriagePanel ,
	CarriageFramework ;

	public net . minecraft . util . IIcon Icon ;

	public net . minecraft . item . ItemStack Stack ( )
	{
		return new ItemStack( Items . SimpleItemSet , this.ordinal()) ;
	}
}