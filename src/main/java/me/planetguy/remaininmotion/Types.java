package me.planetguy.remaininmotion;

import me.planetguy.remaininmotion.core.Items;
import net.minecraft.util.IIcon;

public enum Types
{
	CarriageCrosspiece ,
	CarriagePanel ,
	CarriageFramework ;

	public IIcon Icon ;

	public net . minecraft . item . ItemStack Stack ( )
	{
		return ( Stack . New ( Items . SimpleItemSet , this ) ) ;
	}
}