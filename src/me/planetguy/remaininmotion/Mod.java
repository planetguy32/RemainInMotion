package me.planetguy.remaininmotion ;

@cpw . mods . fml . common . Mod ( modid = Mod . Handle , name = Mod . Title , version = Mod . Version )
@cpw . mods . fml . common . network . NetworkMod ( clientSideRequired = true , serverSideRequired = false , channels = { Mod . Channel } , packetHandler = PacketManager . class )
public class Mod
{
	public static final String Namespace = "JAKJ.RedstoneInMotion." ;

	public static final String Handle = "JAKJ_RedstoneInMotion" ;

	public static final String Title = "Redstone In Motion" ;

	public static final String Version = "2.3.0.0" ;

	public static final String Channel = "JAKJ_RIM" ;

	@cpw . mods . fml . common . Mod . EventHandler
	public void PreInit ( cpw . mods . fml . common . event . FMLPreInitializationEvent Event )
	{
		( new Configuration ( Event . getSuggestedConfigurationFile ( ) ) ) . Process ( ) ;

		Core . HandlePreInit ( ) ;
	}

	@cpw . mods . fml . common . Mod . EventHandler
	public void Init ( cpw . mods . fml . common . event . FMLInitializationEvent Event )
	{
		Core . HandleInit ( ) ;
	}

	@cpw . mods . fml . common . Mod . EventHandler
	public void PostInit ( cpw . mods . fml . common . event . FMLPostInitializationEvent Event )
	{
		ClientSetupProxy . Instance . Execute ( ) ;

		Core . HandlePostInit ( ) ;
	}

	@cpw . mods . fml . common . Mod . EventHandler
	public void ServerStopping ( cpw . mods . fml . common . event . FMLServerStoppingEvent Event )
	{
		Core . HandleServerStopping ( ) ;
	}
}
