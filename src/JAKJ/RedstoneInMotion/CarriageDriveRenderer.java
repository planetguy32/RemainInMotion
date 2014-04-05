package JAKJ . RedstoneInMotion ;

public class CarriageDriveRenderer extends BlockRenderer
{
	public CarriageDriveRenderer ( )
	{
		Blocks . CarriageDrive . RenderId = Initialize ( Blocks . CarriageDrive . blockID ) ;
	}

	public void UseFullLabel ( )
	{
		Configuration . TextureSets TextureSet = Configuration . TextureSets . values ( ) [ Configuration . TextureSets . TextureSet ] ;

		SetSideSpan ( TextureSet . LabelMinH , TextureSet . LabelMinV , TextureSet . LabelMaxH , TextureSet . LabelMaxV ) ;
	}

	public void UsePartialLabel ( double MinH , double MinV , double MaxH , double MaxV )
	{
		UseFullLabel ( ) ;

		SubsectSideSpan ( MinH , MinV , MaxH , MaxV ) ;
	}

	public void UseFullIcon ( net . minecraft . util . Icon Icon )
	{
		SetTextureSpan ( Icon ) ;
	}

	public void UsePartialIcon ( net . minecraft . util . Icon Icon , double MinU , double MinV , double MaxU , double MaxV )
	{
		UseFullIcon ( Icon ) ;

		SubsectTextureSpan ( MinU , MinV , MaxU , MaxV ) ;
	}

	@Override
	public void Render ( net . minecraft . tileentity . TileEntity TileEntity )
	{
		if ( TileEntity instanceof CarriageTranslocatorEntity )
		{
			CarriageTranslocatorEntity Translocator = ( CarriageTranslocatorEntity ) TileEntity ;

			if ( Translocator . Player == null )
			{
				return ;
			}

			UseFullLabel ( ) ;

			if ( Translocator . Player . equals ( "" ) )
			{
				UseFullIcon ( CarriageDrive . PublicIcon ) ;
			}
			else
			{
				if ( Translocator . Player . equals ( net . minecraft . client . Minecraft . getMinecraft ( ) . thePlayer . username ) )
				{
					UseFullIcon ( CarriageDrive . PrivateToSelfIcon ) ;
				}
				else
				{
					UseFullIcon ( CarriageDrive . PrivateToOtherIcon ) ;
				}
			}

			RenderOverlay ( 0.001 , Translocator . SideClosed , false ) ;

			for ( Vanilla . DyeTypes DyeType : Vanilla . DyeTypes . values ( ) )
			{
				if ( CarriageDriveItem . LabelHasDye ( Translocator . Label , DyeType ) )
				{
					double GridH = ( ( double ) ( DyeType . ordinal ( ) % 4 ) ) / 4 ;
					double GridV = ( ( double ) ( DyeType . ordinal ( ) / 4 ) ) / 4 ;

					UsePartialLabel ( GridH , GridV , GridH + 0.25 , GridV + 0.25 ) ;

					UsePartialIcon ( CarriageDrive . DyeIconSet , GridH , GridV , GridH + 0.25 , GridV + 0.25 ) ;

					RenderOverlay ( 0.002 , Translocator . SideClosed , false ) ;
				}
			}
		}
	}

	@Override
	public net . minecraft . util . Icon GetIcon ( net . minecraft . item . ItemStack Item , Directions Side )
	{
		return ( Blocks . CarriageDrive . getIcon ( 0 , BlockItem . GetBlockType ( Item ) ) ) ;
	}
}
