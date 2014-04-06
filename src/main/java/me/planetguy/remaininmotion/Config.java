package me.planetguy.remaininmotion ;

public abstract class Config
{
	public net . minecraftforge . common . Configuration Configuration ;

	public String Category ;

	public Config ( java . io . File File )
	{
		Configuration = new net . minecraftforge . common . Configuration ( File , true ) ;
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

	public int BlockId ( String Name , int Default )
	{
		return ( Configuration . getBlock ( "Block IDs" , Name , Default ) . getInt ( Default ) ) ;
	}

	public int ItemId ( String Name , int Default )
	{
		return ( Configuration . getItem ( "Item IDs" , Name , Default ) . getInt ( Default ) ) ;
	}
}
