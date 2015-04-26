package me.planetguy.remaininmotion.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public abstract class SneakyWorldUtil {

    public static boolean SetBlock(World world, int x, int y, int z, Block block, int meta) {
        int chunkX = x & 0xF;
        int chunkZ = z & 0xF;

        Chunk chunk = world.getChunkFromBlockCoords(x, z);

        int i1 = chunkZ << 4 | chunkX;

        if (y >= chunk.precipitationHeightMap[i1] - 1)
        {
            chunk.precipitationHeightMap[i1] = -999;
        }

        int j1 = chunk.heightMap[i1];
        Block block1 = chunk.getBlock(chunkX, y, chunkZ);
        int k1 = chunk.getBlockMetadata(chunkX, y, chunkZ);

        if (block1 == block && k1 == meta)
        {
            return false;
        }
        else
        {
            ExtendedBlockStorage extendedblockstorage = chunk.storageArrays[y >> 4];
            boolean flag = false;

            if (extendedblockstorage == null)
            {
                if (block == Blocks.air)
                {
                    return false;
                }

                extendedblockstorage = chunk.storageArrays[y >> 4] = new ExtendedBlockStorage(y >> 4 << 4, !world.provider.hasNoSky);
                flag = y >= j1;
            }

            int l1 = x;
            int i2 = z;

            int k2 = block1.getLightOpacity(world, l1, y, i2);

            extendedblockstorage.func_150818_a(chunkX, y & 15, chunkZ, block);
            extendedblockstorage.setExtBlockMetadata(chunkX, y & 15, chunkZ, meta); // This line duplicates the one below, so breakBlock fires with valid worldstate

            if (!world.isRemote)
            {
                // After breakBlock a phantom TE might have been created with incorrect meta. This attempts to kill that phantom TE so the normal one can be create properly later
                TileEntity te = chunk.getTileEntityUnsafe(chunkX & 0x0F, y, chunkZ & 0x0F);
                if (te != null && te.shouldRefresh(block1, chunk.getBlock(chunkX & 0x0F, y, chunkZ & 0x0F), k1, chunk.getBlockMetadata(chunkX & 0x0F, y, chunkZ & 0x0F), world, l1, y, i2))
                {
                    chunk.removeTileEntity(chunkX & 0x0F, y, chunkZ & 0x0F);
                }
            }
            else if (block1.hasTileEntity(k1))
            {
                TileEntity te = chunk.getTileEntityUnsafe(chunkX & 0x0F, y, chunkZ & 0x0F);
                if (te != null && te.shouldRefresh(block1, block, k1, meta, world, l1, y, i2))
                {
                    world.removeTileEntity(l1, y, i2);
                }
            }

            if (extendedblockstorage.getBlockByExtId(chunkX, y & 15, chunkZ) != block)
            {
                return false;
            }
            else
            {
                extendedblockstorage.setExtBlockMetadata(chunkX, y & 15, chunkZ, meta);

                if (flag)
                {
                    chunk.generateSkylightMap();
                }
                else
                {
                    int j2 = block.getLightOpacity(world, l1, y, i2);

                    if (j2 > 0)
                    {
                        if (y >= j1)
                        {
                            chunk.relightBlock(chunkX, y + 1, chunkZ);
                        }
                    }
                    else if (y == j1 - 1)
                    {
                        chunk.relightBlock(chunkX, y, chunkZ);
                    }

                    if (j2 != k2 && (j2 < k2 || chunk.getSavedLightValue(EnumSkyBlock.Sky, chunkX, y, chunkZ) > 0 || chunk.getSavedLightValue(EnumSkyBlock.Block, chunkX, y, chunkZ) > 0))
                    {
                        chunk.propagateSkylightOcclusion(chunkX, chunkZ);
                    }
                }

                TileEntity tileentity;

                if (block.hasTileEntity(meta))
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
