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

public class NBTPacketUp extends NBTPacket implements IMessage, IMessageHandler<NBTPacketUp, IMessage> {

	@Deprecated
	// Only for use by Netty
	public NBTPacketUp() {}

	public NBTPacketUp(TypesUp type, NBTTagCompound body) {
		super(type.ordinal(), body);
	}

	@Override
	public IMessage onMessage(NBTPacketUp message, MessageContext ctx) {
		if(message.type==TypesUp.RECONFIGURE.ordinal())
			PacketCarriageUpdate.receive(message.body, ctx.getServerHandler().playerEntity);
		return null;
	}

}
