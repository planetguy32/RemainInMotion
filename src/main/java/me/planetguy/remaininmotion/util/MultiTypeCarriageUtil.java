package me.planetguy.remaininmotion.util;

import me.planetguy.remaininmotion.util.Position.BlockRecord;
import me.planetguy.remaininmotion.util.Position.BlockRecordSet;
import me.planetguy.remaininmotion.motion.CarriageMatchers;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.core.Closeables;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.api.ConnectabilityState;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.Moveable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class MultiTypeCarriageUtil {

	public static void fillPackage(CarriagePackage package1, TileEntity carriage) throws CarriageMotionException {
		Moveable m = CarriageMatchers.getMover(package1.AnchorRecord.block, package1.AnchorRecord.Meta,
				package1.AnchorRecord.entity);
		m.fillPackage(package1);
	}
	
	public static void fillFramePackage(CarriagePackage Package, World worldObj) throws CarriageMotionException {
		int seen = 0;

		BlockRecordSet CarriagesToCheck = new BlockRecordSet();

		BlockRecordSet BlocksChecked = new BlockRecordSet();

		BlocksChecked.add(Package.driveRecord);

		BlocksChecked.add(Package.AnchorRecord);

		Package.AddBlock(Package.AnchorRecord);

		CarriagesToCheck.add(Package.AnchorRecord);

		while (CarriagesToCheck.size() > 0) {
			seen++;
			BlockRecord CarriageRecord = CarriagesToCheck.pollFirst();

			for (Directions TargetDirection : Directions.validDirections()) {
				BlockRecord TargetRecord = CarriageRecord.NextInDirection(TargetDirection);

				ICloseable closeable = Closeables.getCloseable(CarriageRecord.entity);

				if (closeable != null) {

					int direction = TargetDirection.ordinal();

					ConnectabilityState state = closeable.isSideClosed(direction);

					if (direction >= 0 && direction < 6 && state == ConnectabilityState.CLOSED) {
						// DEBUG =!= SideClosed");
						if (TargetDirection == Package.MotionDirection) {
							Package.AddPotentialObstruction(TargetRecord);
						}

						continue;
					}
				}

				if (!BlocksChecked.add(TargetRecord)) {
					continue;
				}

				if (worldObj.isAirBlock(TargetRecord.X, TargetRecord.Y, TargetRecord.Z)) {
					continue;
				}

				TargetRecord.Identify(worldObj);

				Package.AddBlock(TargetRecord);

				if (Package.MatchesCarriageType(TargetRecord)) {
					CarriagesToCheck.add(TargetRecord);

					continue;
				}

				if (Package.MotionDirection != null) {
					Package.AddPotentialObstruction(TargetRecord.NextInDirection(Package.MotionDirection));
				}

			}
		}
	}

}
