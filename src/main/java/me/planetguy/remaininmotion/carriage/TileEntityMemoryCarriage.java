package me.planetguy.remaininmotion.carriage;

import me.planetguy.remaininmotion.motion.BlacklistManager;
import me.planetguy.remaininmotion.util.Position.BlockRecord;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.base.BlockRiM;

public class TileEntityMemoryCarriage extends TileEntityTemplateCarriage {

	@Override
	public void EmitDrops(BlockRiM block, int meta) {
		emitParentDrops(block, meta);
	}

	@Override
	public boolean isBlockValidMarkerForPattern(BlockRecord record) {
		record.Identify(worldObj);
		if (worldObj.isAirBlock(record.X, record.Y, record.Z)) {
			return false;
		}
		boolean isBlacklisted = BlacklistManager.lookup(BlacklistManager.blacklistHard, record)
				|| BlacklistManager.lookup(BlacklistManager.blacklistSoft, record);
		record.entity = null;
		return !isBlacklisted;
	}

	// Do not remove pattern
	@Override
	public void erase(BlockRecord record) {

	}

	@Override
	public void ToggleSide(int Side, boolean Sneaking) {
		if (Pattern == null) {
			AbsorbPattern();

			if (Pattern == null) { return; }
		} else {
			RenderPattern = !RenderPattern;
		}

		Propagate();
	}

	@Override
	public void updatePattern() throws CarriageMotionException {
		AbsorbPattern();
		if (Pattern == null) { throw (new CarriageMotionException("template carriage has not yet been patterned")); }
	}

}
