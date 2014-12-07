package me.planetguy.remaininmotion.spectre;

import scala.actors.threadpool.Arrays;

public class Matrix {
	
	public int[][] matrix;
	
	public static final Matrix[] clockwiseRotMatrices=new Matrix[] {
		new Matrix(new int[][]{ //-y
				{0,0,1},
				{0,1,0},
				{-1,0,0}}),
		new Matrix(new int[][]{ //+y
				{0,0,-1},
				{0,1,0},
				{1,0,0}}),
		new Matrix(new int[][]{ //-z
				{0,-1,0},
				{1,0,0},
				{0,0,1}}),
		new Matrix(new int[][]{ //+z
				{0,1,0},
				{-1,0,0},
				{0,0,1}}),		
		new Matrix(new int[][]{ //-x
				{1,0,0},
				{0,0,-1},
				{0,1,0}}),		
		new Matrix(new int[][]{ //+x
				{1,0,0},
				{0,0,1},
				{0,-1,0}}),		
	};
	
	public Matrix(int[][] coords) {
		this.matrix=coords;
	}
	
	public Matrix crossProduct(Matrix m) {
		int[][] newMatrix=new int[this.matrix.length][m.matrix[0].length];
		for(int x=0; x<newMatrix.length; x++) {
			for(int y=0; y<newMatrix[0].length; y++) {
				int cellCounter=0;
				for(int k=0; k<m.matrix.length; k++) {
					cellCounter += this.matrix[x][k] * m.matrix[k][y];
				}
				newMatrix[x][y]=cellCounter;
			}
		}
		return new Matrix(newMatrix);
	}
	
	public String toString() {
		String s="";
		for(int[] ints:this.matrix) {
			s+=Arrays.toString(ints)+"\n";
		}
		return s;
	}

}
