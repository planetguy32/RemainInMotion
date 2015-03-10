package me.planetguy.remaininmotion.core.interop.fmp;

import java.util.HashMap;

import scala.collection.JavaConversions;
import scala.collection.mutable.HashSet;
import codechicken.lib.world.WorldExtension;
import codechicken.lib.world.WorldExtensionManager;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TickScheduler.ChunkTickScheduler;
import codechicken.multipart.TickScheduler.PartTickEntry;
import codechicken.multipart.TickScheduler.WorldTickScheduler;
import codechicken.multipart.TileMultipart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FMPHandlerImpl implements FMPHandler{
	
	private HashMap<World, WorldTickScheduler> knownSchedulers=new HashMap<World, WorldTickScheduler>();
	
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

	@Override
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

	@Override
	public void loadMultipartTick(TileEntity te, NBTTagCompound record) {
		WorldTickScheduler sched=getTickScheduler(te.getWorldObj());
		long ticks=record.getInteger("RemIMFMPTickD");
		boolean random=record.getBoolean("RemIMFMPTickR");
		String type=record.getString("RemIMFMPTickT");
		for(TMultiPart part:JavaConversions.asJavaCollection(((TileMultipart)te).partList())) {
			if(part.getType().equals(type))
				sched.scheduleTick(part, (int) ticks, random);
		}
	}

	@Override
	public boolean isMultipart(TileEntity part) {
		return part instanceof TileMultipart;
	}

	
}
