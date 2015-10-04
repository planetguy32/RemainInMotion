package me.planetguy.remaininmotion.render;

import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import net.minecraft.tileentity.TileEntity;

public class MotiveSpectreRenderer extends RIMTileEntityRenderer {
	@Override
	public void Render(TileEntity TileEntity, float PartialTick) {

		TileEntityMotiveSpectre Spectre = (TileEntityMotiveSpectre) TileEntity;

		if (Spectre.renderCacheKey == null) { return; }

		{
			double Offset;

			if (RiMConfiguration.CarriageMotion.RenderInFinalPositionDuringLag
					&& (Spectre.ticksExisted >= Spectre.personalDurationInTicks)) {
				Offset = 1;
			} else {
				Offset = Math.min(Spectre.personalVelocity * (Spectre.ticksExisted + PartialTick), 1.0D);
			}

			Render.Translate(Offset * Spectre.motionDirection.deltaX - Spectre.xCoord, Offset
					* Spectre.motionDirection.deltaY - Spectre.yCoord, Offset * Spectre.motionDirection.deltaZ
					- Spectre.zCoord);
		}

		Integer DisplayList = CarriageRenderCache.lookupDisplayList(Spectre.renderCacheKey);

		if (DisplayList != null) {
			Render.ResetBoundTexture();

			Render.ExecuteDisplayList(DisplayList);

			Render.ResetBoundTexture();
		}
	}
}
