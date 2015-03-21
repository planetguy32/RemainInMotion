package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.base.TileEntityRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.position.AABBUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;

import java.util.ArrayList;

public class TileEntitySupportiveSpectre extends TileEntityRiM {

    private int lightValue = 0;
    private int lightOpacity = 255;

    public ArrayList<AxisAlignedBB> BB = new ArrayList<AxisAlignedBB>();

    public void setLight(Block block)
    {
        lightValue = block.getLightValue();
        lightOpacity = block.getLightOpacity();
        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord,yCoord,zCoord);
        worldObj.updateLightByType(EnumSkyBlock.Sky, xCoord,yCoord,zCoord);
    }

    @Override
    public void WriteCommonRecord(NBTTagCompound TagCompound) {
        super.WriteCommonRecord(TagCompound);
        TagCompound.setInteger("lightValue", lightValue);
        TagCompound.setInteger("lightOpacity", lightOpacity);

        AABBUtil.writeAABBListToNBT(BB, TagCompound);
    }

    @Override
    public void ReadCommonRecord(NBTTagCompound TagCompound) {
        super.ReadCommonRecord(TagCompound);
        lightValue = TagCompound.getInteger("lightValue");
        lightOpacity = TagCompound.getInteger("lightOpacity");
        if(worldObj != null) {
            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
            worldObj.updateLightByType(EnumSkyBlock.Sky, xCoord, yCoord, zCoord);
        }

        BB = AABBUtil.readAABBsFromNBT(TagCompound);
    }

    public int getLightValue()
    {
        return lightValue;
    }

    public int getLightOpacity()
    {
        return lightOpacity;
    }

    public void setBoundingBox(NBTTagCompound nbt) {
        BB = AABBUtil.readAABBsFromNBT(nbt);
    }


}
