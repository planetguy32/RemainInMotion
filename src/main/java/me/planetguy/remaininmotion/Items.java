package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.core.Mod;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class Items
{
	public static ToolItemSet ToolItemSet ;

	public static SimpleItemSet SimpleItemSet ;

	public static void Initialize ( )
	{
		ToolItemSet = new ToolItemSet ( ) ;
		
		SimpleItemSet = new SimpleItemSet ( ) ;
		
	}
}
