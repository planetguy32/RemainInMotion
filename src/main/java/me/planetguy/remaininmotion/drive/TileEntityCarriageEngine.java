package me.planetguy.remaininmotion.drive;

import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCarriageEngine extends TileEntityCarriageDrive {
	@Override
	public CarriagePackage GeneratePackage(TileEntity carriage, Directions CarriageDirection, Directions MotionDirection)
			throws CarriageMotionException {
		CarriagePackage Package = new CarriagePackage(this, carriage, MotionDirection);

		Package.AddBlock(Package.driveRecord);

		if (MotionDirection != CarriageDirection) {
			Package.AddPotentialObstruction(Package.driveRecord.NextInDirection(MotionDirection));
		}

		MultiTypeCarriageUtil.fillPackage(Package, carriage);

		Package.Finalize();

        // Called twice, once in Finalize()
		//removeUsedEnergy(Package);

		return (Package);
	}

	@Override
	public boolean Anchored() {
		return (false);
	}

}
