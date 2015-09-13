package me.planetguy.remaininmotion.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

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

	//Simplified for speed by:
	//		- assuming we won't use it to add a TileEntity to the same place repeatedly -> no iterating over all TEs
	//		- not checking World.field_147481_N (we know we're called from a TE's update())
	public static void unsafeAddSpectre(World w, int x, int y, int z, TileEntity te) {
		w.addedTileEntityList.add(te);
		Chunk chunk = w.getChunkFromChunkCoords(x >> 4, z >> 4);
	    if (chunk != null)
	    {
	        chunk.func_150812_a(x & 15, y, z & 15, te);
	    }
	}
}
