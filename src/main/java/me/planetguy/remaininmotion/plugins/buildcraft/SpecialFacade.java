package me.planetguy.remaininmotion.plugins.buildcraft;

import io.netty.buffer.ByteBuf;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.pluggable.IFacadePluggable;
import buildcraft.api.transport.pluggable.IPipePluggableRenderer;
import buildcraft.api.transport.pluggable.PipePluggable;

public class SpecialFacade implements IFacadePluggable{

	@Override
	public Block getCurrentBlock() {
		return RIMBlocks.Carriage;
	}

	@Override
	public int getCurrentMetadata() {
		return 0;
	}

	@Override
	public boolean isTransparent() {
		return false;
	}

	@Override
	public boolean isHollow() {
		return false;
	}
	
}
