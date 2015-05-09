package me.planetguy.remaininmotion.network;

import java.util.List;

import me.planetguy.lib.prefab.GuiHandlerPrefab;
import me.planetguy.remaininmotion.core.ModRiM;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketManager {

	public static final SimpleNetworkWrapper	wrapper	= NetworkRegistry.INSTANCE.newSimpleChannel(ModRiM.Channel);

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new PacketManager());
		wrapper.registerMessage(NBTPacketDown.class, NBTPacketDown.class, 0, Side.CLIENT);
		wrapper.registerMessage(NBTPacketUp.class, NBTPacketUp.class, 1, Side.SERVER);
		
	}

	public static void BroadcastPacketFromBlock(int X, int Y, int Z, World world, TypesDown pt, NBTTagCompound tag) {
		if (!world.isRemote) {
			for (EntityPlayerMP player : ((List<EntityPlayerMP>) world.playerEntities)) {
				send_do(player, new NBTPacketDown(pt, tag));
			}
		}
	}

	public static void SendPacketToPlayer(EntityPlayerMP player, TypesDown pt, NBTTagCompound tag) {
		send_do(player, new NBTPacketDown(pt, tag));
	}

	public static void send_do(EntityPlayerMP player, NBTPacketDown msg) {
		wrapper.sendTo(msg, player);
	}
	
	public static void sendPacketToServer(TypesUp pt, NBTTagCompound tag){
		wrapper.sendToServer(new NBTPacketUp(pt, tag));
	}
	
}