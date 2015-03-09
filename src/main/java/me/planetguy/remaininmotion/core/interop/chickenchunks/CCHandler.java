package me.planetguy.remaininmotion.core.interop.chickenchunks;

import codechicken.chunkloader.ChunkLoaderManager;
import codechicken.chunkloader.TileChunkLoaderBase;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.core.interop.ModInteraction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CCHandler implements IChickenChunksHandler {
	
    /* (non-Javadoc)
	 * @see me.planetguy.remaininmotion.core.interop.chickenchunks.IChickenChunksHandler#performChickenChunksPostInit(net.minecraft.world.World, me.planetguy.remaininmotion.BlockRecord)
	 */
    @Override
	public void performChickenChunksPostInit(World worldObj, BlockRecord record){
        try{
            if(record.entity instanceof TileChunkLoaderBase){
                if(record.entityRecord.hasKey("ChickenChunkLoader")){
                    if(!worldObj.isRemote) {
                        ((TileChunkLoaderBase) record.entity).activate();
                        // remove chunk loader afterwards
                        NBTTagCompound tag = record.entityRecord.getCompoundTag("ChickenChunkLoader");
                        DummyChickenChunkLoader loader = new DummyChickenChunkLoader(tag);
                        ChunkLoaderManager.remChunkLoader(loader);
                        // make sure those chunks are loaded
                        ((TileChunkLoaderBase) record.entity).activate();
                    }
                }
            }
        }catch(Throwable t) {}
    }
    
    /* (non-Javadoc)
	 * @see me.planetguy.remaininmotion.core.interop.chickenchunks.IChickenChunksHandler#handleChickenChunks(net.minecraft.world.World, me.planetguy.remaininmotion.BlockRecord, me.planetguy.remaininmotion.BlockRecord)
	 */
    @Override
	public void handleChickenChunks(World worldObj, BlockRecord record, BlockRecord newPosition)
    {
        if(record.entityRecord == null) return;
        TileEntity te = worldObj.getTileEntity(record.X, record.Y, record.Z);
        if(te != null){
            if(te instanceof TileChunkLoaderBase){
                // are we still in the same chunk? If so, don't destroy the ticket and cause unnecessary lag.
                if(record.X >> 4 != newPosition.X >> 4 && record.Z >> 4 != newPosition.Z >> 4){
                    // store the chunk loader so chunks are not unloaded prematurely
                    if(((TileChunkLoaderBase)te).active){
                        NBTTagCompound tag = new NBTTagCompound();
                        DummyChickenChunkLoader loader = new DummyChickenChunkLoader((TileChunkLoaderBase) te);
                        loader.writeToNBT(tag);
                        record.entityRecord.setTag("ChickenChunkLoader",tag);
                    }
                }
            }
        }
    }

}
