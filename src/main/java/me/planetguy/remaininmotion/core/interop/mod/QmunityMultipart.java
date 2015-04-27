package me.planetguy.remaininmotion.core.interop.mod;

import me.planetguy.remaininmotion.api.event.BlockSelectForMoveEvent;
import me.planetguy.remaininmotion.util.position.BlockRecord;

public class QmunityMultipart {
	
	//TODO test bare QLib parts
	public void add(BlockSelectForMoveEvent e){
		//Force to FMP
		if(e.location.entity() instanceof uk.co.qmunity.lib.tile.TileMultipart){
			e.location.world().setTileEntity(e.location.x(), e.location.y(), e.location.z(), codechicken.multipart.TileMultipart.getOrConvertTile(e.location.world(), new codechicken.lib.vec.BlockCoord(e.location.x(), e.location.y(), e.location.z()) ));
			BlockRecord rloc=(BlockRecord) e.location;
			rloc.Identify(e.location.world()); //flush state - this is a non-API call
			ForgeMultipart.saveMultipartTick(rloc.entity(), rloc.entityTag());
		}
	}

}
