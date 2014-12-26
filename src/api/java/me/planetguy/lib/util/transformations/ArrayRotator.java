package me.planetguy.lib.util.transformations;

import static me.planetguy.lib.util.transformations.Rotator.newSide;

import java.util.Arrays;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Rotates arrays. Assumes the arrays have a length of 6.
 *
 */
public class ArrayRotator {

	public static <T> void rotate(T[] array, int axis) {
		T[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(float[] array, int axis) {
		float[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(double[] array, int axis) {
		double[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(boolean[] array, int axis) {
		boolean[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(byte[] array, int axis) {
		byte[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(short[] array, int axis) {
		short[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(int[] array, int axis) {
		int[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(long[] array, int axis) {
		long[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(char[] array, int axis) {
		char[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static <T> void rotate(T[] array, ForgeDirection axis) {
		T[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(float[] array, ForgeDirection axis) {
		float[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(double[] array, ForgeDirection axis) {
		double[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(boolean[] array, ForgeDirection axis) {
		boolean[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(byte[] array, ForgeDirection axis) {
		byte[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(short[] array, ForgeDirection axis) {
		short[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(int[] array, ForgeDirection axis) {
		int[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(long[] array, ForgeDirection axis) {
		long[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

	public static void rotate(char[] array, ForgeDirection axis) {
		char[] temp = Arrays.copyOfRange(array, 0, 6);
		for (int i = 0; i < 6; i++) {
			int newSide = newSide(i, axis);
			array[newSide] = temp[i];
		}
	}

}
