package me.planetguy.remaininmotion.spectre;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TeleportativeSpectreTeleporter extends Teleporter {
	public TeleportativeSpectreTeleporter(World World) {
		super((WorldServer) World);
	}

	@Override
	public void placeInPortal(Entity Entity, double X, double Y, double Z, float Yaw) {}
}
