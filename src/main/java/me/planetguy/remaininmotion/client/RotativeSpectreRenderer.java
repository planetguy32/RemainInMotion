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
		if(Spectre != null && Spectre.RenderCacheKey != null) {
			
			GL11.glTranslatef(0f, 0f, 0f);
			
			Render . Rotate ( Offset * -90, 0, 1, 0) ; //TODO implement other angles
			
			GL11.glTranslatef(Spectre.RenderCacheKey.X, Spectre.RenderCacheKey.Y, Spectre.RenderCacheKey.Z);
			
			Integer DisplayList = CarriageRenderCache . lookupDisplayList ( Spectre . RenderCacheKey ) ;

			if ( DisplayList != null )
			{
				Render . ResetBoundTexture ( ) ;

				Render . ExecuteDisplayList ( DisplayList ) ;

				Render . ResetBoundTexture ( ) ;
			}

		}
	}
}
