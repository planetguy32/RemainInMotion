package me.planetguy.remaininmotion.fmp;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.CarriagePackageUtil;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.core.Blocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.IIcon;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JCuboidPart;
import codechicken.multipart.JIconHitEffects;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.minecraft.McBlockPart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.Optional;

public class FMPCarriage extends McBlockPart implements Moveable{

	public static FMPCarriage instance;

	final FMPCarriageRenderer renderer=new FMPCarriageRenderer();

	static final double l=1.5/8;

	public static final Cuboid6[] cubeOutsideEdges=new Cuboid6[]{ //not most efficient, but understandable, system;
		new Cuboid6(0,0,0, 1, l, l),
		new Cuboid6(0,0,0, l, 1-l, l),
		new Cuboid6(0,0,0, l, l, 1),

		new Cuboid6(1,1,0, 0,   1-l, l ),
		new Cuboid6(1,1,0, 1-l, l,   l ),
		new Cuboid6(1,1,0, 1-l, 1-l, 1-l),

		new Cuboid6(1,0,1, 1-l, l,    l ),
		new Cuboid6(1,0,1, 1-l, 1-l,  1-l),
		new Cuboid6(1,0,1, l,   l,    1-l),

		new Cuboid6(0,1,1, 1,  1-l, 1-l),
		new Cuboid6(0,1,1, l,  l,   1-l),
		new Cuboid6(0,1,1, l,  1-l, l),

	};

	@Override
	public Iterable<Cuboid6> getOcclusionBoxes() {
		return new Iterable(){
			public Iterator iterator(){
				return new Iterator(){
					//empty iterator
					@Override
					public boolean hasNext() {return false;}
					@Override
					public Object next() {return null;}
					@Override
					public void remove() {}
				};
				
			}
		};
	}

	public Iterable<Cuboid6> getCollisionBoxes() {
		return new Iterable(){

			@Override
			public Iterator iterator() {
				return new Iterator(){

					int idx=-1;

					@Override
					public boolean hasNext() {
						return idx+1<cubeOutsideEdges.length;
					}

					@Override
					public Object next() {
						idx++;
						return cubeOutsideEdges[idx];
					}

					@Override
					public void remove() {
					}

				};
			}

		};
	}

	@Override
	public String getType() {
		return "FMPCarriage";
	}

	public Cuboid6 getBounds(){
		return Cuboid6.full;
	}


	@Override
	public Block getBlock() {
		return Blocks.Carriage;
	}

	@Override
	public boolean renderStatic(Vector3 pos, int pass){
		renderer.renderCovers(this.world(), pos, null, pass);
		return true;
	}

	@Override
	public void fillPackage(CarriagePackage _package)
			throws me.planetguy.remaininmotion.util.CarriageMotionException {
		CarriagePackageUtil.fillFramePackage(_package, this.world());
	}

}
