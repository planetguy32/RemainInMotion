package me.planetguy.remaininmotion.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class NBTPacketDown extends NBTPacket implements IMessage, IMessageHandler<NBTPacketDown, IMessage> {

	@Deprecated
	// Only for use by Netty
	public NBTPacketDown() {}

	public NBTPacketDown(TypesDown type, NBTTagCompound body) {
		super(type.ordinal(), body);
	}
	
	@Override
	public IMessage onMessage(NBTPacketDown message, MessageContext ctx) {
		// Debug.dbg("Handling message "+message.type);
		if (message.type==TypesDown.RENDER.ordinal()) {
			PacketRenderData.receive(message.body, Minecraft.getMinecraft().thePlayer.worldObj);
		} else if (message.type==TypesDown.MULTIPART_PROPAGATION.ordinal()) {
			PacketMultipartSynchronization.receive(message.body, Minecraft.getMinecraft().thePlayer.worldObj);
		}
		return null;
	}

	@Override
	protected NBTSizeTracker createTracker() {
		return NBTSizeTracker.field_152451_a;
	}
}
