package me.planetguy.remaininmotion.drive;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.api.ISpecialMoveBehavior;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityCarriageAdapter extends TileEntityCarriageEngine implements ISpecialMoveBehavior {

	public boolean	alreadyMoving;

	static {
		// BlacklistManager.blacklistSoft.blacklist(RIMBlocks.CarriageDrive,5);
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) { return; }

		if (Stale) {
			HandleNeighbourBlockChange();
		}

		if (CooldownRemaining > 0) {
			CooldownRemaining--;

			MarkServerRecordDirty();

			return;
		}

		if (Active) { return; }

		if (getSignalDirection() == null) {
			if (Signalled) {
				Signalled = false;

				MarkServerRecordDirty();
			}

			return;
		}

		if (CarriageDirection == null) { return; }

		if (Signalled) {
			if (!Continuous) { return; }
		} else {
			Signalled = true;

			MarkServerRecordDirty();
		}
	}

	@Override
	public void onAdded(CarriagePackage pkg, NBTTagCompound tag) throws CarriageMotionException {

		// icky hack to stop adding already-added adaptors
		StackTraceElement[] e = Thread.currentThread().getStackTrace();
		if (e[10].getClassName().equals(e[12].getClassName())) { return; }

		HandleNeighbourBlockChange();
		BlockRecord record = new BlockRecord(xCoord, yCoord, zCoord);
		record.Identify(worldObj);
		pkg.AddBlock(record);
		if (!alreadyMoving) {
			alreadyMoving = true;
			if (CarriageDirection != null) {
				BlockRecord oldAnchor = pkg.AnchorRecord;
				pkg.AnchorRecord = new BlockRecord(xCoord + CarriageDirection.DeltaX,
						yCoord + CarriageDirection.DeltaY, zCoord + CarriageDirection.DeltaZ);
				pkg.AnchorRecord.Identify(worldObj);
				MultiTypeCarriageUtil.fillPackage(pkg, worldObj.getTileEntity(xCoord + CarriageDirection.DeltaX, yCoord
						+ CarriageDirection.DeltaY, zCoord + CarriageDirection.DeltaZ));
				pkg.AnchorRecord = oldAnchor;

			}
		}
	}

	@Override
	public String toString() {
		return Debug.dump(this);
	}
	
	@Override
	public void HandleToolUsage(int clickedSide, boolean sneaking) {
		for(int i=0; i<SideClosed.length; i++)
			if(sneaking) {
				setSideClosed(i, i != Directions.values()[clickedSide].Opposite);
			}else { 
				setSideClosed(i, i != clickedSide);
			}
		Propagate();
	}	

}