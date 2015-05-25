package me.planetguy.remaininmotion.drive;

import me.planetguy.remaininmotion.drive.gui.Buttons;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriageObstructionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCarriageEngine extends TileEntityCarriageDrive {
	
	@Override
	public CarriagePackage GeneratePackage(TileEntity carriage, Directions CarriageDirection, Directions MotionDirection)
			throws CarriageMotionException {
		
		if(Anchored()){

			if (MotionDirection == CarriageDirection.opposite()) { throw (new CarriageMotionException(
					"motor cannot pull carriage into itself")); }

		}

		CarriagePackage Package = new CarriagePackage(this, carriage, MotionDirection);

		if(!Anchored()) {
			Package.AddBlock(Package.driveRecord);

			if (MotionDirection != CarriageDirection) {
				Package.AddPotentialObstruction(Package.driveRecord.NextInDirection(MotionDirection));
			}
		}

		MultiTypeCarriageUtil.fillPackage(Package, carriage);
		
		if(Anchored()){
			if (Package.Body.contains(Package.driveRecord)) { throw (new CarriageMotionException(
					"carriage is attempting to move motor")); }

			if (Package.Body.contains(Package.driveRecord.NextInDirection(MotionDirection.opposite()))) { throw (new CarriageObstructionException(
					"carriage motion is obstructed by motor", xCoord, yCoord, zCoord)); }
		}

		Package.Finalize();

		// Called twice, once in Finalize()
		//removeUsedEnergy(Package);

		return (Package);
	}

	@Override
	public boolean Anchored() {
		return isAnchored;
	}
	
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		super.WriteCommonRecord(TagCompound);
		if(Anchored()){
			TagCompound.setString("id", "");
		}
	}
	
	public void setConfigurationSuper(long flags, EntityPlayerMP changer){
		super.setConfiguration(flags, changer);
		isAnchored=(flags & (1<<(Buttons.MOVE_WITH_CARRIAGE.ordinal() + 3))) == 0;
	}
	
	public void setConfiguration(long flags, EntityPlayerMP changer){
		setConfigurationSuper(flags, changer);
		if(isAnchored){
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, BlockCarriageDrive.Types.Motor.ordinal(), 2);
			TileEntity te=new TileEntityCarriageMotor();
			NBTTagCompound tag=new NBTTagCompound();
			this.writeToNBT(tag);
			te.readFromNBT(tag);
			worldObj.setTileEntity(xCoord, yCoord, zCoord, te);
		}
	}

}
