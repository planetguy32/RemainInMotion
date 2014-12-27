package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.util.transformations.Matrices;
import me.planetguy.remaininmotion.util.transformations.Matrix;

/**
 * Supports rotation using BlockRecords
 * 
 */
public class RemIMRotator {

	public static void rotateOrthogonal(BlockRecord pivot, Directions clockwiseFace, BlockRecord pos) {
		double offset = 0;
		double px = pivot.X + offset;
		double py = pivot.Y + offset;
		double pz = pivot.Z + offset;
		Matrix coordsMatrixNew = new Matrix(new double[][] { { pos.X - px }, { pos.Y - py }, { pos.Z - pz } });
		Matrix rotation = Matrices.ccwRotMatrices[clockwiseFace.ordinal()];
		Matrix newCoords = rotation.crossProduct(coordsMatrixNew);
		pos.X = (int) (newCoords.matrix[0][0] + px);
		pos.Y = (int) (newCoords.matrix[1][0] + py);
		pos.Z = (int) (newCoords.matrix[2][0] + pz);
	}

	public static void rotatePartial(BlockRecord pivot, Directions clockwiseFace, Matrix pos, double partialAngle) {
		double offset = 0;
		double px = pivot.X + offset;
		double py = pivot.Y + offset;
		double pz = pivot.Z + offset;
		Matrix coordsMatrixNew = new Matrix(new double[][] { { pos.matrix[0][0] - px }, { pos.matrix[1][0] - py },
				{ pos.matrix[2][0] - pz } });
		Matrix rotation = Matrices.fromAxisAndAngle(clockwiseFace.ordinal(), partialAngle);
		Matrix newCoords = rotation.crossProduct(coordsMatrixNew);
		pos.matrix[0][0] = (newCoords.matrix[0][0] + px);
		pos.matrix[1][0] = (newCoords.matrix[1][0] + py);
		pos.matrix[2][0] = (newCoords.matrix[2][0] + pz);
	}

}
