package me.planetguy.remaininmotion.client ;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.Vanilla;
import me.planetguy.remaininmotion.base.BlockItem;
import me.planetguy.remaininmotion.base.BlockRenderer;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.drive.CarriageDrive;
import me.planetguy.remaininmotion.drive.CarriageDriveItem;
import me.planetguy.remaininmotion.drive.CarriageTranslocatorEntity;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class CarriageDriveRenderer extends BlockRenderer
{
	public CarriageDriveRenderer ( )
	{
		RIMBlocks . CarriageDrive . RenderId = Initialize ( RIMBlocks . CarriageDrive ) ;
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

	public void UseFullIcon ( IIcon Icon )
	{
		SetTextureSpan ( Icon ) ;
	}

	public void UsePartialIcon ( IIcon Icon , double MinU , double MinV , double MaxU , double MaxV )
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
				if ( Translocator . Player . equals ( net . minecraft . client . Minecraft . getMinecraft ( ) . thePlayer.getDisplayName() ) )
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
	public IIcon GetIcon ( net . minecraft . item . ItemStack Item , Directions Side )
	{
		
		IIcon ico= RIMBlocks . CarriageDrive . getIcon ( Side.ordinal() , BlockItem . GetBlockType ( Item ) ) ;
		if(ico == null) {
			return Blocks.stone.getIcon(0, 0);
		}else
			return (ico) ;
	}
}
