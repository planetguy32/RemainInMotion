package me.planetguy.remaininmotion.util;

import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import me.planetguy.lib.util.Reflection;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public abstract class SneakyWorldUtil {

    public static void SetBlock(World world, int X, int Y, int Z, Block spectre, int Meta) {
        Chunk chunk = world.getChunkFromBlockCoords(X, Z);

        int ChunkX = X & 0xF;
        int ChunkY = Y & 0xF;
        int ChunkZ = Z & 0xF;

        Block block1 = chunk.getBlock(X, Y, Z);
        int k2 = block1.getLightOpacity(world, X, Y, Z);

        TileEntity oldTE=world.getTileEntity(X, Y, Z);
        if(oldTE != null) //no null checks inside the function there
        	world.func_147457_a(oldTE);

        chunk.removeTileEntity(ChunkX, Y, ChunkZ);

        int LayerY = Y >> 4;
        // Tested, this does work.
        ExtendedBlockStorage[] storageArrays = chunk.getBlockStorageArray();

        if (storageArrays[LayerY] == null) {
            storageArrays[LayerY] = new ExtendedBlockStorage((LayerY) << 4, !world.provider.hasNoSky);
        }

        // RIMLog.dump(spectre);

        if (spectre == null) {
            spectre = Blocks.air;
        }

        storageArrays[LayerY].func_150818_a(ChunkX, ChunkY, ChunkZ, spectre);

        storageArrays[LayerY].setExtBlockMetadata(ChunkX, ChunkY, ChunkZ, Meta);


        // Heightmap and Skylight
        int oldHeight = chunk.getHeightValue(ChunkX,ChunkZ);
        // Don't do this if we aren't changing the top block
        if(Y >= oldHeight) {
            chunk.precipitationHeightMap[ChunkZ << 4 | ChunkX] = -999;
            int i = chunk.getTopFilledSegment();
            int l = i + 16 - 1;

            while (true) {
                if (l > 0) {
                    if (chunk.func_150808_b(ChunkX, l - 1, ChunkZ) == 0) {
                        --l;
                        continue;
                    }

                    chunk.heightMap[ChunkZ << 4 | ChunkX] = l;

                    if (l < chunk.heightMapMinimum) {
                        chunk.heightMapMinimum = l;
                    }
                }

                if (!world.provider.hasNoSky)
                {
                    l = 15;
                    int i1 = i + 16 - 1;

                    do
                    {
                        int j1 = chunk.func_150808_b(ChunkX, i1, ChunkZ);

                        if (j1 == 0 && l != 15)
                        {
                            j1 = 1;
                        }

                        l -= j1;

                        if (l > 0)
                        {
                            ExtendedBlockStorage extendedblockstorage = storageArrays[i1 >> 4];

                            if (extendedblockstorage != null)
                            {
                                extendedblockstorage.setExtSkylightValue(ChunkX, i1 & 15, ChunkZ, l);
                            }
                        }

                        --i1;
                    }
                    while (i1 > 0 && l > 0);
                }

                break;
            }
        } else {
            int j2 = block1.getLightOpacity(world, X, Y, Z);
            if (j2 != k2 && (j2 < k2 || chunk.getSavedLightValue(EnumSkyBlock.Sky, ChunkX, Y, ChunkZ) > 0 || chunk.getSavedLightValue(EnumSkyBlock.Block, ChunkX, Y, ChunkZ) > 0))
            {
                chunk.propagateSkylightOcclusion(ChunkX, ChunkZ);
            }
        }


        chunk.isModified = true;

        world.markBlockForUpdate(X, Y, Z);
    }

    public static void SetTileEntity(World world, int X, int Y, int Z, TileEntity entity) {
        if (entity == null) { throw new NullPointerException(); }
        // This does exactly the same thing, except without reflection
        world.addTileEntity(entity);
        
        world.getChunkFromBlockCoords(X, Z).func_150812_a(X & 0xF, Y, Z & 0xF, entity);
    }

    /*
     * out of context, this is woefully redundant and inefficient, and really
     * needs to be fixed
     *
     * Minecraft does this now by default anyway, we can just remove it.
     */
    public static void UpdateLighting(World world, int X, int Y, int Z, Block block) {
        // found the update light method!
        world.func_147451_t(X, Y, Z);
        world.markBlockForUpdate(X,Y,Z);
    }

    public static void NotifyBlocks(World world, int X, int Y, int Z, Block OldId, Block NewId) {
        world.notifyBlockChange(X,Y,Z,OldId);

        if (NewId == null) { return; }

        if ((world.getTileEntity(X, Y, Z) != null) || (NewId.hasComparatorInputOverride())) {
            world.func_147453_f(X, Y, Z, NewId);
        }
    }

    public static void RefreshBlock(World world, int X, int Y, int Z, Block OldId, Block NewId) {
        UpdateLighting(world, X, Y, Z, OldId);

        NotifyBlocks(world, X, Y, Z, OldId, NewId);
    }
    public static void notifyBlock(World world, int x, int y, int z, Block source)
    {
        Block block = world.getBlock(x, y, z);
        if (block != null) {
            block.onNeighborBlockChange(world, x, y, z, source);
        }
    }

    public static void updateIndirectNeighbors(World world, int x, int y, int z, Block block)
    {
        if ((world.isRemote) ||
                (FMLCommonHandler.instance().getSide() == Side.CLIENT)) {
            return;
        }
        for (int inDirX = -3; inDirX <= 3; inDirX++) {
            for (int inDirY = -3; inDirY <= 3; inDirY++) {
                for (int inDirZ = -3; inDirZ <= 3; inDirZ++)
                {
                    int updateDirection = inDirX >= 0 ? inDirX : -inDirX;
                    updateDirection += (inDirY >= 0 ? inDirY : -inDirY);
                    updateDirection += (inDirZ >= 0 ? inDirZ : -inDirZ);
                    if (updateDirection <= 3) {

                        notifyBlock(world, x + inDirX, y + inDirY, z + inDirZ, block);
                    }
                }
            }
        }
    }


    public static void markBlockDirty(World world, int x, int y, int z) {
        if (world.blockExists(x, y, z)) {
            world.getChunkFromBlockCoords(x, z).setChunkModified();
        }
    }
}
