package me.planetguy.remaininmotion ;

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
}
