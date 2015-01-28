package me.planetguy.remaininmotion.drive;

import net.minecraft.entity.player.EntityPlayer;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.Directions;

public class TileEntityCarriageDirected extends TileEntityCarriageEngine{
	
	private boolean powered=false;
	
	private Directions pointedDir;
	
	public void setSignalDirection(Directions dir) {
		if(dir!=null)
			powered=true;
	}
	
	public Directions getSignalDirection() {
		return pointedDir;
	}
	
	@Override
	public boolean onRightClicked(int side, EntityPlayer player) {
		if(player.getHeldItem()!= null) {
			pointedDir=Directions.values()[side];
			return true;
		}
		return false;
	}
	
	public void HandleNeighbourBlockChange() {
		Debug.details(this);
		powered=false;
		super.HandleNeighbourBlockChange();
		Debug.details(this);
	}


}
