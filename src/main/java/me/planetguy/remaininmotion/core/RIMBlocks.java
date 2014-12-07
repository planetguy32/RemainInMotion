package me.planetguy.remaininmotion.core ;

import me.planetguy.remaininmotion.carriage.Carriage;
import me.planetguy.remaininmotion.drive.CarriageDrive;
import me.planetguy.remaininmotion.spectre.Spectre;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;


public abstract class RIMBlocks
{
	public static Carriage Carriage ;

	public static CarriageDrive CarriageDrive ;

	public static Spectre Spectre ;
	
	public static Block air=Blocks.air;

	public static void Initialize ( )
	{
		Carriage = new Carriage ( ) ;

		CarriageDrive = new CarriageDrive ( ) ;

		Spectre = new Spectre ( ) ;
		
	}
}
