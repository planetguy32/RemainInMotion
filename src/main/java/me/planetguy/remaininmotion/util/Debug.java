package me.planetguy.remaininmotion.util ;

import java.util.Arrays;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import cpw.mods.fml.common.FMLLog;

import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.Mod;

public abstract class Debug
{
	private static Logger log=FMLLog.getLogger();
	private static Marker m=MarkerManager.getMarker("RemIM");
	
	public static String Label = "*-*-* " + Mod . Title . toUpperCase ( ) + " *-*-*" ;

	public static void dbt(Object o){
		StackTraceElement[] trace=Thread.currentThread().getStackTrace();
		dbg(Arrays.toString(trace)+" >>> "+o);
	}
	
	public static void dbg(Object o){
		StackTraceElement[] trace=Thread.currentThread().getStackTrace();
		System.out.println(trace[2]+"   "+o);
		//if(Configuration.Debug.verbose)	log.debug(m,o);
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
