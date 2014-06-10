package me.planetguy.remaininmotion.core ;

import me.planetguy.remaininmotion.SimpleItemSet;
import me.planetguy.remaininmotion.ToolItemSet;



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
