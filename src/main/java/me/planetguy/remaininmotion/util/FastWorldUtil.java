package me.planetguy.remaininmotion.util;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class FastWorldUtil {
	
	private static Compare compare=new Compare();

	//Simplified for speed by:
	//		- assuming we won't use it to add a TileEntity to the same place repeatedly -> no iterating over all TEs
	//		- not checking World.field_147481_N (we know we're called from a TE's update())
	public static void unsafeAddSpectre(World w, int x, int y, int z, TileEntity te) {
		List<TileEntity> l=extractAddedTEList(w);
		l.add(te);
		Chunk chunk = w.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null)
        {
            chunk.func_150812_a(x & 15, y, z & 15, te);
        }
	}
	
	
	public static final class Compare implements Comparator<TileEntity> {
		@Override
		public int compare(TileEntity te1, TileEntity te2) {
			return hash(te1)-hash(te2);
		}

		public int hash(TileEntity te) {
			return te.xCoord ^ te.zCoord*265443576 ^ (te.yCoord << 256)*283;
		}
	}
	
	private static Field f;
	
	static {
		try {
			f=World.class.getDeclaredField("addedTileEntityList");
			f.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static List<TileEntity> extractAddedTEList(World w){
		try {
			return (List<TileEntity>) f.get(w);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
