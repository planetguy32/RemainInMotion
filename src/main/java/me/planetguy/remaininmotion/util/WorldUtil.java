package me.planetguy.remaininmotion.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class WorldUtil {
	public static int	OverworldId	= 0;
	public static int	NetherId	= -1;
	public static int	EndId		= 1;

	public static WorldServer GetWorld(int Dimension) {
		return (GameUtil.GetServer().worldServerForDimension(Dimension));
	}

	public static void ClearBlock(World world, int X, int Y, int Z) {
		SetBlock(world, X, Y, Z, Blocks.air, 0);
	}

	public static void SetBlock(World world, int X, int Y, int Z, Block spectre, int Meta) {
		world.setBlock(X, Y, Z, spectre, Meta, 0x03);

	}
}
