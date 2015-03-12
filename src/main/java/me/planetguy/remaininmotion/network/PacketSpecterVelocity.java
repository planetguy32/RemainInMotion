package me.planetguy.remaininmotion.network;

import cpw.mods.fml.client.FMLClientHandler;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class PacketSpecterVelocity {
    public static void Dispatch(EntityPlayerMP Player) {
        NBTTagCompound Packet = new NBTTagCompound();
        Packet.setInteger("MotionDuration", RiMConfiguration.CarriageMotion.MotionDuration);
        PacketManager.SendPacketToPlayer(Player, PacketTypes.SpecterVelocity, Packet);
    }

    public static void Handle(NBTTagCompound Packet, World World) {
        if(!World.isRemote) return;
        RiMConfiguration.CarriageMotion.MotionDuration = Packet.getInteger("MotionDuration");
        TileEntityMotiveSpectre.Velocity = 1 / ((double) RiMConfiguration.CarriageMotion.MotionDuration);
    }
}
