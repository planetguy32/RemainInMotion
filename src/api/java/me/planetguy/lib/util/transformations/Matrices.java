package me.planetguy.lib.util.transformations;

public class Matrices {

	public static double sin(double fractionOfCircle) {
		return Math.sin(fractionOfCircle * 2 * Math.PI);
	}

	public static double cos(double fractionOfCircle) {
		return Math.cos(fractionOfCircle * 2 * Math.PI);
	}

	/*
	 * BROKEN
	 */
	public static Matrix fromAxisAndAngle(int axis, double a) {
		switch (axis) {
			case 5:
				a = -a;
			case 4:
				return new Matrix(new double[][] { { 1, 0, 0 }, { 0, cos(a), -sin(a) }, { 0, sin(a), cos(a) } });

			case 3:
				a = -a;
			case 2:
				return new Matrix(new double[][] { { cos(a), -sin(a), 0 }, { sin(a), cos(a), 0 }, { 0, 0, 1 }, });

			case 1:
				a = -a;
			case 0:
				return new Matrix(new double[][] { { cos(a), 0, sin(a) }, { 0, 1, 0 }, { -sin(a), 0, cos(a) }, });
			default:
				return null;
		}
	}

	// Use constants here, not calculated - we don't
	public static final Matrix[]	ccwRotMatrices	= new Matrix[] { new Matrix(new double[][] { // -y
															{ 0, 0, 1 }, { 0, 1, 0 }, { -1, 0, 0 } }),

													new Matrix(new double[][] { // +y
															{ 0, 0, -1 }, { 0, 1, 0 }, { 1, 0, 0 } }),

													new Matrix(new double[][] { // -z
															{ 0, -1, 0 }, { 1, 0, 0 }, { 0, 0, 1 } }),

													new Matrix(new double[][] { // +z
															{ 0, 1, 0 }, { -1, 0, 0 }, { 0, 0, 1 } }),

													new Matrix(new double[][] { // -x
															{ 1, 0, 0 }, { 0, 0, -1 }, { 0, 1, 0 } }),

													new Matrix(new double[][] { // +x
															{ 1, 0, 0 }, { 0, 0, 1 }, { 0, -1, 0 } }), };

}
