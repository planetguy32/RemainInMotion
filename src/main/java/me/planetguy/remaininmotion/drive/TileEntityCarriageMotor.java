package me.planetguy.remaininmotion.drive;

import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriageObstructionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCarriageMotor extends TileEntityCarriageDrive {
	@Override
	public CarriagePackage GeneratePackage(TileEntity carriage, Directions CarriageDirection, Directions MotionDirection)
			throws CarriageMotionException {
		if (MotionDirection == CarriageDirection) { throw (new CarriageMotionException(
				"motor cannot push carriage away from itself")); }

		if (MotionDirection == CarriageDirection.Opposite()) { throw (new CarriageMotionException(
				"motor cannot pull carriage into itself")); }

		CarriagePackage Package = new CarriagePackage(this, carriage, MotionDirection);

		MultiTypeCarriageUtil.fillPackage(Package, carriage);

		if (Package.Body.contains(Package.DriveRecord)) { throw (new CarriageMotionException(
				"carriage is attempting to move motor")); }

		if (Package.Body.contains(Package.DriveRecord.NextInDirection(MotionDirection.Opposite()))) { throw (new CarriageObstructionException(
				"carriage motion is obstructed by motor", xCoord, yCoord, zCoord)); }

		Package.Finalize();

		return (Package);
	}

	@Override
	public boolean Anchored() {
		return (true);
	}
}
