package me.planetguy.remaininmotion.core.interop.mod;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
import blusunrize.immersiveengineering.common.util.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.planetguy.remaininmotion.api.event.BlockPreMoveEvent;
import me.planetguy.remaininmotion.api.event.MotionFinalizeEvent;
import me.planetguy.remaininmotion.api.event.TEPostPlaceEvent;
import me.planetguy.remaininmotion.util.position.BlockRecord;

public class ImmersiveEngineering {
	
	@SubscribeEvent
	public void saveConnections(BlockPreMoveEvent e) {
		if(e.location.entity() instanceof IImmersiveConnectable) {
   			ChunkCoordinates teLoc=Utils.toCC(e.location.entity());
			NBTTagList ls=new NBTTagList();
			e.location.entityTag().setTag("RemIM_links", ls);
			for(Connection c:ImmersiveNetHandler.INSTANCE
				.getConnections(
						e.location.entity().getWorldObj(),
						teLoc)) {
				NBTTagCompound t=new NBTTagCompound();
				ls.appendTag(t);
				t.setString("wire", c.cableType.getUniqueName());
				t.setDouble("distance", c.length);
				if(c.start.equals(teLoc)) {
					//relative to TE
					if(e.blocks.contains(new BlockRecord(c.end.posX, c.end.posY, c.end.posZ))) {
						t.setBoolean("absolute", false);
						t.setInteger("x", c.end.posX-teLoc.posX);
						t.setInteger("y", c.end.posY-teLoc.posY);
						t.setInteger("z", c.end.posZ-teLoc.posZ);
					} else {
						t.setBoolean("absolute", true);
						t.setInteger("x", c.end.posX);
						t.setInteger("y", c.end.posY);
						t.setInteger("z", c.end.posZ);
					}
				} else if(c.end.equals(teLoc)) {
					if(e.blocks.contains(new BlockRecord(c.start.posX, c.start.posY, c.start.posZ))) {
						t.setBoolean("absolute", false);
						t.setInteger("x", c.start.posX-teLoc.posX);
						t.setInteger("y", c.start.posY-teLoc.posY);
						t.setInteger("z", c.start.posZ-teLoc.posZ);
					} else {
						t.setBoolean("absolute", true);
						t.setInteger("x", c.start.posX);
						t.setInteger("y", c.start.posY);
						t.setInteger("z", c.start.posZ);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void loadConnections(MotionFinalizeEvent e) {
		if(e.location.entity() instanceof IImmersiveConnectable) {
			ChunkCoordinates teLoc=Utils.toCC(e.location.entity());
			NBTTagList tag=e.location.entityTag().getTagList("RemIM_links", 10);
			for(int i=0; i<tag.tagCount(); i++) {
				NBTTagCompound conn=tag.getCompoundTagAt(i);
				int x, y, z;
				if(conn.getBoolean("absolute")) {
					x=conn.getInteger("x");
					y=conn.getInteger("y");
					z=conn.getInteger("z");
				}else {
					x=conn.getInteger("x") + teLoc.posX;
					y=conn.getInteger("y") + teLoc.posY;
					z=conn.getInteger("z") + teLoc.posZ;
				}
				if(e.location.world().getTileEntity(x,y,z)
						instanceof IImmersiveConnectable) {
					WireType type=WireType.getValue(conn.getString("wire"));
					ChunkCoordinates newPos=new ChunkCoordinates(x,y,z);
					int distance=(int) Math.ceil(Math.sqrt(newPos.getDistanceSquaredToChunkCoordinates(teLoc)));
					if(distance<=type.getMaxLength())
						ImmersiveNetHandler.INSTANCE.addConnection(e.location.world(), teLoc, newPos, distance, type);
				}
			}
		}
	}

}
