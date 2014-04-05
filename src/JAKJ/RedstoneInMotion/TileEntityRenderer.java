package JAKJ . RedstoneInMotion ;

public abstract class TileEntityRenderer extends net . minecraft . client . renderer . tileentity . TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt ( net . minecraft . tileentity . TileEntity TileEntity , double X , double Y , double Z , float PartialTick )
	{
		Render . PushMatrix ( ) ;

		Render . Translate ( X , Y , Z ) ;

		Render ( TileEntity , PartialTick ) ;

		Render . PopMatrix ( ) ;
	}

	public abstract void Render ( net . minecraft . tileentity . TileEntity TileEntity , float PartialTick ) ;
}
