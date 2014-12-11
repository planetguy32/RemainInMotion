package me.planetguy.remaininmotion.spectre;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.Directions;

public class RotativeSpectreEntity extends MotiveSpectreEntity {
	
	private int axisOfRotation;
	
	public RotativeSpectreEntity() {
		this.MotionDirection=Directions.Null;
		this.axisOfRotation=Directions.PosY.ordinal();
	}
	
	public void ShiftBlockPosition(BlockRecord record) {
		Rotator.rotate(this.DriveRecord, Directions.values()[axisOfRotation], record);
	}
	
	public void onMotionFinalized(BlockRecord record){
		Block b=worldObj.getBlock(record.X, record.Y, record.Z);
		b.rotateBlock(worldObj, record.X, record.Y, record.Z, ForgeDirection.values()[axisOfRotation]);
	}
	
	public void setAxis(int axis) {
		this.axisOfRotation=axis;
	}
	
	public int getAxis() {
		Debug.dbg(axisOfRotation);
		return axisOfRotation;
	}
	
}
