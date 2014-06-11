package me.planetguy.remaininmotion.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import me.planetguy.remaininmotion.PacketTypes;
import me.planetguy.remaininmotion.core.Mod;
import me.planetguy.remaininmotion.util.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketManager {

	public static void init(){
		MinecraftForge.EVENT_BUS.register(new PacketManager());
		Debug.dbg("Registered packet manager!" );
	}
	
	@SubscribeEvent
	public void onData(ClientCustomPacketEvent evt){
		Debug.dbg("Packet get! "+evt.packet);
		if(evt.packet==null || evt.packet.payload()==null){
			System.out.println("RemIM recieved bad packet!");
			return;
		}
		ByteBuf data=evt.packet.payload();
		System.out.println("RemIM handling packet "+Arrays.toString(data.array()));
		int packetType=data.readInt();
		data.discardReadBytes();
		try {
			NBTTagCompound body=CompressedStreamTools.decompress(data.array());
			switch(PacketTypes.values()[packetType]){
			case MultipartPropagation:
				MultipartPropagationPacket.Handle(body, Minecraft.getMinecraft().thePlayer.worldObj);
			case Render:
				RenderPacket.Handle(body, Minecraft.getMinecraft().thePlayer.worldObj);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void BroadcastPacketFromBlock ( int X , int Y , int Z , World world , Enum Type , NBTTagCompound Body )
	{
		Debug.dbg("Packet broadcast: "+Type);
		if(!world.isRemote){
			ByteBuf data=Unpooled.buffer();
			data.writeInt(Type.ordinal());
			try {
				data.writeBytes(CompressedStreamTools.compress(Body));
				S3FPacketCustomPayload packet=new S3FPacketCustomPayload(Mod.Channel, data);
				for(EntityPlayerMP player:((List<EntityPlayerMP>)world.playerEntities)){
					player.playerNetServerHandler.sendPacket(packet);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void SendPacketToPlayer(EntityPlayerMP player , PacketTypes pt , NBTTagCompound tag){
		Debug.dbg("Packet sent to: "+player.getDisplayName());
		ByteBuf data=Unpooled.buffer();
		data.writeInt(pt.ordinal());
		try {
			data.writeBytes(CompressedStreamTools.compress(tag));
			S3FPacketCustomPayload packet=new S3FPacketCustomPayload(Mod.Channel, data);
			player.playerNetServerHandler.sendPacket(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}