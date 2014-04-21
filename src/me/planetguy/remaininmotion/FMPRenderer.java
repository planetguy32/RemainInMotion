package me.planetguy.remaininmotion;

import net.minecraft.world.World;
import codechicken.lib.lighting.LazyLightMatrix;
import codechicken.lib.render.CCModel;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.IMicroMaterialRender;
import codechicken.microblock.JMicroblockClient;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.MicroMaterialRegistry.IMicroMaterial;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

class FMPRenderer implements IMicroMaterialRender{

	BlockCoord pos=new BlockCoord();

	public CCModel generateModel(){
		CCModel ccm=CCModel.quadModel(12*8);
		for(int i=0; i<12; i++){
			ccm.generateBlock(i*8, FMPCarriage.cubeOutsideEdges[i]);
		}
		return ccm;
	}

	private World world;

	@Override
	public Cuboid6 getRenderBounds() {
		return Cuboid6.full;
	}

	@Override
	public World world() {
		return world;
	}

	@Override
	public int x() {
		return pos.x;
	}

	@Override
	public int y() {
		return pos.y;
	}

	@Override
	public int z() {
		return pos.z;
	}

	public void renderCovers(World world, Vector3 t, LazyLightMatrix olm, int material){
		IMicroMaterial microMaterial = MicroMaterialRegistry.getMaterial("tile.wood");
		for(Cuboid6 c:FMPCarriage.cubeOutsideEdges){
			JMicroblockClient.renderCuboid(t, olm, microMaterial, c, 0, this);
		}
	}

}