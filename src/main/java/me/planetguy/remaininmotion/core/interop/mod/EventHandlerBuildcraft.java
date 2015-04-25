package me.planetguy.remaininmotion.core.interop.mod;

import java.util.HashMap;

import buildcraft.transport.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.transformations.Rotator;
import me.planetguy.remaininmotion.api.event.*;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import buildcraft.core.TileBuildCraft;
import buildcraft.factory.BlockTank;
import buildcraft.factory.TileQuarry;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;

public class EventHandlerBuildcraft {

    private HashMap<ChunkCoordinates, TravelerSet> cachedItems = new HashMap<ChunkCoordinates, TravelerSet>();
	
	@SubscribeEvent
	public void onBCMoved(TEPreUnpackEvent e) {
		performBuildcraftPreInit((BlockRecord) e.location, ((TileEntityMotiveSpectre) e.spectre).getOffset((BlockRecord) e.location));
	}
	
	@SubscribeEvent
	public void onBCMoved(TEPostPlaceEvent e) {
		performBuildcraftPostInit((BlockRecord) e.location, ((TileEntityMotiveSpectre) e.spectre).getOffset((BlockRecord) e.location));
	}
	
	@SubscribeEvent
	public void onRotated(RotatingTEPreUnpackEvent e) {
		IBlockPos pos=e.location;
		Block b=pos.world().getBlock(pos.x(), pos.y(), pos.z());
		if(pos.world().isRemote)
			return;
		
		if(b instanceof BlockGenericPipe) {
			NBTTagCompound tag=e.location.entityTag();
			NBTTagCompound[] foundTags=new NBTTagCompound[6];
			for(int i=0; i<6; i++) {
				String tagName="pluggable["+i+"]";
				if(tag.hasKey(tagName)) {
					NBTTagCompound pluggableForSide=tag.getCompoundTag(tagName);
					tag.removeTag(tagName);
					foundTags[Rotator.newSide(i, e.axis)]=pluggableForSide;
				}
			}
			for(int i=0; i<6; i++) {
				if(foundTags[i] != null)
					tag.setTag("pluggable["+i+"]", foundTags[i]);
			}
		}
	}

    @SubscribeEvent
    public void onPreRender(PreRenderDuringMovementEvent event) {
        if(FMLCommonHandler.instance().getSide() == Side.SERVER) return;
        if(event.pass != 0) return;
        if(event.tile != null && event.tile instanceof TileGenericPipe) {
            if(((TileGenericPipe) event.tile).pipe.transport instanceof PipeTransportItems) {
                cachedItems.put(new ChunkCoordinates(event.x, event.y, event.z), ((PipeTransportItems) ((TileGenericPipe) event.tile).pipe.transport).items);
                ((PipeTransportItems) ((TileGenericPipe) event.tile).pipe.transport).items.clear();
            }// else if(((TileGenericPipe) event.tile).pipe.transport instanceof PipeTransportFluids) {

            //}
        }
    }

    @SubscribeEvent
    public void onPostRender(PostRenderDuringMovementEvent event) {
        if(FMLCommonHandler.instance().getSide() == Side.SERVER) return;
        if(event.pass != 0) return;
        if(event.tile != null && event.tile instanceof TileGenericPipe) {
            if(((TileGenericPipe) event.tile).pipe.transport instanceof PipeTransportItems) {
                if(cachedItems != null && !cachedItems.isEmpty()) {
                    ((PipeTransportItems) ((TileGenericPipe) event.tile).pipe.transport).items.addAll(cachedItems.get(new ChunkCoordinates(event.x,event.y,event.z)));
                    cachedItems.remove(new ChunkCoordinates(event.x,event.y,event.z));
                }
            }// else if(((TileGenericPipe) event.tile).pipe.transport instanceof PipeTransportFluids) {

            //}
        }
    }
	
    private void performBuildcraftPreInit(IBlockPos record, int[] offset) {
        if (record.entityTag().hasKey("box")) {
            NBTTagCompound boxNBT = record.entityTag().getCompoundTag("box");
            if (!boxNBT.hasNoTags()) {
                int xMax = boxNBT.getInteger("xMax");
                int xMin = boxNBT.getInteger("xMin");
                int yMax = boxNBT.getInteger("yMax");
                int yMin = boxNBT.getInteger("yMin");
                int zMax = boxNBT.getInteger("zMax");
                int zMin = boxNBT.getInteger("zMin");

                boxNBT.setInteger("xMax", xMax += offset[0]);
                boxNBT.setInteger("xMin", xMin += offset[0]);
                boxNBT.setInteger("yMax", yMax += offset[1]);
                boxNBT.setInteger("yMin", yMin += offset[1]);
                boxNBT.setInteger("zMax", zMax += offset[2]);
                boxNBT.setInteger("zMin", zMin += offset[2]);

            }
        }

        // reset states on filler and mining well (and anything based off it)
        if(record.entityTag().hasKey("done")) {
            record.entityTag().setBoolean("done", false);
        }
        if(record.entityTag().hasKey("digging")) {
            record.entityTag().setBoolean("digging", true);
        }
        // reset quarry
        if (record.entityTag().getString("id").equals("Machine")) {
            record.entityTag().setInteger("targetX", 0);
            record.entityTag().setInteger("targetY", 0);
            record.entityTag().setInteger("targetZ", 0);

            record.entityTag().setDouble("headPosX", 0.0D);
            record.entityTag().setDouble("headPosY", 0.0D);
            record.entityTag().setDouble("headPosZ", 0.0D);
        }

        // traveling items
        // now we don't need to worry about ConcurrentModificationExceptions or the offset not being applied
        if(record.entityTag().hasKey("travelingEntities")) {
            NBTTagList list = record.entityTag().getTagList("travelingEntities", 10);
            for(int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                if(tag != null) {
                    if(tag.hasKey("x")) {
                        tag.setDouble("x", tag.getDouble("x") + offset[0]);
                        tag.setDouble("y", tag.getDouble("y") + offset[1]);
                        tag.setDouble("z", tag.getDouble("z") + offset[2]);
                    }
                }
            }
        }
    }

    private void performBuildcraftPostInit(IBlockPos record, int[] offset) {
        try {

            if (record.entity() instanceof TileGenericPipe) {
                TileGenericPipe tile = (TileGenericPipe) record.entity();
                Pipe pipe = tile.pipe;
                if (!tile.initialized) {
                    tile.initialize(pipe);
                }
            } else if (record.entity() instanceof TileBuildCraft) {

                record.entity().invalidate();
                ((TileBuildCraft)record.entity()).initialize();
                if(record.entity() instanceof TileQuarry) {
                    ((TileQuarry) record.entity()).createUtilsIfNeeded();
                }

            }
        } catch (Throwable Throwable) {
            //Throwable.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onBlockAdded(CancelableOnBlockAddedEvent e) {
        if(e.worldObj.getBlock(e.xCoord,e.yCoord,e.zCoord) instanceof BlockTank) {
        	e.setCanceled(true);
        }
    }

}
