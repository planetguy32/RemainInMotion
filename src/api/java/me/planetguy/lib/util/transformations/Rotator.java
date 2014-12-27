package me.planetguy.lib.util.transformations;

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

}
