package me.planetguy.remaininmotion.network;

import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PacketSpecterVelocity {
	
    public static void send(EntityPlayerMP Player) {
        NBTTagCompound Packet = new NBTTagCompound();
        Packet.setInteger("MotionDuration", RiMConfiguration.CarriageMotion.MotionDuration);
        PacketManager.SendPacketToPlayer(Player, TypesDown.SPECTRE_VELOCITY, Packet);
    }

    public static void receive(NBTTagCompound Packet, World World) {
        if(!World.isRemote) return;
        RiMConfiguration.CarriageMotion.MotionDuration = Packet.getInteger("MotionDuration");
        TileEntityMotiveSpectre.velocity = 1 / ((double) RiMConfiguration.CarriageMotion.MotionDuration);
    }
}
