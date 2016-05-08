package me.planetguy.remaininmotion.core.interop.mod;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.handler.MultipartProxy_serverImpl;
import uk.co.qmunity.lib.part.compat.fmp.FMPPartFactory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.Reflection;
import me.planetguy.lib.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.api.event.BlockSelectForMoveEvent;
import me.planetguy.remaininmotion.api.event.RotatingTEPreUnpackEvent;
import me.planetguy.remaininmotion.plugins.fmp.PartCarriageFMP;
import me.planetguy.remaininmotion.util.position.BlockRecord;

public class QmunityMultipart implements Runnable {
	
	private static final String qchType="QmunityConversionHelper";

	FMPPartFactory factory=new FMPPartFactory();
	
	/* Run at init!!! */
	public void run(){
		MultiPartRegistry.registerParts(new IPartFactory() {
			@Override
			public TMultiPart createPart(String arg0, boolean arg1) {
				if (arg0.equals(qchType)) 
					return new QmunityConversionHelper();
				else throw new RuntimeException("Requested string "+arg0);
			}

		}, new String[] { qchType });
	}
	
	@SubscribeEvent
	public void add(BlockSelectForMoveEvent e){
		//Force to FMP
		//if(!e.location.world().isRemote)
			if(e.location.entity() instanceof uk.co.qmunity.lib.tile.TileMultipart){
				int x=e.location.x();
				int y=e.location.y();
				int z=e.location.z();

				BlockCoord coord=new BlockCoord(x,y,z);
				
				TMultiPart part=new QmunityConversionHelper();

				TileMultipart.addPart(e.location.world(), coord, part);
				TileMultipart.getTile(e.location.world(), coord).remPart(part);

				BlockRecord rloc=(BlockRecord) e.location;
				rloc.Identify(e.location.world()); //flush state - this is a non-API call
				rloc.entityRecord=new NBTTagCompound();
				TileMultipart.getTile(e.location.world(), coord).writeToNBT(rloc.entityRecord);
				ForgeMultipart.saveMultipartTick(rloc.entity(), rloc.entityTag());
			}
	}
	
