package me.planetguy.remaininmotion.core.interop.mod;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.handler.MultipartProxy_serverImpl;
import uk.co.qmunity.lib.part.compat.fmp.FMPPartFactory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.Reflection;
import me.planetguy.lib.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.api.event.BlockSelectForMoveEvent;
import me.planetguy.remaininmotion.plugins.fmp.PartCarriageFMP;
import me.planetguy.remaininmotion.util.position.BlockRecord;

public class QmunityMultipart implements Runnable {
	
	private static final String qchType="QmunityConversionHelper";

	FMPPartFactory factory=new FMPPartFactory();
	
	/* Run at init!!! */
	public void run(){
		MultiPartRegistry.registerParts(new IPartFactory() {
			@Override
			public TMultiPart createPart(String arg0, boolean arg1) {
				if (arg0.equals(qchType)) 
					return new QmunityConversionHelper();
				else throw new RuntimeException("Requested string "+arg0);
			}

		}, new String[] { qchType });
	}
	
	@SubscribeEvent
	public void add(BlockSelectForMoveEvent e){
		//Force to FMP
		//if(!e.location.world().isRemote)
			if(e.location.entity() instanceof uk.co.qmunity.lib.tile.TileMultipart){
				int x=e.location.x();
				int y=e.location.y();
				int z=e.location.z();

				BlockCoord coord=new BlockCoord(x,y,z);
				
				TMultiPart part=new QmunityConversionHelper();

				TileMultipart.addPart(e.location.world(), coord, part);
				TileMultipart.getTile(e.location.world(), coord).remPart(part);

				BlockRecord rloc=(BlockRecord) e.location;
				rloc.Identify(e.location.world()); //flush state - this is a non-API call
				rloc.entityRecord=new NBTTagCompound();
				TileMultipart.getTile(e.location.world(), coord).writeToNBT(rloc.entityRecord);
				ForgeMultipart.saveMultipartTick(rloc.entity(), rloc.entityTag());
			}
	}
	
	private final class QmunityConversionHelper extends TMultiPart {
		@Override
		public String getType() {
			return qchType;
		}
	}

}
