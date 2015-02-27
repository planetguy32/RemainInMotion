package me.planetguy.remaininmotion.util;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.BlockRecordSet;
import me.planetguy.remaininmotion.CarriageMatchers;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Closeables;
import me.planetguy.remaininmotion.Directions;
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

			for (Directions TargetDirection : Directions.values()) {
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
