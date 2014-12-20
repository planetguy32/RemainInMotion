package me.planetguy.remaininmotion.spectre;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.util.transformations.Matrices;
import me.planetguy.remaininmotion.util.transformations.Matrix;

public class RotativeSpectreEntity extends MotiveSpectreEntity {
	
	private int axisOfRotation;
	
	public RotativeSpectreEntity() {
		this.MotionDirection=Directions.Null;
		this.axisOfRotation=Directions.PosY.ordinal();
	}
	
	public void ShiftBlockPosition(BlockRecord record) {
		RemIMRotator.rotateOrthogonal(this.DriveRecord, Directions.values()[axisOfRotation], record);
	}
	
	public void onMotionFinalized(BlockRecord record){
		Block b=worldObj.getBlock(record.X, record.Y, record.Z);
		if(!worldObj.isRemote) //do not rotate on client
			b.rotateBlock(worldObj, record.X, record.Y, record.Z, ForgeDirection.values()[axisOfRotation]);
	}
	
	/*
	
	public void applyVelocityToEntity(Entity entity, double time) {
		double[][] pos=new double[][] {
				{entity.posX},
				{entity.posY},
				{entity.posZ}
		};
		Matrix entityMatrix=new Matrix(pos);
		RemIMRotator.rotatePartial(this.DriveRecord, Directions.values()[axisOfRotation], entityMatrix, time / 4.0);
		
		entityMatrix.scalarMultiply(Velocity);
		Debug.dbg(entityMatrix);
		entity.motionX=0;//entityMatrix.matrix[0][0];
		entity.motionY=0;//entityMatrix.matrix[1][0];
		entity.motionZ=0;//entityMatrix.matrix[2][0];
	}
	
	public Matrix shiftPosition(Matrix m, double time, int ticks, Entity entity) {
		Matrix entityMatrix=new Matrix(new double[][] {
				{entity.posX},
				{entity.posY},
				{entity.posZ}
		});
		RemIMRotator.rotatePartial(this.DriveRecord, Directions.values()[axisOfRotation], entityMatrix, time/4.0);
		return entityMatrix;
	}
	*/

	
	public void setAxis(int axis) {
		this.axisOfRotation=axis;
	}
	
	public int getAxis() {
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
