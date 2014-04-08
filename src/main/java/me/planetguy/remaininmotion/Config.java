package me.planetguy.remaininmotion ;

import net.minecraftforge.common.config.Configuration;

public abstract class Config
{
	public Configuration Configuration ;

	public String Category ;

	public Config ( java . io . File File )
	{
		Configuration = new Configuration ( File , true ) ;
	}

	public String String ( String Name , String Default )
	{
		return ( Configuration . get ( Category , Name , Default ) . getString ( ) ) ;
	}

	public boolean Boolean ( String Name , boolean Default )
	{
		return ( Configuration . get ( Category , Name , Default ) . getBoolean ( Default ) ) ;
	}

	public int Integer ( String Name , int Default )
	{
		return ( Configuration . get ( Category , Name , Default ) . getInt ( Default ) ) ;
	}

	public int BoundedInteger ( String Name , int Min , int Default , int Max )
	{
		int Value = Integer ( Name , Default ) ;

		if ( Value < Min )
		{
			new RuntimeException ( Name + " must be at least " + Min ) . printStackTrace ( ) ;

			return ( Default ) ;
		}

		if ( Value > Max )
		{
			new RuntimeException ( Name + " must be at most " + Max ) . printStackTrace ( ) ;

			return ( Default ) ;
		}

		return ( Value ) ;
	}

}
