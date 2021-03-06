package me.planetguy.remaininmotion.plugins.fmp;

import net.minecraft.world.World;
import codechicken.lib.render.BlockRenderer.BlockFace;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.IMicroMaterialRender;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.MicroMaterialRegistry.IMicroMaterial;
import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "IMicroMaterialRender", modid = "ForgeMultipart")
public class FMPRenderer implements IMicroMaterialRender {

	BlockCoord	pos	= new BlockCoord();

	CCModel		ccm;

	@Optional.Method(modid = "ForgeMultipart")
	public CCModel generateModel() {
		if (ccm == null) {
			/*
			 * ccm=CCModel.quadModel(12*8); for(int i=0; i<12; i++){
			 * ccm.generateBlock(i*8, FMPCarriage.cubeOutsideEdges[i]); }
			 */
			ccm = CCModel.quadModel(8);
			ccm.generateBlock(0, Cuboid6.full);
		}
		return ccm;
	}

	private World	world;

	@Optional.Method(modid = "ForgeMultipart")
	@Override
	public Cuboid6 getRenderBounds() {
		return Cuboid6.full;
	}

	@Optional.Method(modid = "ForgeMultipart")
	@Override
	public World world() {
		return world;
	}

	@Optional.Method(modid = "ForgeMultipart")
	@Override
	public int x() {
		return pos.x;
	}

	@Optional.Method(modid = "ForgeMultipart")
	@Override
	public int y() {
		return pos.y;
	}

	@Optional.Method(modid = "ForgeMultipart")
	@Override
	public int z() {
		return pos.z;
	}

	@Optional.Method(modid = "ForgeMultipart")
	public void renderCovers(World world, Vector3 t, int pass, PartCarriageFMP part) {

		// render early, only once
		if (pass != 0) { return; }

		if (mmOpen == null) {
			mmOpen = MicroMaterialRegistry.getMaterial("tile.wood");
		}

		if (mmClosed == null) {
			mmClosed = MicroMaterialRegistry.getMaterial("tile.wood");
			/*
			 * for(Cuboid6 c:FMPCarriage.cubeOutsideEdges){
			 * MicroblockRender.renderCuboid(t, microMaterial, pass, c, 0); }
			 */
			// MicroblockRender.renderCuboid(t, microMaterial, pass,
			// Cuboid6.full,
			// 0);
		}

		// code based on MicroblockRender.renderCuboid
		CCRenderState.setModel(face);
		for (int i = 0; i < 6; i++) {
			if (part.tile().partMap(i) == null) {
				drawFace(i, part.drawSideClosedJAKJ(i) ? mmClosed : mmOpen, pass, t);
			} else {
				drawFace(i, mmCorners, pass, t);
			}
		}

	}

	IMicroMaterial	mmOpen		= MicroMaterialRegistry.getMaterial("tile.hollowCarriage.open");
	IMicroMaterial	mmClosed	= MicroMaterialRegistry.getMaterial("tile.hollowCarriage.closed");
	IMicroMaterial	mmCorners	= MicroMaterialRegistry.getMaterial("tile.hollowCarriage.corners");

	BlockFace		face		= new BlockFace();

	private void drawFace(int side, IMicroMaterial microMaterial, int pass, Vector3 position) {
		face.loadCuboidFace(Cuboid6.full, side);
		microMaterial.renderMicroFace(position, pass, Cuboid6.full);
	}

}