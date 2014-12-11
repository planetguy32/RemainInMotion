package me.planetguy.remaininmotion.spectre;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
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
		//if(worldObj != null) {
		//	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		//	markDirty();
		//}
	}
	
	public int getAxis() {
		Debug.dbg(axisOfRotation);
		return axisOfRotation;
	}
	
	public void WriteCommonRecord(NBTTagCompound tag) {
		super.WriteCommonRecord(tag);
		writeSyncableDataToNBT(tag);
	}
	
	public void ReadCommonRecord(NBTTagCompound tag) {
		super.ReadCommonRecord(tag);
		readSyncableDataFromNBT(tag);
	}
	
	/*
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound syncData = new NBTTagCompound();
		this.writeSyncableDataToNBT(syncData);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		readSyncableDataFromNBT(pkt.func_148857_g());
	}
	*/

	private void readSyncableDataFromNBT(NBTTagCompound tag) {
		axisOfRotation=tag.getInteger("axisOfRotation");
	}
	
	private void writeSyncableDataToNBT(NBTTagCompound tag) {
		tag.setInteger("axisOfRotation",axisOfRotation);
	}

	
}
