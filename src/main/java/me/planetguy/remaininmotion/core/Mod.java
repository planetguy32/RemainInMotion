package me.planetguy.remaininmotion.core ;

import me.planetguy.remaininmotion.ClientSetupProxy;

@cpw . mods . fml . common . Mod ( modid = Mod . Handle , name = Mod . Title , version = Mod . Version )
public class Mod
{
	public static final String Namespace = "me.planetguy.remaininmotion." ;

	public static final String Handle = "JAKJ_RedstoneInMotion" ;

	public static final String Title = "Redstone In Motion" ;

	public static final String Version = "1.0.1" ;

	public static final String Channel = "JAKJ_RIM" ;

	@cpw . mods . fml . common . Mod . EventHandler
	public void PreInit ( cpw . mods . fml . common . event . FMLPreInitializationEvent Event )
	{
		( new Configuration ( Event . getSuggestedConfigurationFile ( ) ) ) . Process ( ) ;

		Core . HandleInit ( ) ;
	}

	@cpw . mods . fml . common . Mod . EventHandler
	public void Init ( cpw . mods . fml . common . event . FMLInitializationEvent Event )
	{
		//Core . HandleInit ( ) ;
		
		ClientSetupProxy . Instance . Execute ( ) ;
	}

	@cpw . mods . fml . common . Mod . EventHandler
	public void PostInit ( cpw . mods . fml . common . event . FMLPostInitializationEvent Event )
	{
		
		

		Core . HandlePostInit ( ) ;
	}

	@cpw . mods . fml . common . Mod . EventHandler
	public void ServerStopping ( cpw . mods . fml . common . event . FMLServerStoppingEvent Event )
	{
		Core . HandleServerStopping ( ) ;
	}
}
