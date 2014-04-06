package me.planetguy.remaininmotion ;

public class ClientSetup extends ClientSetupProxy
{
	public void RegisterTileEntityRenderer ( TileEntityRenderer Renderer , Class < ? extends TileEntity > ... EntityClasses )
	{
		for ( Class < ? extends TileEntity > EntityClass : EntityClasses )
		{
			cpw . mods . fml . client . registry . ClientRegistry . bindTileEntitySpecialRenderer ( EntityClass , Renderer ) ;
		}
	}

	@Override
	public void Execute ( )
	{
		RegisterTileEntityRenderer ( new MotiveSpectreRenderer ( ) , MotiveSpectreEntity . class ) ;

		RegisterTileEntityRenderer ( new TeleportativeSpectreRenderer ( ) , TeleportativeSpectreEntity . class ) ;

		new CarriageRenderer ( ) ;

		new CarriageDriveRenderer ( ) ;
	}
}
