package me.planetguy.remaininmotion;

import net.minecraft.util.IIcon;
import me.planetguy.remaininmotion.core.Items;

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