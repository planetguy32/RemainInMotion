package me.planetguy.remaininmotion.util.position;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class AABBUtil {

    public static void writeAABBListToNBT(List<AxisAlignedBB> list, NBTTagCompound nbt) {
        if(list.isEmpty()) return;
        NBTTagList tags = new NBTTagList();
        for(AxisAlignedBB aabb : list) {
            NBTTagCompound tag = new NBTTagCompound();
            writeAABBToNBT(aabb, tag);
            tags.appendTag(tag);
        }
        nbt.setTag("CollisionAABB", tags);
    }

    public static void writeAABBToNBT(AxisAlignedBB aabb, NBTTagCompound nbt) {
        nbt.setDouble("minX", aabb.minX);
        nbt.setDouble("maxX", aabb.maxX);

        nbt.setDouble("minY", aabb.minY);
        nbt.setDouble("maxY", aabb.maxY);

        nbt.setDouble("minZ", aabb.minZ);
        nbt.setDouble("maxZ", aabb.maxZ);
    }

    public static AxisAlignedBB readAABBFromNBT(NBTTagCompound tag) {
        double minX = tag.getDouble("minX");
        double maxX = tag.getDouble("maxX");

        double minY = tag.getDouble("minY");
        double maxY = tag.getDouble("maxY");

        double minZ = tag.getDouble("minZ");
        double maxZ = tag.getDouble("maxZ");

        return AxisAlignedBB.getBoundingBox(minX,minY,minZ,maxX,maxY,maxZ);
    }

    public static void writeCollisionBoundingBoxesToNBT(World world, int x, int y, int z, NBTTagCompound nbt) {
        Block block = world.getBlock(x,y,z);
        try {
            ArrayList<AxisAlignedBB> list = new ArrayList();
            block.addCollisionBoxesToList(world, x, y, z, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1), list, null);
            if(!list.isEmpty()) {
                for(AxisAlignedBB aabb : list) {
                    aabb.offset(-x,-y,-z);
                }
                writeAABBListToNBT(list, nbt);
            }
        }catch (Throwable t){}
    }

    public static ArrayList<AxisAlignedBB> readAABBsFromNBT(NBTTagCompound nbt) {
        ArrayList<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
        if(nbt.hasKey("CollisionAABB")) {
            NBTTagList tags = nbt.getTagList("CollisionAABB", 10);
            if(tags.tagCount() > 0) {
                for(int i = 0; i < tags.tagCount(); i++) {
                    NBTTagCompound tag = tags.getCompoundTagAt(i);
                    AxisAlignedBB aabb = readAABBFromNBT(tag);
                    list.add(aabb);
                }
            }
        }
        return list;
    }

}
