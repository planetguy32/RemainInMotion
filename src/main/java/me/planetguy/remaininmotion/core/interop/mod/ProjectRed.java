package me.planetguy.remaininmotion.core.interop.mod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.event.RotatingTEPreUnpackEvent;
import me.planetguy.remaininmotion.util.transformations.Rotator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class ProjectRed {

    @SubscribeEvent
    public void onFMPBlockRotated(RotatingTEPreUnpackEvent e) {
        try {
            NBTTagCompound tag=e.location.entityTag();
            if(tag != null && tag.getString("id").equals("savedMultipart")) {
                for(int i=0; i<tag.getTagList("parts", 10).tagCount(); i++) {
                    NBTTagCompound part=tag.getTagList("parts", 10).getCompoundTagAt(i);
                    if(part.getString("id").startsWith("pr")) {
                    	transform(part, e.axis);
                    }
                }
            }
        }catch(Exception ignored) {}
    }

	private void transform(NBTTagCompound part, ForgeDirection axis) {
		if(part.hasKey("side")) {
            int side = part.getByte("side");
            side = Rotator.newSide(side, axis);
            part.setByte("side", (byte)side);
		} else if(part.hasKey("orient")) {
 			int orient=part.getByte("orient");
			try{
				orient=Rotator.newLogicTileOrientation(orient, axis);
				if(orient!=-1)
					part.setByte("orient", (byte) orient);
				else
					Debug.mark();
			}catch(ArrayIndexOutOfBoundsException e){
				
			}
		}
		
	}

}
