package me.planetguy.remaininmotion.network;

import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Collection;

/**
 * Created by Tom on 3/6/2015.
 */
public abstract class PacketSpecterVelocity {
    public static void Dispatch(EntityPlayerMP Player) {
        NBTTagCompound Packet = new NBTTagCompound();
        Packet.setInteger("MotionDuration", RiMConfiguration.CarriageMotion.MotionDuration);
        PacketManager.SendPacketToPlayer(Player, PacketTypes.SpecterVelocity, Packet);
    }

    public static void Handle(NBTTagCompound Packet, World World) {
        RiMConfiguration.CarriageMotion.MotionDuration = Packet.getInteger("MotionDuration");
        TileEntityMotiveSpectre.Velocity = 1 / ((double) RiMConfiguration.CarriageMotion.MotionDuration);
    }
}
