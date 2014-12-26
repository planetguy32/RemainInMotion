package me.planetguy.remaininmotion.render;

import net.minecraft.tileentity.TileEntity;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;

public class MotiveSpectreRenderer extends RIMTileEntityRenderer {
	@Override
	public void Render(TileEntity TileEntity, float PartialTick) {

		TileEntityMotiveSpectre Spectre = (TileEntityMotiveSpectre) TileEntity;

		if (Spectre.RenderCacheKey == null) { return; }

		{
			double Offset;

			if (RiMConfiguration.CarriageMotion.RenderInFinalPositionDuringLag
					&& (Spectre.TicksExisted >= RiMConfiguration.CarriageMotion.MotionDuration)) {
				Offset = 1;
			} else {
				Offset = TileEntityMotiveSpectre.Velocity * (Spectre.TicksExisted + PartialTick);
			}

			Render.Translate(Offset * Spectre.MotionDirection.DeltaX - Spectre.xCoord, Offset
					* Spectre.MotionDirection.DeltaY - Spectre.yCoord, Offset * Spectre.MotionDirection.DeltaZ
					- Spectre.zCoord);
		}

		Integer DisplayList = CarriageRenderCache.lookupDisplayList(Spectre.RenderCacheKey);

		if (DisplayList != null) {
			Render.ResetBoundTexture();

			Render.ExecuteDisplayList(DisplayList);

			Render.ResetBoundTexture();
		}
	}
}
