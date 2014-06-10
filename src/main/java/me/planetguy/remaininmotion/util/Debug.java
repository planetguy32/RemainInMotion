package me.planetguy.remaininmotion.util ;

import java.util.Arrays;

import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.Mod;

public abstract class Debug
{
	public static String Label = "*-*-* " + Mod . Title . toUpperCase ( ) + " *-*-*" ;

	public static void dbt(Object o){
		StackTraceElement[] trace=Thread.currentThread().getStackTrace();
		dbg(Arrays.toString(trace)+" >>> "+o);
	}
	
	public static void dbg(Object o){
		if(Configuration.Debug.verbose)
			System.out.println(o);
	}
	
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
