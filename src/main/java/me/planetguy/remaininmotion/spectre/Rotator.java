package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.Directions;

public class Rotator {
	
	public static void rotate(BlockRecord pivot, Directions clockwiseFace, BlockRecord pos) {
		Matrix coordsMatrixNew=new Matrix(new int[][] {
				{pos.X-pivot.X},
				{pos.Y-pivot.Y},
				{pos.Z-pivot.Z}});
		Matrix rotation=Matrix.clockwiseRotMatrices[clockwiseFace.ordinal()];
		Matrix newCoords=rotation.crossProduct(coordsMatrixNew);
		pos.X=newCoords.matrix[0][0];
		pos.Y=newCoords.matrix[1][0];
		pos.Z=newCoords.matrix[2][0];
	}
	
	public static void main(String[] args) {
		Matrix A=new Matrix(new int[][] {{1,2,3}});
		Matrix B=new Matrix(new int[][] {{5},{7},{11}});
		System.out.println(A.crossProduct(B));
		System.out.println(B.crossProduct(A));
	}

}
