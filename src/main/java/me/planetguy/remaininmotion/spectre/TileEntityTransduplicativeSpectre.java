package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.util.position.BlockRecord;

public class TileEntityTransduplicativeSpectre extends TileEntityTeleportativeSpectre {

	@Override
	public void Release() {

		for (BlockRecord r : body) {
			ShiftBlockPosition(r);
		}

		doRelease();

        for (BlockRecord r : body) {
            inverseShiftBlockPosition(r);
        }
        doRelease();
	}

    @Override
    public void cleanupSpecter(){}

    public void inverseShiftBlockPosition(BlockRecord Record) {
        Record.X -= ShiftX;
        Record.Y -= ShiftY;
        Record.Z -= ShiftZ;
    }

}
