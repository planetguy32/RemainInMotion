package me.planetguy.remaininmotion.render;

import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import me.planetguy.remaininmotion.spectre.TileEntityRotativeSpectre;
import net.minecraft.tileentity.TileEntity;

public class RotativeSpectreRenderer extends RIMTileEntityRenderer {

	public static final int[][]	matrices	= new int[][] { { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 },
			{ -1, 0, 0 }, { 1, 0, 0 },		};

	@Override
	public void renderTileEntityAt(TileEntity TileEntity, double X, double Y, double Z, float PartialTick) {
		Render.PushMatrix();

		try {

			TileEntityRotativeSpectre Spectre = (TileEntityRotativeSpectre) TileEntity;

			if (Spectre.renderCacheKey != null) {
				double Offset;

				if (RiMConfiguration.CarriageMotion.RenderInFinalPositionDuringLag
						&& (Spectre.ticksExisted >= RiMConfiguration.CarriageMotion.MotionDuration)) {
					Offset = 1;
				} else {
					Offset = Math.min(TileEntityMotiveSpectre.velocity * (Spectre.ticksExisted + PartialTick), 1.0D);
				}
				if (Spectre.renderCacheKey != null) {

					Render.Translate(X, Y, Z); // negative player pos

					Render.Translate(0.5, 0.5, 0.5);

					int axis = Spectre.getAxis();

                    // TODO implement other angles
					Render.Rotate(Offset * -90, matrices[axis][0], matrices[axis][1], matrices[axis][2]);

                    // negative block pos
					Render.Translate(-Spectre.xCoord - .5, -Spectre.yCoord - .5, -Spectre.zCoord - .5);

					Integer DisplayList = CarriageRenderCache.lookupDisplayList(Spectre.renderCacheKey);

					if (DisplayList != null) {
						Render.ResetBoundTexture();

						Render.ExecuteDisplayList(DisplayList);

						Render.ResetBoundTexture();
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Render.PopMatrix();
	}

	@Override
	public void Render(TileEntity TileEntity, float PartialTick) {}
}
