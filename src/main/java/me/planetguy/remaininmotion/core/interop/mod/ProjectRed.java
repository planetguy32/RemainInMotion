package me.planetguy.remaininmotion.core.interop.mod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.planetguy.remaininmotion.api.event.RotatingTEPreUnpackEvent;
import me.planetguy.remaininmotion.util.transformations.Rotator;
import net.minecraft.nbt.NBTTagCompound;

public class ProjectRed {

    @SubscribeEvent
    public void onFMPBlockRotated(RotatingTEPreUnpackEvent e) {
        try {
            NBTTagCompound tag=e.location.entityTag();
            if(tag != null && tag.getString("id").equals("savedMultipart")) {
                for(int i=0; i<tag.getTagList("parts", 10).tagCount(); i++) {
                    NBTTagCompound part=tag.getTagList("parts", 10).getCompoundTagAt(i);
                    if(part.getString("id").startsWith("pr") && part.hasKey("side")) {
                        int side = part.getByte("side");
                        side = Rotator.newSide(side, e.axis);
                        part.setByte("side", (byte)side);
                    }
                }
            }
        }catch(Exception ignored) {}
    }

}
