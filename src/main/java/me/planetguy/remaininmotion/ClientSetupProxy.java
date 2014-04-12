package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.core.Mod;

public class ClientSetupProxy
{
	public static final String ClientSideClassName = ClientSetup.class.getCanonicalName();
	public static final String ServerSideClassName = ClientSetupProxy.class.getCanonicalName();

	@cpw . mods . fml . common . SidedProxy ( clientSide = ClientSideClassName , serverSide = ServerSideClassName )
	public static ClientSetupProxy Instance ;

	public void Execute ( )
	{
	}
}
