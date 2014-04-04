package JAKJ . RedstoneInMotion ;

public class TeleportativeSpectreRenderer extends TileEntityRenderer
{
	@Override
	public void Render ( net . minecraft . tileentity . TileEntity TileEntity , float PartialTick )
	{
		TeleportativeSpectreEntity Spectre = ( TeleportativeSpectreEntity ) TileEntity ;

		if ( Spectre . RenderCacheKey == null )
		{
			return ;
		}

		Integer DisplayList = CarriageRenderCache . Lookup ( Spectre . RenderCacheKey ) ;

		if ( DisplayList == null )
		{
			return ;
		}

		double Timestamp = Math . min ( Spectre . TicksExisted + PartialTick , Configuration . CarriageMotion . TeleportationDuration ) ;

		double Threshold ;

		if ( Configuration . CarriageMotion . RenderInFinalPositionDuringLag && ( Spectre . TicksExisted >= Configuration . CarriageMotion . TeleportationDuration ) )
		{
			Threshold = 2 ;
		}
		else
		{
			Threshold = ( Timestamp / Configuration . CarriageMotion . TeleportationDuration ) * 0.95 + 0.025 ;
		}

		double Value = Math . abs ( Math . sin ( Timestamp * ( ( 2 * Math . PI ) / 20 ) * 4 ) ) ;

		if ( Spectre . Source == true )
		{
			Render . Translate ( - Spectre . xCoord , - Spectre . yCoord , - Spectre . zCoord ) ;
		}
		else
		{
			Threshold = 1 - Threshold ;

			Render . Translate ( - Spectre . xCoord + Spectre . ShiftX , - Spectre . yCoord + Spectre . ShiftY , - Spectre . zCoord + Spectre . ShiftZ ) ;
		}

		if ( Value > Threshold )
		{
			Render . ResetBoundTexture ( ) ;

			Render . ExecuteDisplayList ( DisplayList ) ;

			Render . ResetBoundTexture ( ) ;
		}
	}
}
