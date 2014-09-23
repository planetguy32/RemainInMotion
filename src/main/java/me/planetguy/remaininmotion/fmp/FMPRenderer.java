package me.planetguy.remaininmotion.fmp;

import net.minecraft.world.World;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.CCModel;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.IMicroMaterialRender;
import codechicken.microblock.MaterialRenderHelper;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.MicroMaterialRegistry.IMicroMaterial;
import codechicken.microblock.MicroblockRender;
import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "IMicroMaterialRender", modid = "ForgeMultipart")
class FMPRenderer implements IMicroMaterialRender{

	BlockCoord pos=new BlockCoord();

	@Optional.Method(modid = "ForgeMultipart")
	public CCModel generateModel(){
		CCModel ccm=CCModel.quadModel(12*8);
		for(int i=0; i<12; i++){
			ccm.generateBlock(i*8, FMPCarriage.cubeOutsideEdges[i]);
		}
		return ccm;
	}

	private World world;

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
	public void renderCovers(World world, Vector3 t, int pass){
		IMicroMaterial microMaterial = MicroMaterialRegistry.getMaterial("tile.wood");
		for(Cuboid6 c:FMPCarriage.cubeOutsideEdges){
			MicroblockRender.renderCuboid(t, microMaterial, pass, c, 0);
		}
	}

}