package me.planetguy.remaininmotion.util.transformations;

import net.minecraftforge.common.util.ForgeDirection;

public enum Directions {
	NegY(ForgeDirection.DOWN), 
	PosY(ForgeDirection.UP), 
	NegZ(ForgeDirection.NORTH), 
	PosZ(ForgeDirection.SOUTH), 
	NegX(ForgeDirection.WEST), 
	PosX(ForgeDirection.EAST),

	Null(null);

	public int deltaX;
	public int deltaY;
	public int deltaZ;

	public int oppositeOrdinal;

	private Directions(ForgeDirection Direction) {
		if (Direction != null) {
			deltaX = Direction.offsetX;
			deltaY = Direction.offsetY;
			deltaZ = Direction.offsetZ;

			oppositeOrdinal = Direction.getOpposite().ordinal();
		}
	}

	public Directions opposite() {
		return (values()[oppositeOrdinal]);
	}

	static final Directions[] valid=new Directions[] {
		NegY, PosY, NegZ, PosZ, NegX, PosX
	};
	
	public static Directions[] validDirections() {
		return valid;
	}
}
