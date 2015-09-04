package me.planetguy.remaininmotion.drive;

import me.planetguy.lib.util.Lang;
import me.planetguy.lib.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.position.BlockRecordSet;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.util.Registry;
import me.planetguy.remaininmotion.api.ISpecialMoveBehavior;
import me.planetguy.remaininmotion.carriage.BlockCarriage;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration.DirtyHacks;
import me.planetguy.remaininmotion.spectre.BlockSpectre;
import me.planetguy.remaininmotion.spectre.RemIMRotator;
import me.planetguy.remaininmotion.spectre.TileEntityRotativeSpectre;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import me.planetguy.remaininmotion.util.WorldUtil;
import me.planetguy.remaininmotion.util.transformations.Rotator;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityCarriageRotator extends TileEntityCarriageDirected implements ISpecialMoveBehavior {

	public boolean  	alreadyMoving;

	@Override
	public CarriagePackage GeneratePackage(TileEntity carriage, Directions CarriageDirection, Directions MotionDirection)
			throws CarriageMotionException {
		if (!DirtyHacks.allowRotation) { throw new CarriageMotionException(Lang.translate(ModRiM.Handle
				+ ".noRotatorCarriage")); }

		CarriagePackage Package = new CarriagePackage(this, carriage, Directions.Null);

		Package.axis = pointedDir.ordinal();

		Package.blacklistByRotation = true;

        MultiTypeCarriageUtil.fillPackage(Package, carriage);

		BlockRecordSet dests = new BlockRecordSet();

		for (BlockRecord record : Package.Body) {
			if ((record.X == xCoord || record.Y == yCoord || record.Z == zCoord)) {
				continue;
			}

			BlockRecord dest = RemIMRotator.simulateRotateOrthogonal(new BlockRecord(this), pointedDir,
					record);
			
			if(dest.Y > 255 || dest.Y < 0) {
				throw new CarriageMotionException("Cannot rotate block at "+new BlockRecord(this)+ " to "+dest+": out of bounds");
			}
			
            Package.AddPotentialObstruction(dest);
		}

        Package.Finalize();

		return (Package);
	}

	// don't establish placeholders yet - it's very hard to predict where things
	// will go
	@Override
	public void EstablishPlaceholders(CarriagePackage pkg) {
		for (BlockRecord Record : pkg.Body) {
			{
				SneakyWorldUtil.setBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.air, 0);
			}
		}
	}

	@Override
	public void EstablishSpectre(CarriagePackage Package) {
		int CarriageX = Package.AnchorRecord.X;
		int CarriageY = Package.AnchorRecord.Y;
		int CarriageZ = Package.AnchorRecord.Z;

		WorldUtil.SetBlock(worldObj, CarriageX, CarriageY, CarriageZ, RIMBlocks.Spectre,
				BlockSpectre.Types.Rotative.ordinal());

		TileEntityRotativeSpectre theEntity = new TileEntityRotativeSpectre();

		theEntity.setAxis(pointedDir.ordinal());

		worldObj.setTileEntity(CarriageX, CarriageY, CarriageZ, theEntity);

		theEntity.Absorb(Package);

        worldObj.markBlockForUpdate(CarriageX, CarriageY, CarriageZ);
        worldObj.func_147451_t(CarriageX, CarriageY, CarriageZ);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		try {
			if (drawSideClosed(side)) {
				return BlockCarriageDrive.InactiveIcon;
			} else {
				return icons[pointedDir.ordinal()][side];
			}
		} catch (ArrayIndexOutOfBoundsException e) { // testing only
			return Blocks.activator_rail.getIcon(0, 0);
		}
	}

	public static IIcon[][]	icons;

	public static void onRegisterIcons(IIconRegister iconRegister) {
		IIcon pivotCCW = Registry.RegisterIcon(iconRegister, "RotatorArrowCCW");
		IIcon pivotCW = new IconFlipped(pivotCCW, true, false);
		IIcon arrow = Registry.RegisterIcon(iconRegister, "RotatorArrowUp");
		icons = new IIcon[][] { 
				{ pivotCCW, pivotCCW, arrow, arrow, arrow, arrow },
				{ pivotCW, pivotCW, arrow, arrow, arrow, arrow },
				{ arrow, arrow, pivotCW, pivotCCW, arrow, arrow },
				{ arrow, arrow, pivotCCW, pivotCW, arrow, arrow }, 
				{ arrow, arrow, arrow, arrow, pivotCW, pivotCCW },
				{ arrow, arrow, arrow, arrow, pivotCCW, pivotCW }, };
	}

	public void setAxis(int axis) {
		pointedDir = Directions.validDirections()[axis];
	}

	public boolean drawSideClosed(int side) {
		return super.isSideClosed(side);
	}

	@Override
	public void updateEntity() {
		if (!(CarriageDirection != null 
				&& (CarriageDirection == pointedDir || CarriageDirection.oppositeOrdinal == pointedDir.ordinal()))) {
			CarriageDirection = null;
		}
		super.updateEntity();
	}

	@Override
	public void rotateSpecial(ForgeDirection axis) {
		super.rotateSpecial(axis);
		pointedDir = Directions.values()[Rotator.newSide(pointedDir.ordinal(), axis)];
	}

	@Override
	public void onAdded(CarriagePackage pkg, NBTTagCompound tag) throws CarriageMotionException {

		HandleNeighbourBlockChange();
		BlockRecord record = new BlockRecord(this);
		if(!alreadyMoving) {
			alreadyMoving = true;
			pkg.AddBlock(record);
			if (CarriageDirection != null && CarriageDirection != Directions.Null) {
				BlockRecord oldAnchor = pkg.AnchorRecord;
				
				pkg.AnchorRecord = new BlockRecord(xCoord + CarriageDirection.deltaX,
						yCoord + CarriageDirection.deltaY, zCoord + CarriageDirection.deltaZ);
				pkg.AnchorRecord.Identify(worldObj);
				MultiTypeCarriageUtil.fillPackage(pkg, worldObj.getTileEntity(xCoord + CarriageDirection.deltaX, yCoord
						+ CarriageDirection.deltaY, zCoord + CarriageDirection.deltaZ));
				
				pkg.AnchorRecord = oldAnchor;

			}
		}
	}

}
