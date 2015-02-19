package me.planetguy.remaininmotion.core;

import cpw.mods.fml.common.SidedProxy;

public class ClientSetupProxy {
	public static final String		ClientSideClassName	= ModRiM.Namespace + "core.ClientSetup";
	public static final String		ServerSideClassName	= ModRiM.Namespace + "core.ClientSetupProxy";

	@SidedProxy(clientSide = ClientSideClassName, serverSide = ServerSideClassName)
	public static ClientSetupProxy	Instance;

	public void Execute() {}
}
