package me.planetguy.remaininmotion.core;

public class ClientSetupProxy {
	public static final String		ClientSideClassName	= Mod.Namespace + "core.ClientSetup";
	public static final String		ServerSideClassName	= Mod.Namespace + "core.ClientSetupProxy";

	@cpw.mods.fml.common.SidedProxy(clientSide = ClientSideClassName, serverSide = ServerSideClassName)
	public static ClientSetupProxy	Instance;

	public void Execute() {}
}
