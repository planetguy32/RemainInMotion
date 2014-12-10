package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.Directions;

public class Rotator {
	
	public static void rotate(BlockRecord pivot, Directions clockwiseFace, BlockRecord pos) {
		double offset=0;
		double px=pivot.X+offset;
		double py=pivot.Y+offset;
		double pz=pivot.Z+offset;
		Matrix coordsMatrixNew=new Matrix(new double[][] {
				{pos.X-px},
				{pos.Y-py},
				{pos.Z-pz}});
		Matrix rotation=Matrix.ccwRotMatrices[clockwiseFace.ordinal()];
		Matrix newCoords=rotation.crossProduct(coordsMatrixNew);
		pos.X=(int) (newCoords.matrix[0][0]+px);
		pos.Y=(int) (newCoords.matrix[1][0]+py);
		pos.Z=(int) (newCoords.matrix[2][0]+pz);
	}
	
	public static void main(String[] args) {
		BlockRecord pivot=new BlockRecord(1,1,1);
		BlockRecord pos=new BlockRecord(1,2,2);
		System.out.println(pos);
		rotate(pivot, Directions.PosY, pos);
		rotate(pivot, Directions.PosY, pos);
		System.out.println(pos);
		rotate(pivot, Directions.PosY, pos);
		rotate(pivot, Directions.PosY, pos);
		System.out.println(pos);
		
		System.out.println();
		pos=new BlockRecord(1,2,1);
		
		System.out.println(pos);
		rotate(pivot, Directions.PosY, pos);
		rotate(pivot, Directions.PosY, pos);
		System.out.println(pos);
		rotate(pivot, Directions.PosY, pos);
		rotate(pivot, Directions.PosY, pos);
		System.out.println(pos);
	}

}
