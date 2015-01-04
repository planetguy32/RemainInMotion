package me.planetguy.remaininmotion.carriage;

import me.planetguy.remaininmotion.BlacklistManager;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.BlockRecordSet;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.base.BlockRiM;

public class TileEntityMemoryCarriage extends TileEntityTemplateCarriage {
	
	public void EmitDrops(BlockRiM block, int meta) {
		emitParentDrops(block, meta);
	}
	
	public boolean isBlockValidMarkerForPattern(BlockRecord record) {
		record.Identify(worldObj);
		boolean isBlacklisted= BlacklistManager.lookup(BlacklistManager.blacklistHard, record)
				||BlacklistManager.lookup(BlacklistManager.blacklistSoft, record);
		record.Entity=null;
		return !isBlacklisted;
	}
	
	//Do not remove pattern
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
	
	public void updatePattern() throws CarriageMotionException {
		AbsorbPattern();
		if(Pattern==null)
			throw (new CarriageMotionException("template carriage has not yet been patterned"));
	}

}
