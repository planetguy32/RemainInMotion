package me.planetguy.remaininmotion.util ;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.world.chunk.Chunk;

public abstract class Reflection
{
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

	public static Object get(Class c, Object o, String field){
		try{
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

	public static void set(Class c, Object o, String field, Object in){
		try{
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

	public static String obfuscate(String s){
		return s; //TODO
	}

	public static Object runMethod(Class class1, Object chunk,
			String name, Object...objects) {
		try{
			name=obfuscate(name);
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
