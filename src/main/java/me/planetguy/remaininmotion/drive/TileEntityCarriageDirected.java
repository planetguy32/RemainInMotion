package me.planetguy.remaininmotion.drive;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.Directions;
import net.minecraft.entity.player.EntityPlayer;

public class TileEntityCarriageDirected extends TileEntityCarriageEngine {

	private boolean		powered	= false;

	private Directions	pointedDir;

	@Override
	public void setSignalDirection(Directions dir) {
		if (dir != null) {
			powered = true;
		}
	}

	@Override
	public Directions getSignalDirection() {
		return pointedDir;
	}

	@Override
	public boolean onRightClicked(int side, EntityPlayer player) {
		if (player.getHeldItem() != null) {
			pointedDir = Directions.values()[side];
			return true;
		}
		return false;
	}

	@Override
	public void HandleNeighbourBlockChange() {
		Debug.details(this);
		powered = false;
		super.HandleNeighbourBlockChange();
		Debug.details(this);
	}

}
