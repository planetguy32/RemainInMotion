package me.planetguy.remaininmotion.network;

import java.io.IOException;
import java.util.EnumMap;

import me.planetguy.remaininmotion.core.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<RiMPacket>{

	public static EnumMap<Side, FMLEmbeddedChannel> channels;

	static{
		channels = NetworkRegistry.INSTANCE.newChannel(Mod.Handle, new ChannelHandler());	
	}

	public ChannelHandler() {
		this.addDiscriminator(31, RiMPacket.class);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, RiMPacket packet, ByteBuf data) throws Exception {
		packet.writeBytes(data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data, RiMPacket packet) {
		try {
			packet.readBytes(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public static void BroadcastPacketFromBlock ( int X , int Y , int Z , int Dimension , Enum Type , net . minecraft . nbt . NBTTagCompound Body )
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeOutbound(new RiMPacket(Body));
	}
	
	public static void sendPacketToPlayer(EntityPlayerMP player , PacketTypes pt , NBTTagCompound tag){
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channels.get(Side.SERVER).writeOutbound(new RiMPacket(tag));
	}
	

}
