package me.planetguy.remaininmotion.core ;

import me.planetguy.remaininmotion.Spectre;
import me.planetguy.remaininmotion.carriage.Carriage;
import me.planetguy.remaininmotion.drive.CarriageDrive;
import net.minecraft.block.Block;


public abstract class RIMBlocks
{
	public static Carriage Carriage ;

	public static CarriageDrive CarriageDrive ;

	public static Spectre Spectre ;
	
	public static Block air=net.minecraft.init.Blocks.air;

	public static void Initialize ( )
	{
		Carriage = new Carriage ( ) ;

		CarriageDrive = new CarriageDrive ( ) ;

		Spectre = new Spectre ( ) ;
		
	}
}
