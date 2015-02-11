package me.planetguy.remaininmotion.carriage;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.BlockRecordSet;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.core.RiMConfiguration;

public class TileEntitySupportCarriage extends TileEntityCarriage {
	public TileEntitySupportCarriage() {
		for (Directions Direction : Directions.values()) {
			if (Direction != Directions.PosY) {
				SideClosed[Direction.ordinal()] = true;
			}
		}
	}

	@Override
	public void ToggleSide(int Side, boolean Sneaking) {
		if (Sneaking) {
			Side = Directions.values()[Side].Opposite().ordinal();
		}

		for (Directions Direction : Directions.values()) {
			SideClosed[Direction.ordinal()] = (Direction.ordinal() != Side);
		}

		Propagate();
	}

	public void FailBecauseOverburdened() throws CarriageMotionException {
		throw (new CarriageMotionException("support carriage exceeds maximum burden of "
				+ RiMConfiguration.Carriage.MaxSupportBurden + " blocks carried"));
	}

	@Override
	public void fillPackage(CarriagePackage Package) throws CarriageMotionException {
		Directions SupportDirection = null;

		for (Directions Direction : Directions.values()) {
			if (!SideClosed[Direction.ordinal()]) {
				SupportDirection = Direction;

				break;
			}
		}

		if (SupportDirection == null) { return; }

		BlockRecordSet ValidColumns = new BlockRecordSet();

		int ValidColumnCheckFactorX = (SupportDirection.DeltaX == 0) ? (1) : (0);
		int ValidColumnCheckFactorY = (SupportDirection.DeltaY == 0) ? (1) : (0);
		int ValidColumnCheckFactorZ = (SupportDirection.DeltaZ == 0) ? (1) : (0);

		BlockRecordSet BlocksChecked = new BlockRecordSet();

		BlockRecordSet CarriagesToCheck = new BlockRecordSet();

		BlockRecordSet BlocksToCheck = new BlockRecordSet();

		BlocksChecked.add(Package.AnchorRecord);

		Package.AddBlock(Package.AnchorRecord);

		CarriagesToCheck.add(Package.AnchorRecord);

		int BlocksCarried = 0;

		while (CarriagesToCheck.size() > 0) {
			BlockRecord CarriageRecord = CarriagesToCheck.pollFirst();

			if (((TileEntitySupportCarriage) CarriageRecord.Entity).SideClosed[SupportDirection.ordinal()]) { throw (new CarriageMotionException(
					"support carriage must have all open sides in the same direction")); }

			ValidColumns.add(new BlockRecord(CarriageRecord.X * ValidColumnCheckFactorX, CarriageRecord.Y
					* ValidColumnCheckFactorY, CarriageRecord.Z * ValidColumnCheckFactorZ));

			if (Package.MotionDirection == SupportDirection.Opposite()) {
				Package.AddPotentialObstruction(CarriageRecord.NextInDirection(Package.MotionDirection));
			}

			for (Directions TargetDirection : Directions.values()) {
				if (TargetDirection == SupportDirection.Opposite()) {
					continue;
				}

				BlockRecord TargetRecord = CarriageRecord.NextInDirection(TargetDirection);

				if (!BlocksChecked.add(TargetRecord)) {
					continue;
				}

				if (worldObj.isAirBlock(TargetRecord.X, TargetRecord.Y, TargetRecord.Z)) {
					continue;
				}

				TargetRecord.Identify(worldObj);

				if (TargetDirection == SupportDirection) {
					Package.AddBlock(TargetRecord);

					BlocksToCheck.add(TargetRecord);

					BlocksCarried++;

					if (BlocksCarried > RiMConfiguration.Carriage.MaxSupportBurden) {
						FailBecauseOverburdened();
					}

					continue;
				}

				if (Package.MatchesCarriageType(TargetRecord)) {
					Package.AddBlock(TargetRecord);

					CarriagesToCheck.add(TargetRecord);

					continue;
				}

				if (TargetDirection == Package.MotionDirection) {
					Package.AddPotentialObstruction(TargetRecord);
				}
			}
		}

		while (BlocksToCheck.size() > 0) {
			BlockRecord BlockRecord = BlocksToCheck.pollFirst();

			for (Directions TargetDirection : Directions.values()) {
				BlockRecord TargetRecord = BlockRecord.NextInDirection(TargetDirection);

				{
					BlockRecord TargetRecordCheck = new BlockRecord(TargetRecord.X * ValidColumnCheckFactorX,
							TargetRecord.Y * ValidColumnCheckFactorY, TargetRecord.Z * ValidColumnCheckFactorZ);

					if (!ValidColumns.contains(TargetRecordCheck)) {
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

				BlocksToCheck.add(TargetRecord);

				BlocksCarried++;

				if (BlocksCarried > RiMConfiguration.Carriage.MaxSupportBurden) {
					FailBecauseOverburdened();
				}
			}
		}
	}
}
