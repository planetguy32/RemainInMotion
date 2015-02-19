package me.planetguy.remaininmotion;

public class BlockPosition implements Comparable<BlockPosition> {
	public int	X;
	public int	Y;
	public int	Z;

	public int	Dimension;

	public BlockPosition(int X, int Y, int Z, int Dimension) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;

		this.Dimension = Dimension;
	}

	@Override
	public String toString() {
		return (X + "|" + Y + "|" + Z + "|" + Dimension);
	}

	@Override
	public boolean equals(Object Object) {
		if (!(Object instanceof BlockPosition)) { return (false); }

		BlockPosition Target = (BlockPosition) Object;

		if (Target.X != X) { return (false); }

		if (Target.Y != Y) { return (false); }

		if (Target.Z != Z) { return (false); }

		if (Target.Dimension != Dimension) { return (false); }

		return (true);
	}

	@Override
	public int compareTo(BlockPosition Target) {
		int Result = X - Target.X;

		if (Result == 0) {
			Result = Y - Target.Y;

			if (Result == 0) {
				Result = Z - Target.Z;

				if (Result == 0) {
					Result = Dimension - Target.Dimension;
				}
			}
		}

		return (Result);
	}
}
