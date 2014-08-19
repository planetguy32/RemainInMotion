package me.planetguy.remaininmotion.util ;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import me.planetguy.util.Debug;
import net.minecraft.world.World;

public class Reflection
{
	
	public static void init() {
		if(EstablishField(World.class, "loadedEntityList")!=null){
			Debug.dbg("Deobfuscated MC detected");
			instance=new Reflection();
		}else
			instance=new ReflectionObfuscated();
	}

	private static Reflection instance;

	public static boolean Verbose = false ;

	public static HashMap<String, Field> fieldCache=new HashMap<String, Field>();
	public static HashMap<String, Method> methodCache=new HashMap<String, Method>();

	public static Class EstablishClass ( String Name )
	{
		try
		{
			return ( Class . forName ( Name ) ) ;
		}
		catch ( Throwable Throwable )
		{
			if ( Verbose )
			{
				Throwable . printStackTrace ( ) ;
			}

			return ( null ) ;
		}
	}

	public static java . lang . reflect . Method EstablishMethod ( Class Class , String Name , Class ... Arguments )
	{
		try
		{
			java . lang . reflect . Method Method = Class . getDeclaredMethod ( Name , Arguments ) ;

			Method . setAccessible ( true ) ;

			return ( Method ) ;
		}
		catch ( Throwable Throwable )
		{
			if ( Verbose )
			{
				Throwable . printStackTrace ( ) ;
			}

			return ( null ) ;
		}
	}

	public static java . lang . reflect . Field EstablishField ( Class Class , String Name )
	{
		try
		{
			java . lang . reflect . Field Field = Class . getDeclaredField ( Name ) ;

			Field . setAccessible ( true ) ;

			return ( Field ) ;
		}
		catch ( Throwable Throwable )
		{
			if ( Verbose )
			{
				Throwable . printStackTrace ( ) ;
			}

			return ( null ) ;
		}
	}

	/*
	public static Object get(Class c, Object o, String field){
		return instance.getImpl(c, o, field);
	}

	public static void set(Class c, Object o, String field, Object in){
		instance.setImpl(c, o, field, in);
	}

	public static Object runMethod(Class class1, Object chunk,
			String name, Object...objects){
		return instance.runMethodImpl(class1, chunk, name, objects);
	}
	
	*/

	public Object getImpl(Class c, Object o, String field){
		try{
			field=remap(field);
			String fqfn=c.getSimpleName()+"/"+field;
			if(fieldCache.containsKey(fqfn)){
				return fieldCache.get(fqfn).get(o);
			}else{
				Field f=c.getDeclaredField(field);
				f.setAccessible(true);
				fieldCache.put(fqfn, f);
				return f.get(o);
			}
		}catch(Exception e){
			throw new RuntimeException("Could not access field "+field+": "+e.getClass().getSimpleName());
		}
	}


	public void setImpl(Class c, Object o, String field, Object in){
		try{
			field=remap(field);
			String fqfn=c.getCanonicalName()+"/"+field;
			if(fieldCache.containsKey(fqfn)){
				fieldCache.get(fqfn).set(o, in);
			}else{
				Field f=c.getDeclaredField(field);
				f.setAccessible(true);
				fieldCache.put(fqfn, f);
				f.set(o, in);
			}
		}catch(Exception e){
			throw new RuntimeException("Could not access field "+field+": "+e.getClass().getSimpleName());
		}
	}

	public String remap(String s){
		return s;
	}

	public Object runMethodImpl(Class class1, Object chunk,
			String name, Object[] objects) {
		try{
			name=remap(name);
			String fqmn=class1.getCanonicalName()+"/"+name;
			if(methodCache.containsKey(fqmn)){
				return methodCache.get(fqmn).invoke(chunk, objects);
			}else{
				ArrayList<Class> classes=new ArrayList<Class>();
				for(Object o:objects){
					classes.add(o.getClass());
				}
				Method m=class1.getDeclaredMethod(name, classes.toArray(new Class[0]));
				m.setAccessible(true);
				methodCache.put(fqmn, m);
				return m.invoke(chunk, objects);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}
