package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.Directions;

public class RotativeSpectreEntity extends MotiveSpectreEntity {
	
	public void ShiftBlockPosition(BlockRecord record) {
		Rotator.rotate(this.DriveRecord, Directions.PosY, record);
	}
	
	

}
