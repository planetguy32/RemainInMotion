package me.planetguy.remaininmotion.drive;

import me.planetguy.remaininmotion.drive.gui.Buttons;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriageObstructionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCarriageMotor extends TileEntityCarriageEngine {
	
	public TileEntityCarriageMotor(){
		super();
		isAnchored=true;
	}
	
	public void setConfiguration(long flags, EntityPlayerMP changer){
		setConfigurationSuper(flags, changer);
		if((flags & (1<<(Buttons.MOVE_WITH_CARRIAGE.ordinal() +3))) == 0){
			isAnchored=false;
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, BlockCarriageDrive.Types.Engine.ordinal(), 2);
			TileEntity te=new TileEntityCarriageEngine();
			NBTTagCompound tag=new NBTTagCompound();
			this.writeToNBT(tag);
			te.readFromNBT(tag);
			worldObj.setTileEntity(xCoord, yCoord, zCoord, te);
		}
	}

}
