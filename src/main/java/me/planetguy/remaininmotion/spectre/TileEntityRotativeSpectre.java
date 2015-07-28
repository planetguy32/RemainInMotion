package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.util.transformations.Matrix;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.api.event.BlockRotateEvent;
import me.planetguy.remaininmotion.api.event.RotatingTEPreUnpackEvent;
import me.planetguy.remaininmotion.api.event.TEPostPlaceEvent;
import me.planetguy.remaininmotion.api.event.TEPrePlaceEvent;
import me.planetguy.remaininmotion.api.event.TEPreUnpackEvent;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Teleporter;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRotativeSpectre extends TileEntityMotiveSpectre {

	private int	axisOfRotation;

	public TileEntityRotativeSpectre() {
		motionDirection = Directions.Null;
		axisOfRotation = Directions.PosY.ordinal();
	}

	@Override
	public void ShiftBlockPosition(BlockRecord record) {
		RemIMRotator.rotateOrthogonal(driveRecord, Directions.values()[axisOfRotation], record);
	}

    @Override
    public int[] getOffset(BlockRecord record) {
        BlockRecord out = RemIMRotator.simulateRotateOrthogonal(this.driveRecord, Directions.values()[axisOfRotation], record);
        return new int[] { out.X, out.Y, out.Z};
    }

    @Override
	public void onMotionFinalized(BlockRecord record) {
		Block b = worldObj.getBlock(record.X, record.Y, record.Z);
		if (!worldObj.isRemote) {
			b.rotateBlock(worldObj, record.X, record.Y, record.Z, ForgeDirection.values()[axisOfRotation]);
			RiMRegistry.blockMoveBus.post(new BlockRotateEvent(record, ForgeDirection.values()[axisOfRotation]));
		}
	}
    
    public void announceTEConstruction(BlockRecord record) {
    	RiMRegistry.blockMoveBus.post(new RotatingTEPreUnpackEvent(this, record, ForgeDirection.getOrientation(axisOfRotation)));
    }
    
	@Override
	public void doPerSpectreEntityUpdate(CapturedEntity capture, Entity entity) {
		if(capture.startingPosition == null) {
			capture.startingPosition=new Matrix(new double[][] { { entity.posX }, { entity.posY - 2*entity.getEyeHeight() }, { entity.posZ } });
		}
		Matrix newPos=new Matrix(Matrix.copy(capture.startingPosition.matrix));
		double fractionOfCircle = Math.PI * 0.25 * 0.3333333333333333333 * Math.min(((double) ticksExisted) / RiMConfiguration.CarriageMotion.MotionDuration, 1d);
		if(driveRecord != null)
			RemIMRotator.rotatePartialEntity(driveRecord, Directions.values()[axisOfRotation], newPos, fractionOfCircle);
		
		entity.setLocationAndAngles(
				newPos.matrix[0][0],
				newPos.matrix[1][0],
				newPos.matrix[2][0], entity.rotationYaw, entity.rotationPitch);
		
		entity.fallDistance = 0;
		if (ticksExisted >= RiMConfiguration.CarriageMotion.MotionDuration) {
			capture.stop();
			entity.onGround = capture.WasOnGround;
			entity.isAirBorne = capture.WasAirBorne;
			return;
		}
		capture.stop();
		entity.onGround = false;
		entity.isAirBorne = true;
	}


	public void setAxis(int axis) {
		axisOfRotation = axis;
	}

	public int getAxis() {
		return axisOfRotation;
	}

	@Override
	public void WriteCommonRecord(NBTTagCompound tag) {
		super.WriteCommonRecord(tag);
		tag.setInteger("axisOfRotation", axisOfRotation);
	}

	@Override
	public void ReadCommonRecord(NBTTagCompound tag) {
		super.ReadCommonRecord(tag);
		axisOfRotation = tag.getInteger("axisOfRotation");
	}
}
