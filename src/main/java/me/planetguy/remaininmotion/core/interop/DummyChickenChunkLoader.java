package me.planetguy.remaininmotion.core.interop;

import codechicken.chunkloader.ChickenChunks;
import codechicken.chunkloader.IChickenChunkLoader;
import codechicken.chunkloader.TileChunkLoaderBase;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.Collection;
import java.util.HashSet;

public class DummyChickenChunkLoader implements IChickenChunkLoader {

    private String owner;
    private BlockCoord coords;
    private Collection<ChunkCoordIntPair> chunks;
    private World world;


    public DummyChickenChunkLoader(TileChunkLoaderBase tile)
    {
        owner = tile.getOwner();
        coords = tile.getPosition();
        chunks = tile.getChunks();
        world = tile.getWorldObj();
    }

    public DummyChickenChunkLoader(NBTTagCompound tag){
        this.readFromNBT(tag);
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setString("owner",owner);
        NBTTagCompound coord = new NBTTagCompound();
        coord.setInteger("X", coords.x);
        coord.setInteger("Y", coords.y);
        coord.setInteger("Z", coords.z);
        tag.setTag("coords", coord);
        tag.setInteger("dimension", world.getWorldInfo().getVanillaDimension());
        NBTTagList chunk = new NBTTagList();
        //long l = (((long)x) << 32) | (y & 0xffffffffL);
        //int x = (int)(l >> 32);
        //int y = (int)l;
        for(ChunkCoordIntPair pair : chunks){
            chunk.appendTag(new NBTTagIntArray(new int[]{pair.chunkXPos, pair.chunkZPos}));
        }
        tag.setTag("chunks",chunk);
    }

    public void readFromNBT(NBTTagCompound tag){
        owner = tag.getString("owner");
        NBTTagCompound coord = tag.getCompoundTag("coords");
        coords = new BlockCoord(coord.getInteger("X"),coord.getInteger("Y"),coord.getInteger("Z"));
        world = DimensionManager.getWorld(tag.getInteger("dimension"));
        HashSet<ChunkCoordIntPair> chunk = new HashSet<ChunkCoordIntPair>();
        NBTTagList list = tag.getTagList("chunks", 4);
        int size = list.tagCount();
        for(int i = 0; i < size; i++){
            int[] array = list.func_150306_c(i);
            chunk.add(new ChunkCoordIntPair(array[0],array[1]));
        }
        chunks = chunk;

    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public Object getMod() {
        return ChickenChunks.instance;
    }

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public BlockCoord getPosition() {
        return coords;
    }

    @Override
    public void deactivate() {}

    public void activate(){}

    @Override
    public Collection<ChunkCoordIntPair> getChunks() {
        return chunks;
    }
}
