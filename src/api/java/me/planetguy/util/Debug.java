package me.planetguy.util ;

import java.util.Arrays;

public abstract class Debug
{
	public static void dbt(Object o){
		StackTraceElement[] trace=Thread.currentThread().getStackTrace();
		dbg(Arrays.toString(trace)+" >>> "+o);
	}
	
	public static void dbg(Object o){
		StackTraceElement[] trace=Thread.currentThread().getStackTrace();
		System.out.println(trace[2].getClassName().replaceAll("[a-zA-Z]*\\.", "")+"@"+trace[2].getLineNumber()+"   "+o);
		//if(Configuration.Debug.verbose)	log.debug(m,o);
	}
	
	public static void exception(Throwable t){
		dbg(t.toString());
	}
	
}
