package me.planetguy.remaininmotion.drive;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.api.ISpecialMoveBehavior;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCarriageAdapter extends TileEntityCarriageEngine implements ISpecialMoveBehavior {

	public boolean	alreadyMoving;

	static {
		// BlacklistManager.blacklistSoft.blacklist(RIMBlocks.CarriageDrive,5);
	}

	@Override
	public void HandleToolUsage(int Side, boolean Sneaking) {
		super.HandleToolUsage(Side, true); // Don't allow toggling continuous -
		// it's meaningless and breaks our icons
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

		if (SignalDirection == null) {
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
		writeToNBT(tag);
	}

	public void fillPackage(CarriagePackage Package, TileEntity carriage) throws CarriageMotionException {
		MultiTypeCarriageUtil.fillPackage(Package, carriage);
	}

	@Override
	public String toString() {
		return Debug.dump(this);
	}

}
