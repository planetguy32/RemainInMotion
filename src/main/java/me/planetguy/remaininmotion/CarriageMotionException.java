package me.planetguy.remaininmotion ;

public class CarriageMotionException extends Exception
{
	public CarriageMotionException ( String Message )
	{
		super ( Message ) ;
		this.printStackTrace();
		
	}
}
