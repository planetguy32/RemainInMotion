package me.planetguy.remaininmotion.util.transformations;

import net.minecraftforge.common.util.ForgeDirection;

public class Rotator {

	public static int[] rotateBlock(int px, int py, int pz, ForgeDirection axis, int yourX, int yourY, int yourZ) {
		Matrix coordsMatrixNew = new Matrix(new double[][] { { yourX - px }, { yourY - py }, { yourZ - pz } });
		Matrix rotation = Matrices.ccwRotMatrices[axis.ordinal()];
		Matrix newCoords = rotation.crossProduct(coordsMatrixNew);
		return new int[] { (int) (newCoords.matrix[0][0] + px), (int) (newCoords.matrix[1][0] + py),
				(int) (newCoords.matrix[2][0] + pz) };
	}

	private static int[][]	CCWRotationSideMap	= new int[][] { { 0, 1, 4, 5, 3, 2 }, { 0, 1, 5, 4, 2, 3 },
			{ 5, 4, 2, 3, 0, 1 }, { 4, 5, 2, 3, 1, 0 }, { 2, 3, 1, 0, 4, 5 }, { 3, 2, 0, 1, 4, 5 } };

	public static int newSide(int side, int axisRotatedAround) {
		return CCWRotationSideMap[axisRotatedAround][side];
	}

	public static int newSide(int side, ForgeDirection axis) {
		return newSide(side, axis.ordinal());
	}

	//Good for Project:Red logic tiles
	public static int newLogicTileOrientation(int orient, ForgeDirection axis) {
		int[][] table=new int[][]{
			//This is the meat of this function.
			//Every odd table is derived from the preceding even table by reversing it (due to the nature of rotation).
				{3, 0, 1, 2, 5, 6, 7, 4,
				16,17,18,19,20,21,22,23,
				12,13,14,15, 8, 9,10,11},
				
				{1, 2, 3, 0, 7, 4, 5, 6,
				20,21,22,23,16,17,18,19,
				 8, 9,10,11,12,13,14,15},
				
				{21,22,23,24,19,16,17,18,
				 11, 8, 9,10,15,12,13,14,
				  1, 2, 3, 0, 7, 4, 5, 6},
				
				//TODO
				{19,16,17,18,21,22,23,20,
				  9,10,11, 8,13,14,15,12,
				  5, 6, 7, 4, 0, 1, 2, 3
				},
				
				{10,11, 8, 9,14,15,12,13,
				  4, 5, 6, 7, 0, 1, 2, 3,
				 19,18,17,16,21,22,23,20
				},
				
				{12,13,14,15, 8, 9,10,11,
				  2, 3, 0, 1, 6, 7, 4, 5,
				 19,18,17,16,23,20,21,22
				}
		};
		return table[axis.ordinal()][orient];
	}

}
