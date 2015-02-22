package me.planetguy.remaininmotion.util;

import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;

public abstract class GameUtil {
	public static MinecraftServer GetServer() {
		return (FMLCommonHandler.instance().getMinecraftServerInstance());
	}
}
