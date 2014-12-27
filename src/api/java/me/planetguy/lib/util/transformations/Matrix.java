package me.planetguy.lib.util.transformations;

import java.util.Arrays;

public class Matrix {

	public double[][]	matrix;

	public Matrix(double[][] coords) {
		matrix = coords;
	}

	public Matrix crossProduct(Matrix m) {
		double[][] newMatrix = new double[matrix.length][m.matrix[0].length];
		for (int x = 0; x < newMatrix.length; x++) {
			for (int y = 0; y < newMatrix[0].length; y++) {
				double cellCounter = 0;
				for (int k = 0; k < m.matrix.length; k++) {
					cellCounter += matrix[x][k] * m.matrix[k][y];
				}
				newMatrix[x][y] = cellCounter;
			}
		}
		return new Matrix(newMatrix);
	}

	@Override
	public String toString() {
		String s = "";
		for (double[] doubles : matrix) {
			s += Arrays.toString(doubles) + "\n";
		}
		return s;
	}

	public Matrix scalarMultiply(double d) {
		Matrix m = new Matrix(copy(matrix));
		for (double[] dbls : matrix) {
			for (double dbl : dbls) {
				dbl *= d;
			}
		}
		return m;
	}

	public static double[][] copy(double[][] dbls) {
		double[][] copy = new double[dbls.length][dbls[0].length];
		for (int x = 0; x < dbls.length; x++) {
			for (int y = 0; y < dbls[0].length; y++) {
				copy[x][y] = dbls[x][y];
			}
		}
		return copy;
	}
}
