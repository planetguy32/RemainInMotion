package me.planetguy.remaininmotion.core.interop.fmp;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import scala.collection.JavaConversions;
import codechicken.lib.world.WorldExtension;
import codechicken.lib.world.WorldExtensionManager;
import codechicken.multipart.MultipartHelper;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.TickScheduler.ChunkTickScheduler;
import codechicken.multipart.TickScheduler.PartTickEntry;
import codechicken.multipart.TickScheduler.WorldTickScheduler;
import codechicken.multipart.handler.MultipartSaveLoad;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.api.event.IBlockPos;
import me.planetguy.remaininmotion.api.event.MotionFinalizeEvent;
import me.planetguy.remaininmotion.api.event.TEPostPlaceEvent;
import me.planetguy.remaininmotion.api.event.TEPrePlaceEvent;
import me.planetguy.remaininmotion.api.event.TEStartMoveEvent;
import me.planetguy.remaininmotion.api.event.UnpackStartEvent;
import me.planetguy.remaininmotion.core.interop.ModInteraction;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;

public class FMPEventHandler {
	
	private HashMap<World, WorldTickScheduler> knownSchedulers=new HashMap<World, WorldTickScheduler>();
	
	@SubscribeEvent
	public void onFMPMoved(TEStartMoveEvent e) {
		if(isMultipart(e.location.entity())) {
			saveMultipartTick(e.location.entity(), e.location.entityTag());
		}
	}
	
	@SubscribeEvent
	public void onFMPFinalized(MotionFinalizeEvent e) {
		if(isMultipart(e.location.entity())) {
			loadMultipartTick(e.location);
		}
	}
	
	@SubscribeEvent
	public void replaceTE(TEPrePlaceEvent e) {
		BlockRecord record=(BlockRecord) e.location;
		World worldObj=record.World;
		if(record.entityRecord.getString("id").equals(
				"savedMultipart")) {
			try {
				record.entity = MultipartHelper.createTileFromNBT(worldObj, record.entityRecord);
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}
		}
	}
	
	@SubscribeEvent
	public void onPlaced(TEPostPlaceEvent e) {
		BlockRecord record=(BlockRecord) e.location;
		World worldObj=record.World;
		if(record.entityRecord.getString("id").equals(
				"savedMultipart")) {
			try {
				((TileMultipart) record.entity).onChunkLoad();
				MultipartHelper.sendDescPacket(worldObj, record.entity);
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}
		}
	}
	
	@SubscribeEvent
	public void onUnpackStart(UnpackStartEvent e) {
		MultipartSaveLoad.loadingWorld_$eq(e.unpackingEntity.getWorldObj());
	}
	
	public void saveMultipartTick(TileEntity te, NBTTagCompound record) {
		WorldTickScheduler sched=getTickScheduler(te.getWorldObj());
		for(ChunkTickScheduler cts:JavaConversions.asJavaCollection(sched.tickChunks())) {
			for(PartTickEntry entry:JavaConversions.asJavaCollection(cts.tickList())) {
				if(entry.part().tile() == te) {
					record.setInteger("RemIMFMPTickD", (int) (entry.time()-sched.schedTime())); 
					//subtract off scheduler time -> ticks in future
					record.setBoolean("RemIMFMPTickR", entry.random());
					record.setString("RemIMFMPTickT", entry.part().getType());
				}
			}
		}
		
	}
	
	private WorldTickScheduler getTickScheduler(World w) {
		if(knownSchedulers.containsKey(w)) {
			return knownSchedulers.get(w);
		}else {
			WorldExtension we;
			int extensionsCounted=0;
			try {
				while(true) {
					we=WorldExtensionManager.getWorldExtension(w, extensionsCounted);
					if(we instanceof WorldTickScheduler) {
						knownSchedulers.put(w, (WorldTickScheduler) we);
						return (WorldTickScheduler) we;
					}
				}
			}catch(Exception e) {
				return null;
			}
		}
	}
	
	public void loadMultipartTick(IBlockPos pos) {
		TileEntity te=pos.entity();
		NBTTagCompound record=pos.entityTag();
		WorldTickScheduler sched=getTickScheduler(te.getWorldObj());
		long ticks=record.getInteger("RemIMFMPTickD");
		boolean random=record.getBoolean("RemIMFMPTickR");
		String type=record.getString("RemIMFMPTickT");
		for(TMultiPart part:JavaConversions.asJavaCollection(((TileMultipart)te).partList())) {
			if(part.getType().equals(type))
				sched.scheduleTick(part, (int) ticks, random);
		}
	}

	public boolean isMultipart(TileEntity part) {
		return part instanceof TileMultipart;
	}

}
