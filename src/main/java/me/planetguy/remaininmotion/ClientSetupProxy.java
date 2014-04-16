package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.core.Mod;

public class ClientSetupProxy
{
	@cpw . mods . fml . common . SidedProxy ( clientSide = "me.planetguy.remaininmotion.ClientSetup" , serverSide = "me.planetguy.remaininmotion.ClientSetupProxy" )
	public static ClientSetupProxy Instance ;

	public void Execute ( )
	{
	}
}
