package me.planetguy.remaininmotion.util ;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.planetguy.remaininmotion.core.RIMLog;

public abstract class Reflection
{
	public static boolean Verbose = false ;

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

	public static Object stealField(Object obj, String fieldName){
		try {
			return EstablishField(obj.getClass(), fieldName).get(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NullPointerException npe){
			//npe.printStackTrace();
			//throw(npe);
		}
		return null;
	}

	public static Object runMethod(Object obj, String method, Object...objects){
		if(obj!=null)
		try {
			Class[] classes=new Class[objects.length];
			for(int i=0; i<objects.length; i++){
				classes[i]=objects[i].getClass();
			}
			Method m=EstablishMethod(obj.getClass(), method, classes);
			if(m==null){
				System.out.println("Null method! Oh noes!");
			}else
				return m.invoke(obj, objects);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
		return null;
	}
}
