package me.planetguy.remaininmotion ;

public abstract class Debug
{
	public static String Label = "*-*-* " + Mod . Title . toUpperCase ( ) + " *-*-*" ;

	public static void Emit ( Object ... Objects )
	{
		System . err . print ( Label + " -- " + cpw . mods . fml . common . FMLCommonHandler . instance ( ) . getEffectiveSide ( ) ) ;

		for ( Object Object : Objects )
		{
			try
			{
				System . err . print ( " -- " + Object ) ;
			}
			catch ( Throwable Throwable )
			{
				System . err . print ( " -- ERROR" ) ;
			}
		}

		System . err . println ( ) ;
	}

	public static void EmitTrace ( )
	{
		new RuntimeException ( Label ) . printStackTrace ( ) ;
	}
}
