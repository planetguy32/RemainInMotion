package me.planetguy.remaininmotion.util ;

public class CarriageObstructionException extends CarriageMotionException
{
	public int X ;
	public int Y ;
	public int Z ;

	public CarriageObstructionException ( String Message , int X , int Y , int Z )
	{
		super ( Message ) ;

		this . X = X ;
		this . Y = Y ;
		this . Z = Z ;
	}
}
