package me.planetguy.remaininmotion.plugins.buildcraft;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.pluggable.IPipePluggableRenderer;
import buildcraft.api.transport.pluggable.PipePluggable;

public class SpecialFacade extends PipePluggable{

	@Override
	public void readFromNBT(NBTTagCompound tag) {
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
	}

	@Override
	public void writeData(ByteBuf data) {
	}

	@Override
	public void readData(ByteBuf data) {
	}

	@Override
	public ItemStack[] getDropItems(IPipeTile pipe) {
		return null;
	}

	@Override
	public boolean isBlocking(IPipeTile pipe, ForgeDirection direction) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(ForgeDirection side) {
		return null;
	}

	@Override
	public IPipePluggableRenderer getRenderer() {
		return null;
	}

}