	/*
	@SubscribeEvent
	public void transform(RotatingTEPreUnpackEvent e){
        try {
            NBTTagCompound tag=e.location.entityTag();
            if(tag != null && tag.getString("id").equals("savedMultipart")) {
            	NBTTagList parts=(NBTTagList) tag.getTagList("parts", 10);
                for(int i=0; i<parts.tagCount(); i++) {
                    NBTTagCompound part=parts.getCompoundTagAt(i);
                    
                    if(TODO part.getString("id").startsWith("pr")) {
                    	transform(part, e.axis);
                    }
                }
            }
        }catch(Exception ignored) {}
	}
	
	private void transform(NBTTagCompound part, ForgeDirection axis) {
		//TODO
		//AND gate
		//{x:293,parts:[0:{data:{face:0,components:{0:{state:1b},1:{state:1b},2:{state:1b},3:{state:0b},4:{power:-1b,enabled:1b},5:{power:0b,enabled:1b},6:{power:0b,enabled:1b},7:{power:0b,enabled:1b},8:{}},rotation:2,buffer:{0_0:0b,0_1:0b,1_0:0b,1_1:0b,2_0:0b,2_1:0b,3_0:0b,3_1:0b,4_0:0b,4_1:0b},connections:{TOP:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:4},LEFT:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:3},RIGHT:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:1},FRONT:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:0},BACK:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:2},BOTTOM:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:5}}},id:"0966688b-d390-4672-ae54-4c31fb8af0b1",type:"and"}],y:64,z:211,id:"qmunitylib.blocks.multipart"}
		//{x:293,parts:[0:{data:{face:0,components:{0:{state:1b},1:{state:1b},2:{state:1b},3:{state:0b},4:{power:-1b,enabled:1b},5:{power:0b,enabled:1b},6:{power:0b,enabled:1b},7:{power:0b,enabled:1b},8:{}},rotation:2,buffer:{0_0:0b,0_1:0b,1_0:0b,1_1:0b,2_0:0b,2_1:0b,3_0:0b,3_1:0b,4_0:0b,4_1:0b},connections:{TOP:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:4},LEFT:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:3},RIGHT:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:1},FRONT:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:0},BACK:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:2},BOTTOM:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:5}}},id:"0966688b-d390-4672-ae54-4c31fb8af0b1",type:"and"}],y:64,z:211,id:"qmunitylib.blocks.multipart"}
		//{x:293,parts:[0:{data:{face:0,components:{0:{state:1b},1:{state:1b},2:{state:1b},3:{state:0b},4:{power:-1b,enabled:1b},5:{power:0b,enabled:1b},6:{power:0b,enabled:1b},7:{power:0b,enabled:1b},8:{}},rotation:1,buffer:{0_0:0b,0_1:0b,1_0:0b,1_1:0b,2_0:0b,2_1:0b,3_0:0b,3_1:0b,4_0:0b,4_1:0b},connections:{TOP:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:4},LEFT:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:3},RIGHT:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:1},FRONT:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:0},BACK:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:2},BOTTOM:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:5}}},id:"e3ba30e1-8381-4405-9b0b-35225ebc2ea2",type:"and"}],y:64,z:211,id:"qmunitylib.blocks.multipart"}
		//{x:293,parts:[0:{data:{face:0,components:{0:{state:1b},1:{state:1b},2:{state:1b},3:{state:0b},4:{power:-1b,enabled:1b},5:{power:0b,enabled:1b},6:{power:0b,enabled:1b},7:{power:0b,enabled:1b},8:{}},rotation:1,buffer:{0_0:0b,0_1:0b,1_0:0b,1_1:0b,2_0:0b,2_1:0b,3_0:0b,3_1:0b,4_0:0b,4_1:0b},connections:{TOP:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:4},LEFT:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:3},RIGHT:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:1},FRONT:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:0},BACK:{output:0b,input:0b,outputOnly:0b,enabled:1b,direction:2},BOTTOM:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:5}}},id:"e3ba30e1-8381-4405-9b0b-35225ebc2ea2",type:"and"}],y:64,z:211,id:"qmunitylib.blocks.multipart"}

		//Sequencer
		//{x:293,parts:[0:{data:{face:0,components:{0:{state:0b},1:{state:0b},2:{state:1b},3:{state:0b},4:{angle:0.2d,increment:0.00625d,state:1b},5:{}},ticks:0,rotation:0,start:634111L,time:160,connections:{TOP:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:4},LEFT:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:3},RIGHT:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:1},FRONT:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:0},BACK:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:2},BOTTOM:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:5}}},id:"19d0bccb-30e9-44ab-9d51-a65f46e03995",type:"sequencer"}],y:64,z:212,id:"qmunitylib.blocks.multipart"}
		//{x:293,parts:[0:{data:{face:0,components:{0:{state:0b},1:{state:0b},2:{state:1b},3:{state:0b},4:{angle:0.17500000000000002d,increment:0.00625d,state:1b},5:{}},ticks:0,rotation:0,start:634111L,time:160,connections:{TOP:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:4},LEFT:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:3},RIGHT:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:1},FRONT:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:0},BACK:{output:0b,input:0b,outputOnly:1b,enabled:1b,direction:2},BOTTOM:{output:0b,input:0b,outputOnly:0b,enabled:0b,direction:5}}},id:"19d0bccb-30e9-44ab-9d51-a65f46e03995",type:"sequencer"}],y:64,z:212,id:"qmunitylib.blocks.multipart"}

	}
	
	*/

	private final class QmunityConversionHelper extends TMultiPart {
		@Override
		public String getType() {
			return qchType;
		}
	}

}
