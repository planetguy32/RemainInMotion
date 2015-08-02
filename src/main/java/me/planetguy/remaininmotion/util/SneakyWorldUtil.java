package me.planetguy.remaininmotion.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public abstract class SneakyWorldUtil {

    public static boolean SetBlock(World world, int x, int y, int z, Block newBlock, int meta) {
        int chunkX = x & 0xF;
        int chunkZ = z & 0xF;

        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        
        if(chunk==null)
        	return false;

        int xzCombinedPosition = chunkZ << 4 | chunkX;

        if (y >= chunk.precipitationHeightMap[xzCombinedPosition] - 1)
        {
            chunk.precipitationHeightMap[xzCombinedPosition] = -999;
        }

        int heightMapAtTarget = chunk.heightMap[xzCombinedPosition];
        Block oldBlock = chunk.getBlock(chunkX, y, chunkZ);
        int metadata = chunk.getBlockMetadata(chunkX, y, chunkZ);

        if (oldBlock == newBlock && metadata == meta)
        {
            return false;
        }
        else
        {
            ExtendedBlockStorage xbs = chunk.storageArrays[y >> 4];
            boolean heightMapChanged = false;

            if (xbs == null)
            {
                if (newBlock == Blocks.air)
                {
                    return false;
                }

                xbs = chunk.storageArrays[y >> 4] = new ExtendedBlockStorage(y >> 4 << 4, !world.provider.hasNoSky);
                heightMapChanged = y >= heightMapAtTarget;
            }

            int oldOpacity = oldBlock.getLightOpacity(world, x, y, z);

            xbs.func_150818_a(chunkX, y & 15, chunkZ, newBlock);
            xbs.setExtBlockMetadata(chunkX, y & 15, chunkZ, meta); // This line duplicates the one below, so breakBlock fires with valid worldstate

            if (!world.isRemote)
            {
                // After breakBlock a phantom TE might have been created with incorrect meta. This attempts to kill that phantom TE so the normal one can be create properly later
                TileEntity te = chunk.getTileEntityUnsafe(chunkX & 0x0F, y, chunkZ & 0x0F);
                if (te != null && te.shouldRefresh(oldBlock, chunk.getBlock(chunkX & 0x0F, y, chunkZ & 0x0F), metadata, chunk.getBlockMetadata(chunkX & 0x0F, y, chunkZ & 0x0F), world, x, y, z))
                {
                	world.restoringBlockSnapshots=true;
                    chunk.removeTileEntity(chunkX & 0x0F, y, chunkZ & 0x0F);
                    world.restoringBlockSnapshots=false;
                }
            }
            else if (oldBlock.hasTileEntity(metadata))
            {
                TileEntity te = chunk.getTileEntityUnsafe(chunkX & 0x0F, y, chunkZ & 0x0F);
                if (te != null && te.shouldRefresh(oldBlock, newBlock, metadata, meta, world, x, y, z))
                {
                    world.restoringBlockSnapshots=true;
                    world.removeTileEntity(x, y, z);
                    world.restoringBlockSnapshots=false;
                }
            }

            if (xbs.getBlockByExtId(chunkX, y & 15, chunkZ) != newBlock)
            {
                return false;
            }
            else
            {
                xbs.setExtBlockMetadata(chunkX, y & 15, chunkZ, meta);

                if (heightMapChanged)
                {
                    chunk.generateSkylightMap();
                }
                else
                {
                    int newOpacity = newBlock.getLightOpacity(world, x, y, z);

                    if (newOpacity > 0)
                    {
                        if (y >= heightMapAtTarget)
                        {
                            chunk.relightBlock(chunkX, y + 1, chunkZ);
                        }
                    }
                    else if (y == heightMapAtTarget - 1)
                    {
                        chunk.relightBlock(chunkX, y, chunkZ);
                    }

                    if (newOpacity != oldOpacity && (newOpacity < oldOpacity || chunk.getSavedLightValue(EnumSkyBlock.Sky, chunkX, y, chunkZ) > 0 || chunk.getSavedLightValue(EnumSkyBlock.Block, chunkX, y, chunkZ) > 0))
                    {
                        chunk.propagateSkylightOcclusion(chunkX, chunkZ);
                    }
                }

                TileEntity tileentity;

                if (newBlock.hasTileEntity(meta))
                {
                    tileentity = chunk.func_150806_e(chunkX, y, chunkZ);

                    if (tileentity != null)
                    {
                        tileentity.updateContainingBlockInfo();
                        tileentity.blockMetadata = meta;
                    }
                }

                chunk.isModified = true;

                world.func_147451_t(x, y, z);

                if(chunk.func_150802_k()) world.markBlockForUpdate(x, y, z);

                return true;
            }
        }

    }

    public static void SetTileEntity(World world, int X, int Y, int Z, TileEntity entity) {
        if (entity == null) { throw new NullPointerException(); }
        // This does exactly the same thing, except without reflection
        world.addTileEntity(entity);
        
        world.getChunkFromBlockCoords(X, Z).func_150812_a(X & 0xF, Y, Z & 0xF, entity);
    }

    public static void NotifyBlocks(World world, int X, int Y, int Z, Block OldId, Block NewId) {
        world.notifyBlockChange(X,Y,Z,OldId);

        if (NewId == null) { return; }

        if ((world.getTileEntity(X, Y, Z) != null) || (NewId.hasComparatorInputOverride())) {
            world.func_147453_f(X, Y, Z, NewId);
        }
    }

    public static void RefreshBlock(World world, int X, int Y, int Z, Block OldId, Block NewId) {
        NotifyBlocks(world, X, Y, Z, OldId, NewId);
    }

}
