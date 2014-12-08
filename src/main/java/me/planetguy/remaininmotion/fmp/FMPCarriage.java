package me.planetguy.remaininmotion.fmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.core.ModInteraction.Wrenches;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.Microblock;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.minecraft.McBlockPart;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.Interface(iface="JNormalOcclusion", modid="ForgeMultipart")
public class FMPCarriage extends McBlockPart implements JNormalOcclusion, Moveable, ICloseable{

	private boolean[] sidesClosed=new boolean[6];
	
	public static FMPCarriage instance;

	@SideOnly(Side.CLIENT)
	FMPRenderer renderer;
	
	public FMPCarriage(){
		if(FMLCommonHandler.instance().getSide().isClient())
			renderer=new FMPRenderer();
	}

	static final double l=1.5/8;

	//The amount the cuboid extends outside 
	static final double e=0.01;
	
	public static final Cuboid6[] cubeOutsideEdges=new Cuboid6[]{ //not most efficient, but understandable, system;
		new Cuboid6(0-e,0-e,0-e, 1+e, l, l),
		new Cuboid6(0-e,0-e,0-e, l, 1+e-l, l),
		new Cuboid6(0-e,0-e,0-e, l, l, 1+e),

		new Cuboid6(1+e,1+e,0-e, 0-e,   1+e-l, l ),
		new Cuboid6(1+e,1+e,0-e, 1+e-l, l,   l ),
		new Cuboid6(1+e,1+e,0-e, 1+e-l, 1+e-l, 1+e-l),

		new Cuboid6(1+e,0-e,1+e, 1+e-l, l,    l ),
		new Cuboid6(1+e,0-e,1+e, 1+e-l, 1+e-l,  1+e-l),
		new Cuboid6(1+e,0-e,1+e, l,   l,    1+e-l),

		new Cuboid6(0-e,1+e,1+e, 1+e,  1+e-l, 1+e-l),
		new Cuboid6(0-e,1+e,1+e, l,  l,   1+e-l),
		new Cuboid6(0-e,1+e,1+e, l,  1+e-l, l),

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

					//int idx=-1;

					@Override
					public boolean hasNext() {
						return false;// idx+1<cubeOutsideEdges.length;
					}

					@Override
					public Object next() {
						//idx++;
						return null;// cubeOutsideEdges[idx];
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
	public boolean renderStatic(Vector3 pos, int pass){
		renderer.renderCovers(this.world(), pos, pass, this);
		return true;
	}

	@Optional.Method(modid = "ForgeMultipart")
	@Override
	public void fillPackage(CarriagePackage _package)
			throws CarriageMotionException {
		MultiTypeCarriageUtil.fillFramePackage(_package, this.world());
	}

	@Override
	public boolean activate(EntityPlayer player, MovingObjectPosition hit, ItemStack held) {
		if(ToolItemSet . IsScrewdriverOrEquivalent ( held)) {
			sidesClosed[hit.sideHit] = !sidesClosed[hit.sideHit];
			return true;
		}
		return false;
	}

	@Override
	public boolean isSideClosed(int side) {
		Debug.dbg(FMLCommonHandler.instance().getEffectiveSide()+":"+Arrays.toString(sidesClosed));
		return sidesClosed[side] || isSideCovered(side);
	}
	
	private boolean isSideCovered(int side) {
		Object partInSlot=this.tile().partMap(side);
		Debug.mark();
		if(partInSlot != null) {
			if(partInSlot instanceof Microblock) {
				Microblock mb=(Microblock) partInSlot;
				int size=mb.getSize();
				Debug.dbg(size);
			}
		}
		return false;
	}
	
	public void writeDesc(MCDataOutput packet){
		packet.writeByte((byte) toInt());
	}
	
	public void readDesc(MCDataInput packet){
		fromInt(packet.readByte());
	}
	
	public void save(NBTTagCompound tag){
		
		tag.setByte("sideBitMask", (byte)toInt());
	}
	/**
	* Load part from NBT (only called serverside)
	*/
	public void load(NBTTagCompound tag){
		fromInt(tag.getByte("sideBitMask"));
	}
	
	public int toInt() {
		int i=0;
		int pos=1;
		for(int index=0; index<sidesClosed.length; index++) {
			if(sidesClosed[index])
				i |= pos;
			pos=pos << 1;
		}
		return i;
	}
	
	public void fromInt(int i) {
		sidesClosed=new boolean[6];
		int pos=1;
		for(int index=0; index<sidesClosed.length; index++) {
			sidesClosed[index]= (i & pos) != 0;
			pos=pos << 1;
		}
	}
	
}
