package me.planetguy.remaininmotion.core.interop.mod;

import net.minecraft.block.Block;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.event.BlockSelectForMoveEvent;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.position.BlockRecord;

public class QmunityMultipart {
	
	@SubscribeEvent
	public void add(BlockSelectForMoveEvent e){
		//Force to FMP
		Debug.dbg("Hi there");
		if(e.location.entity() instanceof uk.co.qmunity.lib.tile.TileMultipart){
			e.location.world().setTileEntity(e.location.x(), e.location.y(), e.location.z(), codechicken.multipart.TileMultipart.getOrConvertTile(e.location.world(), new codechicken.lib.vec.BlockCoord(e.location.x(), e.location.y(), e.location.z()) ));
			SneakyWorldUtil.SetBlock(e.location.world(), e.location.x(), e.location.y(), e.location.z(), Block.getBlockFromName("ForgeMultipart:multipart"), 0);
			BlockRecord rloc=(BlockRecord) e.location;
			rloc.Identify(e.location.world()); //flush state - this is a non-API call
			ForgeMultipart.saveMultipartTick(rloc.entity(), rloc.entityTag());
		}
	}

}
