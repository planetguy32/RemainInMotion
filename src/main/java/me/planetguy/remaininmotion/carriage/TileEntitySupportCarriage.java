package me.planetguy.remaininmotion.carriage;

import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.position.BlockRecordSet;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.core.RiMConfiguration;

public class TileEntitySupportCarriage extends TileEntityCarriage {

    // Note this is actually the platform carriage

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
			Side = Directions.values()[Side].opposite().ordinal();
		}

		for (Directions Direction : Directions.values()) {
			SideClosed[Direction.ordinal()] = (Direction.ordinal() != Side);
		}

		Propagate();
	}

	public void FailBecauseOverburdened() throws CarriageMotionException {
		throw (new CarriageMotionException("support carriage exceeds size limit of "
				+ RiMConfiguration.Carriage.MaxSupportBurden + " blocks carried"));
	}

	@Override
	public void fillPackage(CarriagePackage Package) throws CarriageMotionException {
		Directions SupportDirection = getHeadingDirection();

		if (SupportDirection == null) { return; }

		BlockRecordSet ValidColumns = new BlockRecordSet();

		int ValidColumnCheckFactorX = (SupportDirection.deltaX == 0) ? (1) : (0);
		int ValidColumnCheckFactorY = (SupportDirection.deltaY == 0) ? (1) : (0);
		int ValidColumnCheckFactorZ = (SupportDirection.deltaZ == 0) ? (1) : (0);

		BlockRecordSet BlocksChecked = new BlockRecordSet();

		BlockRecordSet CarriagesToCheck = new BlockRecordSet();

		BlockRecordSet BlocksToCheck = new BlockRecordSet();

		BlocksChecked.add(Package.AnchorRecord);

		Package.AddBlock(Package.AnchorRecord);

		CarriagesToCheck.add(Package.AnchorRecord);

		int BlocksCarried = 0;

        boolean terminatedByReversal = false;

		while (CarriagesToCheck.size() > 0) {
			BlockRecord checkingRecord = CarriagesToCheck.pollFirst();

			if (((TileEntitySupportCarriage) checkingRecord.entity).getHeadingDirection() != SupportDirection) { 
				throw (new CarriageMotionException(
					"support carriage must have all open sides in the same direction")); }

			ValidColumns.add(new BlockRecord(checkingRecord.X * ValidColumnCheckFactorX, checkingRecord.Y
					* ValidColumnCheckFactorY, checkingRecord.Z * ValidColumnCheckFactorZ));

			if (Package.MotionDirection == SupportDirection.opposite()) {
				Package.AddPotentialObstruction(checkingRecord.NextInDirection(Package.MotionDirection));
			}

			for (Directions TargetDirection : Directions.values()) {
				if (TargetDirection == SupportDirection.opposite()) {
					continue;
				}

				BlockRecord TargetRecord = checkingRecord.NextInDirection(TargetDirection);

				if (!BlocksChecked.add(TargetRecord)) { //already checked
					continue;
				}

				if (worldObj.isAirBlock(TargetRecord.X, TargetRecord.Y, TargetRecord.Z)) {
					continue;
				}

				TargetRecord.Identify(worldObj);

				if (TargetDirection == SupportDirection && !terminatedByReversal) {
					Package.AddBlock(TargetRecord);

					BlocksToCheck.add(TargetRecord);

					BlocksCarried++;

					if (BlocksCarried > RiMConfiguration.Carriage.MaxSupportBurden) {
						FailBecauseOverburdened();
					}

                    if(TargetRecord.entity != null && TargetRecord.entity instanceof TileEntitySupportCarriage) {
                        if(((TileEntityCarriage)TargetRecord.entity).treatSideAsClosed(SupportDirection.oppositeOrdinal)) {
                            terminatedByReversal = true;
                        }
                    }

					continue;
				}

				if (Package.MatchesCarriageType(TargetRecord)) {
					if(Package.AddBlock(TargetRecord)) {
						CarriagesToCheck.add(TargetRecord);
					}
					continue;
				}

				if (TargetDirection == Package.MotionDirection) {
					Package.AddPotentialObstruction(TargetRecord);
				}
			}
		}

        terminatedByReversal = false;

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

				TargetRecord.Identify(worldObj);

                if(SupportDirection == TargetDirection && terminatedByReversal) continue;

                if(TargetRecord.entity != null && TargetRecord.entity instanceof TileEntitySupportCarriage) {
                    if(!((TileEntityCarriage)TargetRecord.entity).treatSideAsClosed(SupportDirection.oppositeOrdinal)) {
                        terminatedByReversal = true;
                    }
                }

				if(!Package.AddBlock(TargetRecord))
					continue;

				BlocksToCheck.add(TargetRecord);

				if (++BlocksCarried > RiMConfiguration.Carriage.MaxSupportBurden) {
					FailBecauseOverburdened();
				}
			}
		}
	}
	
	public Directions getHeadingDirection() {
		Directions heading = null;
		for (Directions Direction : Directions.values()) {
			if (!SideClosed[Direction.ordinal()]) {
				heading = Direction;
				break;
			}
		}
		return heading;
	}
}
