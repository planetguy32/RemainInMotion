package me.planetguy.remaininmotion.network;

import java.util.List;

import me.planetguy.remaininmotion.PacketTypes;
import me.planetguy.remaininmotion.core.Mod;
import me.planetguy.util.Debug;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketManager {

	public static final SimpleNetworkWrapper wrapper=NetworkRegistry.INSTANCE.newSimpleChannel(Mod.Channel);
	
	public static void init(){
		MinecraftForge.EVENT_BUS.register(new PacketManager());
		Debug.dbg("Registered packet manager!" );
		wrapper.registerMessage(Message.class, Message.class, 0, Side.CLIENT);
	}
	
	public static void BroadcastPacketFromBlock ( int X , int Y , int Z , World world , PacketTypes pt , NBTTagCompound tag )
	{
		Debug.dbg("Packet broadcast: "+pt);
		if(!world.isRemote){
			for(EntityPlayerMP player:((List<EntityPlayerMP>)world.playerEntities)){
				send_do(player, new Message(pt, tag));
			}

		}
	}

	public static void SendPacketToPlayer(EntityPlayerMP player , PacketTypes pt , NBTTagCompound tag){
		Debug.dbg("Packet sent to: "+player.getDisplayName());
		send_do(player, new Message(pt, tag));
	}

	public static void send_do(EntityPlayerMP player, Message msg){
		wrapper.sendTo(msg, player);
	}
}