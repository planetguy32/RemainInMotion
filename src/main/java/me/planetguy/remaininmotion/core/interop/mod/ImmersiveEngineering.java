package me.planetguy.remaininmotion.core.interop.mod;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.RotationHelper;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityBucketWheel;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockPart;
import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.transformations.Rotator;
import me.planetguy.remaininmotion.api.event.BlockPreMoveEvent;
import me.planetguy.remaininmotion.api.event.RotatingTEPreUnpackEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ImmersiveEngineering {
	
	@SubscribeEvent
	public void onPreMove(BlockPreMoveEvent e){
		TileEntity te=e.location.entity();
		if(te instanceof TileEntityMultiblockPart) {
			((TileEntityMultiblockPart) te).formed=false;
		}
	}

}
