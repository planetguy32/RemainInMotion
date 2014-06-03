package me.planetguy.remaininmotion ;


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
