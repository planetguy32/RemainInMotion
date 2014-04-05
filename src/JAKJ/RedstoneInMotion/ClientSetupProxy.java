package JAKJ . RedstoneInMotion ;

public class ClientSetupProxy
{
	public static final String ClientSideClassName = Mod . Namespace + "ClientSetup" ;
	public static final String ServerSideClassName = Mod . Namespace + "ClientSetupProxy" ;

	@cpw . mods . fml . common . SidedProxy ( clientSide = ClientSideClassName , serverSide = ServerSideClassName )
	public static ClientSetupProxy Instance ;

	public void Execute ( )
	{
	}
}
