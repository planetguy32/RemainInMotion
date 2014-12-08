package me.planetguy.remaininmotion.spectre;

import scala.actors.threadpool.Arrays;

public class Matrix {
	
	public double[][] matrix;
	
	public static final Matrix[] ccwRotMatrices=new Matrix[] {
		new Matrix(new double[][]{ //-y
				{0,0,1},
				{0,1,0},
				{-1,0,0}}),
		new Matrix(new double[][]{ //+y
				{0,0,-1},
				{0,1,0},
				{1,0,0}}),
		new Matrix(new double[][]{ //-z
				{0,-1,0},
				{1,0,0},
				{0,0,1}}),
		new Matrix(new double[][]{ //+z
				{0,1,0},
				{-1,0,0},
				{0,0,1}}),		
		new Matrix(new double[][]{ //-x
				{1,0,0},
				{0,0,-1},
				{0,1,0}}),		
		new Matrix(new double[][]{ //+x
				{1,0,0},
				{0,0,1},
				{0,-1,0}}),		
	};
	
	public Matrix(double[][] coords) {
		this.matrix=coords;
	}
	
	public Matrix crossProduct(Matrix m) {
		double[][] newMatrix=new double[this.matrix.length][m.matrix[0].length];
		for(int x=0; x<newMatrix.length; x++) {
			for(int y=0; y<newMatrix[0].length; y++) {
				double cellCounter=0;
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
		for(double[] doubles:this.matrix) {
			s+=Arrays.toString(doubles)+"\n";
		}
		return s;
	}

}
