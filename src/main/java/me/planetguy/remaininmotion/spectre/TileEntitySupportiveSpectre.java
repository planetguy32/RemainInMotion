package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.base.TileEntityRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumSkyBlock;

public class TileEntitySupportiveSpectre extends TileEntityRiM {

    private int lightValue = 0;
    private int lightOpacity = 255;

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
    }

    public int getLightValue()
    {
        return lightValue;
    }

    public int getLightOpacity()
    {
        return lightOpacity;
    }
}
