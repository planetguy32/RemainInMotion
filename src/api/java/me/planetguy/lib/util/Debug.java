package me.planetguy.lib.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import me.planetguy.lib.PlanetguyLib;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public abstract class Debug {

	static final Logger	log	= LogManager.getLogger("PL");

	public static void print(String s) {
		if(PlanetguyLib.doPLLogging)
			log.info(s);
	}

	public static void dbt(Object o) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		dbg(Arrays.toString(trace) + " >>> " + o);
	}

	public static void dbg_delegate(Object o) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		print(trace[3].getClassName().replaceAll("[a-zA-Z]*\\.", "") + "@" + trace[3].getLineNumber() + "   " + o);
		// if(Configuration.Debug.verbose) log.debug(m,o);
	}

	public static void dbg(Object o) {
		dbg_delegate(o);
	}

	public static void exception(Throwable t) {
		dbg(t.toString());
	}
	
	@Deprecated
	public static String dump(Object o) {
		String ret = "";
		if (o == null) {
			ret += ("NULL");
		} else {
			ret += (o.getClass());
			for (java.lang.reflect.Field f : o.getClass().getFields()) {
				try {
					ret += (f.getName() + ":   " + f.get(o));
				} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {}
			}
		}
		return ret;
	}

	// classes whose fields not to inspect with details()
	private static List<Class>	baseClasses	= Arrays.asList(new Class[] { Object.class, TileEntity.class, Entity.class, });

	public static void details(Object o) {
		Debug.dbg_delegate("Detailed class dump:");
		if (o != null) {
			Class c = o.getClass();
			print(c + "(" + FMLCommonHandler.instance().getEffectiveSide() + ") @" + System.identityHashCode(o));
			while (!baseClasses.contains(c)) {
				for (Field f : c.getDeclaredFields()) {
					if((f.getModifiers() & Modifier.STATIC) == 0) //does not have static bit set - do not print class fields
						try {
							f.setAccessible(true);
							Object obj=f.get(o);
							if(obj != null && obj.getClass().isArray()) {
								for(int i=0; i<Array.getLength(obj); i++) {
									print("   " + f.getName() + "     " + i + "     " + Array.get(obj, i));
								}
							} else {
								print("   " + f.getName() + "     " + obj);
							}
						} catch (Exception e) {
							print(""+e);
						}
				}
				c = c.getSuperclass();
			}
		}
	}

	public static void mark() {
		dbg_delegate("executed");
	}

	public static void side() {
		Debug.dbg_delegate(FMLCommonHandler.instance().getEffectiveSide());
	}

	/*
	 * Releases mouse control, so you can go use the debugger controls
	 */
	public static void bp() {
		if(PlanetguyLib.doPLLogging && FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT){
			Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
		}
	}
}
