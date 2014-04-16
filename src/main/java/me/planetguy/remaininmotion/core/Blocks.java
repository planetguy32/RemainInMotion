package me.planetguy.remaininmotion.core ;

import me.planetguy.remaininmotion.Carriage;
import me.planetguy.remaininmotion.CarriageDrive;
import me.planetguy.remaininmotion.FMPCarriage;
import me.planetguy.remaininmotion.Spectre;

import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

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

		MultiPartRegistry.registerParts(new IPartFactory(){

			@Override
			public TMultiPart createPart(String arg0, boolean arg1) {
				if(arg0.equals("FMPCarriage"))
					return new FMPCarriage();
				return null;
			}

		}, new String[]{"FMPCarriage"});


	}

}
