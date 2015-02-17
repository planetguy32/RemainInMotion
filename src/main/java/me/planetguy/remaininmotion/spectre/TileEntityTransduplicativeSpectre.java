package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.BlockRecord;

public class TileEntityTransduplicativeSpectre extends TileEntityTeleportativeSpectre {

	@Override
	public void Release() {
		doRelease();

		for (BlockRecord r : body) {
			ShiftBlockPosition(r);
		}

		doRelease();
	}

}
