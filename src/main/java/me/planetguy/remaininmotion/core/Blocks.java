package me.planetguy.remaininmotion.core ;

import me.planetguy.remaininmotion.CarriageDrive;
import me.planetguy.remaininmotion.Spectre;
import me.planetguy.remaininmotion.carriage.Carriage;

public abstract class Blocks
{
	public static Carriage Carriage ;

	public static CarriageDrive CarriageDrive ;

	public static Spectre Spectre ;

	public static void Initialize ( )
	{
		Carriage = new Carriage ( ) ;

		CarriageDrive = new CarriageDrive ( ) ;

		Spectre = new Spectre ( ) ;

	}

}
