package me.planetguy.remaininmotion.render;

import net.minecraft.tileentity.TileEntity;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import me.planetguy.remaininmotion.spectre.TileEntityRotativeSpectre;

public class RotativeSpectreRenderer extends RIMTileEntityRenderer {

	public static final int[][]	matrices	= new int[][] { { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 },
			{ -1, 0, 0 }, { 1, 0, 0 },		};

	@Override
	public void renderTileEntityAt(TileEntity TileEntity, double X, double Y, double Z, float PartialTick) {
		Render.PushMatrix();

		try {

			TileEntityRotativeSpectre Spectre = (TileEntityRotativeSpectre) TileEntity;

			if (Spectre.RenderCacheKey != null) {
				double Offset;

				if (RiMConfiguration.CarriageMotion.RenderInFinalPositionDuringLag
						&& (Spectre.TicksExisted >= RiMConfiguration.CarriageMotion.MotionDuration)) {
					Offset = 1;
				} else {
					Offset = TileEntityMotiveSpectre.Velocity * (Spectre.TicksExisted + PartialTick);
				}
				if (Spectre != null && Spectre.RenderCacheKey != null) {

					Render.Translate(X, Y, Z); // negative player pos

					Render.Translate(0.5, 0.5, 0.5);

					int axis = Spectre.getAxis();

					Render.Rotate(Offset * -90, matrices[axis][0], matrices[axis][1], matrices[axis][2]); // TODO
					// implement
					// other
					// angles

					Render.Translate(-Spectre.xCoord - .5, -Spectre.yCoord - .5, -Spectre.zCoord - .5); // negative
					// block
					// pos

					Integer DisplayList = CarriageRenderCache.lookupDisplayList(Spectre.RenderCacheKey);

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
