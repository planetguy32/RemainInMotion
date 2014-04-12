package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.core.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class CarriageRenderer extends BlockRenderer
{
	public CarriageRenderer ( )
	{
		Blocks . Carriage . RenderId = Initialize ( Blocks . Carriage ) ;
	}

	@Override
	public void Render ( net . minecraft . tileentity . TileEntity TileEntity )
	{
		if ( TileEntity instanceof TemplateCarriageEntity )
		{
			TemplateCarriageEntity Carriage = ( TemplateCarriageEntity ) TileEntity ;

			if ( Carriage . Pattern == null )
			{
				return ;
			}

			if ( Carriage . RenderPattern == false )
			{
				return ;
			}

			SetSideSpan ( 0 , 0 , 1 , 1 ) ;

			SetTextureSpan ( me.planetguy.remaininmotion . Carriage . PlaceholderIcon ) ;

			for ( BlockRecord Record : Carriage . Pattern )
			{
				RenderGhost ( Record . X , Record . Y , Record . Z ) ;
			}
		}
	}

	@Override
	public IIcon GetIcon ( net . minecraft . item . ItemStack Item , Directions Side )
	{
		if ( Side != Directions . PosY )
		{

			if ( CarriageItem.GetDecorationId(Item) != null )
			{
				int DecorationMeta = CarriageItem . GetDecorationMeta ( Item ) ;

				try
				{
					return ( CarriageItem.GetDecorationId(Item) . getIcon ( Side . ordinal ( ) , DecorationMeta ) ) ;
				}
				catch ( Throwable Throwable )
				{
					Throwable . printStackTrace ( ) ;
				}
			}
		}

		return ( Blocks . Carriage . getIcon ( 0 , BlockItem . GetBlockType ( Item ) ) ) ;
	}
}
