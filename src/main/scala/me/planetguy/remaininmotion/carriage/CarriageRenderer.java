package me.planetguy.remaininmotion.carriage ;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.base.BlockItem;
import me.planetguy.remaininmotion.base.BlockRenderer;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class CarriageRenderer extends BlockRenderer
{
	public CarriageRenderer ( )
	{
		RIMBlocks . Carriage . RenderId = Initialize ( RIMBlocks . Carriage ) ;
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

			SetTextureSpan ( me.planetguy.remaininmotion.carriage.Carriage . PlaceholderIcon ) ;

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
			int id=CarriageItem . GetDecorationId ( Item ) ;
			
			if ( id != 0 )
			{
				Block DecorationId =Block.getBlockById(id);

				int DecorationMeta = CarriageItem . GetDecorationMeta ( Item ) ;

				try
				{
					return ( DecorationId . getIcon ( Side . ordinal ( ) , DecorationMeta ) ) ;
				}
				catch ( Throwable Throwable )
				{
					Throwable . printStackTrace ( ) ;
				}
			}
		}

		return ( RIMBlocks . Carriage . getIcon ( 0 , BlockItem . GetBlockType ( Item ) ) ) ;
	}
}
