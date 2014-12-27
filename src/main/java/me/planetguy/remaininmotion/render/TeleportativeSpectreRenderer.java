package me.planetguy.remaininmotion.render;

import net.minecraft.tileentity.TileEntity;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.spectre.TileEntityTeleportativeSpectre;

public class TeleportativeSpectreRenderer extends RIMTileEntityRenderer {
	@Override
	public void Render(TileEntity TileEntity, float PartialTick) {
		TileEntityTeleportativeSpectre Spectre = (TileEntityTeleportativeSpectre) TileEntity;

		if (Spectre.RenderCacheKey == null) { return; }

		Integer DisplayList = CarriageRenderCache.lookupDisplayList(Spectre.RenderCacheKey);

		if (DisplayList == null) { return; }

		double Timestamp = Math.min(Spectre.TicksExisted + PartialTick,
				RiMConfiguration.CarriageMotion.TeleportationDuration);

		double Threshold;

		if (RiMConfiguration.CarriageMotion.RenderInFinalPositionDuringLag
				&& (Spectre.TicksExisted >= RiMConfiguration.CarriageMotion.TeleportationDuration)) {
			Threshold = 2;
		} else {
			Threshold = (Timestamp / RiMConfiguration.CarriageMotion.TeleportationDuration) * 0.95 + 0.025;
		}

		double Value = Math.abs(Math.sin(Timestamp * ((2 * Math.PI) / 20) * 4));

		if (Spectre.Source == true) {
			Render.Translate(-Spectre.xCoord, -Spectre.yCoord, -Spectre.zCoord);
		} else {
			Threshold = 1 - Threshold;

			Render.Translate(-Spectre.xCoord + Spectre.ShiftX, -Spectre.yCoord + Spectre.ShiftY, -Spectre.zCoord
					+ Spectre.ShiftZ);
		}

		if (Value > Threshold) {
			Render.ResetBoundTexture();

			Render.ExecuteDisplayList(DisplayList);

			Render.ResetBoundTexture();
		}
	}
}
