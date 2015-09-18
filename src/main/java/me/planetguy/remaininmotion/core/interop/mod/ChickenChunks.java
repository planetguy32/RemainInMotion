package me.planetguy.remaininmotion.core.interop.mod;

import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.api.event.BlockPreMoveEvent;
import me.planetguy.remaininmotion.api.event.TEPostPlaceEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import codechicken.chunkloader.ChunkLoaderManager;
import codechicken.chunkloader.TileChunkLoaderBase;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ChickenChunks {
	
	@SubscribeEvent
	public void onPostPlace(TEPostPlaceEvent e) {
		performChickenChunksPostInit((BlockRecord) e.location);
	}
	
	@SubscribeEvent
	public void onPreMove(BlockPreMoveEvent e) {
		handleChickenChunks(e.location.world(), (BlockRecord) e.location, (BlockRecord) e.newLoc);
	}
	
	public void performChickenChunksPostInit(BlockRecord record){
		World worldObj=record.world();
        try{
            if(record.entity instanceof TileChunkLoaderBase){
                if(record.entityRecord.hasKey("ChickenChunkLoader")){
                    if(!worldObj.isRemote) {
                        ((TileChunkLoaderBase) record.entity).activate();
                        // remove chunk loader afterwards
                        NBTTagCompound tag = record.entityRecord.getCompoundTag("ChickenChunkLoader");
                        ChickenChunkLoader loader = new ChickenChunkLoader(tag);
                        ChunkLoaderManager.remChunkLoader(loader);
                        // make sure those chunks are loaded
                        ((TileChunkLoaderBase) record.entity).activate();
                    }
                }
            }
        }catch(Throwable t) {}
    }
	
	public void handleChickenChunks(World worldObj, BlockRecord record, BlockRecord newPosition)
    {
        if(record.entityRecord == null) return;
        TileEntity te = worldObj.getTileEntity(record.X, record.Y, record.Z);
        if(te != null){
            if(te instanceof TileChunkLoaderBase){
                // are we still in the same chunk? If so, don't destroy the ticket and cause unnecessary lag.
                if(!(record.X >> 4 == newPosition.X >> 4 && record.Z >> 4 == newPosition.Z >> 4)){
                    // store the chunk loader so chunks are not unloaded prematurely
                    if(((TileChunkLoaderBase)te).active){
                        NBTTagCompound tag = new NBTTagCompound();
                        ChickenChunkLoader loader = new ChickenChunkLoader((TileChunkLoaderBase) te);
                        loader.writeToNBT(tag);
                        record.entityRecord.setTag("ChickenChunkLoader",tag);
                    }
                }
            }
        }
    }

}
