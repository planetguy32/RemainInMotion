package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.util.transformations.Matrix;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRotativeSpectre extends TileEntityMotiveSpectre {

	private int	axisOfRotation;

	public TileEntityRotativeSpectre() {
		MotionDirection = Directions.Null;
		axisOfRotation = Directions.PosY.ordinal();
	}

	@Override
	public void ShiftBlockPosition(BlockRecord record) {
		RemIMRotator.rotateOrthogonal(DriveRecord, Directions.values()[axisOfRotation], record);
	}

	@Override
	public void onMotionFinalized(BlockRecord record) {
		Block b = worldObj.getBlock(record.X, record.Y, record.Z);
		if (!worldObj.isRemote) {
			b.rotateBlock(worldObj, record.X, record.Y, record.Z, ForgeDirection.values()[axisOfRotation]);
		}
	}

	@Override
	public void doPerSpectreUpdate(CapturedEntity capture, Entity entity) {
		if (worldObj.isRemote) { return; }
		Matrix entityPos = new Matrix(new double[][] { { entity.posX }, { entity.posY }, { entity.posZ } });
		double partialAngle = Math.min(((double) TicksExisted) / RiMConfiguration.CarriageMotion.MotionDuration, 1);
		RemIMRotator.rotatePartial(DriveRecord, Directions.values()[axisOfRotation], entityPos, partialAngle);
		entity.posX = entityPos.matrix[0][0];
		entity.posY = entityPos.matrix[1][0];
		entity.posZ = entityPos.matrix[2][0];
		
		entity.fallDistance = 0;
		if (TicksExisted >= RiMConfiguration.CarriageMotion.MotionDuration) {
			capture.SetPosition(MotionDirection.DeltaX, MotionDirection.DeltaY, MotionDirection.DeltaZ);
			capture.stop(entity);
			entity.onGround = capture.WasOnGround;
			entity.isAirBorne = capture.WasAirBorne;
			return;
		}
		entity.onGround = false;
		entity.isAirBorne = true;
		entity.motionX = Velocity * MotionDirection.DeltaX;
		entity.motionY = Velocity * MotionDirection.DeltaY;
		entity.motionZ = Velocity * MotionDirection.DeltaZ;
		capture.SetPosition(entity.posX, entity.posY, entity.posZ);
		entity.prevPosX = entity.posX - entity.motionX;
		entity.prevPosY = entity.posY - entity.motionY;
		entity.prevPosZ = entity.posZ - entity.motionZ;
	}

	/*
	 * 
	 * public void applyVelocityToEntity(Entity entity, double time) {
	 * double[][] pos=new double[][] { {entity.posX}, {entity.posY},
	 * {entity.posZ} }; Matrix entityMatrix=new Matrix(pos);
	 * RemIMRotator.rotatePartial(this.DriveRecord,
	 * Directions.values()[axisOfRotation], entityMatrix, time / 4.0);
	 * 
	 * entityMatrix.scalarMultiply(Velocity); Debug.dbg(entityMatrix);
	 * entity.motionX=0;//entityMatrix.matrix[0][0];
	 * entity.motionY=0;//entityMatrix.matrix[1][0];
	 * entity.motionZ=0;//entityMatrix.matrix[2][0]; }
	 * 
	 * public Matrix shiftPosition(Matrix m, double time, int ticks, Entity
	 * entity) { Matrix entityMatrix=new Matrix(new double[][] { {entity.posX},
	 * {entity.posY}, {entity.posZ} });
	 * RemIMRotator.rotatePartial(this.DriveRecord,
	 * Directions.values()[axisOfRotation], entityMatrix, time/4.0); return
	 * entityMatrix; }
	 */

	public void setAxis(int axis) {
		axisOfRotation = axis;
	}

	public int getAxis() {
		return axisOfRotation;
	}

	@Override
	public void WriteCommonRecord(NBTTagCompound tag) {
		super.WriteCommonRecord(tag);
		writeSyncableDataToNBT(tag);
	}

	@Override
	public void ReadCommonRecord(NBTTagCompound tag) {
		super.ReadCommonRecord(tag);
		readSyncableDataFromNBT(tag);
	}

	/*
	 * @Override public Packet getDescriptionPacket(){ NBTTagCompound syncData =
	 * new NBTTagCompound(); this.writeSyncableDataToNBT(syncData); return new
	 * S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1,
	 * syncData); }
	 * 
	 * @Override public void onDataPacket(NetworkManager net,
	 * S35PacketUpdateTileEntity pkt){
	 * readSyncableDataFromNBT(pkt.func_148857_g()); }
	 */

	private void readSyncableDataFromNBT(NBTTagCompound tag) {
		axisOfRotation = tag.getInteger("axisOfRotation");
	}

	private void writeSyncableDataToNBT(NBTTagCompound tag) {
		tag.setInteger("axisOfRotation", axisOfRotation);
	}

}
