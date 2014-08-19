package me.planetguy.remaininmotion.fmp;

import java.util.Iterator;

import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.block.Block;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.minecraft.McBlockPart;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.Interface(iface="JNormalOcclusion", modid="ForgeMultipart")
public class FMPCarriage extends McBlockPart implements JNormalOcclusion, Moveable{

	public static FMPCarriage instance;

	@SideOnly(Side.CLIENT)
	FMPRenderer renderer;
	
	public FMPCarriage(){
		if(FMLCommonHandler.instance().getSide().isClient())
			renderer=new FMPRenderer();
	}

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

	@Optional.Method(modid = "ForgeMultipart")
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

	@Optional.Method(modid = "ForgeMultipart")
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

	@Optional.Method(modid = "ForgeMultipart")
	@Override
	public String getType() {
		return "FMPCarriage";
	}

	@Optional.Method(modid = "ForgeMultipart")
	public Cuboid6 getBounds(){
		return Cuboid6.full;
	}

	@Optional.Method(modid = "ForgeMultipart")
	@Override
	public Block getBlock() {
		return RIMBlocks.Carriage;
	}

	@Optional.Method(modid = "ForgeMultipart")
	@SideOnly(Side.CLIENT)
	public void renderStatic(Vector3 pos, LightMatrix llm, int pass){
		renderer.renderCovers(this.world(), pos, llm, pass);
	}

	@Optional.Method(modid = "ForgeMultipart")
	@Override
	public void fillPackage(CarriagePackage _package)
			throws CarriageMotionException {
		MultiTypeCarriageUtil.fillFramePackage(_package, this.world());
	}

}
