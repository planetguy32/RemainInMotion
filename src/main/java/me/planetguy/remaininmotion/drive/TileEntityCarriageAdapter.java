package me.planetguy.remaininmotion.drive;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.util.Position.BlockRecord;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
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
		HandleNeighbourBlockChange();
		BlockRecord record = new BlockRecord(this);
		if (!alreadyMoving) {
			alreadyMoving = true;
			pkg.AddBlock(record);
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
		if(sneaking) {
			//rotate
			boolean sideClosedAtIMinus1=SideClosed[SideClosed.length-1];
			for (int i = 0; i < SideClosed.length; i++) {
				boolean oldSC=SideClosed[i];
				setSideClosed( i, sideClosedAtIMinus1 );
				sideClosedAtIMinus1=oldSC;
			}
		}else{
			for (int i = 0; i < SideClosed.length; i++) {
				setSideClosed(i, i != clickedSide);
			}
		}
		Propagate();
	}

}
