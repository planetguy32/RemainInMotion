package me.planetguy.remaininmotion.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class RIMTileEntityRenderer extends TileEntitySpecialRenderer {
	@Override
	public void renderTileEntityAt(TileEntity TileEntity, double X, double Y, double Z, float PartialTick) {
		Render.PushMatrix();

		Render.Translate(X, Y, Z);

		Render(TileEntity, PartialTick);

		Render.PopMatrix();
	}

	public abstract void Render(TileEntity TileEntity, float PartialTick);
}
