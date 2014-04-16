package me.planetguy.remaininmotion;

public enum Types
{
	CarriageCrosspiece ,
	CarriagePanel ,
	CarriageFramework ;

	public net . minecraft . util . Icon Icon ;

	public net . minecraft . item . ItemStack Stack ( )
	{
		return ( Stack . New ( Items . SimpleItemSet , this ) ) ;
	}
}