package me.planetguy.remaininmotion.network;

import java.util.EnumMap;

import me.planetguy.remaininmotion.core.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<PacketNBT>{
	
	public static SimpleNetworkWrapper channels;
	
	static{
		 channels=NetworkRegistry.INSTANCE.newSimpleChannel(Mod.Handle);
	}
	
	public ChannelHandler() {
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, PacketNBT packet, ByteBuf data) throws Exception {
		packet.writeBytes(data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data, PacketNBT packet) {
		packet.readBytes(data);
		switch (FMLCommonHandler.instance().getEffectiveSide()) {
		case CLIENT:
			packet.executeClient(Minecraft.getMinecraft().thePlayer);
			break;
		case SERVER:
			INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
			packet.executeServer(((NetHandlerPlayServer) netHandler).playerEntity);
			break;
		}
	}

}
