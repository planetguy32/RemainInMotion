package me.planetguy.remaininmotion.client ;

import org.lwjgl.opengl.GL11;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.spectre.MotiveSpectreEntity;
import me.planetguy.remaininmotion.spectre.RotativeSpectreEntity;

public class RotativeSpectreRenderer extends RIMTileEntityRenderer
{
	@Override
	public void Render ( net . minecraft . tileentity . TileEntity TileEntity , float PartialTick )
	{
		
		RotativeSpectreEntity Spectre = ( RotativeSpectreEntity ) TileEntity ;

		if ( Spectre . RenderCacheKey == null )
		{
			return ;
		}

		double Offset ;

		if ( Configuration . CarriageMotion . RenderInFinalPositionDuringLag && ( Spectre . TicksExisted >= Configuration . CarriageMotion . MotionDuration ) )
		{
			Offset = 1 ;
		}
		else
		{
			Offset = Spectre . Velocity * ( Spectre . TicksExisted + PartialTick ) ;
		}
		if(Spectre != null && Spectre.DriveRecord != null) {
			GL11.glTranslatef(Spectre.DriveRecord.X, Spectre.DriveRecord.Y, Spectre.DriveRecord.Z);

			Render . Rotate ( Offset * 90, 0, 1, 0) ;

			GL11.glTranslatef(TileEntity.xCoord, TileEntity.yCoord, TileEntity.zCoord);

			Integer DisplayList = CarriageRenderCache . Lookup ( Spectre . RenderCacheKey ) ;

			if ( DisplayList != null )
			{
				Render . ResetBoundTexture ( ) ;

				Render . ExecuteDisplayList ( DisplayList ) ;

				Render . ResetBoundTexture ( ) ;
			}
		}
	}
}
