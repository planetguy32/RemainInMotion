package me.planetguy.lib.multiblock;

import java.util.List;
import java.util.ArrayList;

// +x = east
// +y = up
// +z = south

/**
 * Utility for assembling cuboid multiblocks.
 *
 */

public enum EnumMultiblock{

	TOP_NORTH_EAST(38, new int[]{-1, -1, 1}),
	TOP_NORTH_MID (3,  new int[]{ 0, -1, 1}),
	TOP_NORTH_WEST(37, new int[]{ 1, -1, 1}),

	TOP_MID_EAST  (12, new int[]{-1, -1, 0}),
	TOP_MID_WEST  (12, new int[]{ 1, -1, 0}),
	
	TOP_SOUTH_EAST(42, new int[]{-1, -1, -1}),
	TOP_SOUTH_MID (3,  new int[]{ 0, -1, -1}),
	TOP_SOUTH_WEST(41, new int[]{ 1, -1, -1}),


	MID_NORTH_EAST(48, new int[]{-1, 0,  1}),
	MID_NORTH_WEST(48, new int[]{ 1, 0,  1}),
	MID_SOUTH_EAST(48, new int[]{-1, 0, -1}),
	MID_SOUTH_WEST(48, new int[]{ 1, 0, -1}),
	

	BOTTOM_NORTH_EAST(38, new int[]{-1, 1, 1}),
	BOTTOM_NORTH_MID (3,  new int[]{ 0, 1, 1}),
	BOTTOM_NORTH_WEST(37, new int[]{ 1, 1, 1}),

	BOTTOM_MID_EAST  (12, new int[]{-1, 1, 0}),
	BOTTOM_MID_WEST  (12, new int[]{ 1, 1, 0}),
	
	BOTTOM_SOUTH_EAST(42, new int[]{-1, 1, -1}),
	BOTTOM_SOUTH_MID (3,  new int[]{ 0, 1, -1}),
	BOTTOM_SOUTH_WEST(41, new int[]{ 1, 1, -1});



	private final int neighboursRequiredDUNSWE;
	public final int[] offsetsToInteriorBlock;
	
	EnumMultiblock(int neighboursRequiredDUNSWE, int[] offsetsToInteriorBlock){
		this.neighboursRequiredDUNSWE=neighboursRequiredDUNSWE;
		this.offsetsToInteriorBlock=offsetsToInteriorBlock;
	}

	/*
	 * Lists the possible positions in a cuboid frame that this block occupies, based on the neighbours present.
	 *
	 * dunsweBits is an integer with the bits set if neighbouring blocks are valid frame parts. Bits should be set in the order down, up, north, south, west, east.
	 */
	public static List<EnumMultiblock> getPossiblePositions(int dunsweBits){
		List<EnumMultiblock> multiblocks=new ArrayList<EnumMultiblock>();
		for(EnumMultiblock multiblock:EnumMultiblock.values()){
			if((multiblock.neighboursRequiredDUNSWE & dunsweBits) == multiblock.neighboursRequiredDUNSWE)
				multiblocks.add(multiblock);
		}
		return multiblocks;
	}

}
