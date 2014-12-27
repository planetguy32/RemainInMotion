package me.planetguy.remaininmotion.drive;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.core.RiMConfiguration.DirtyHacks;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.spectre.TileEntityRotativeSpectre;
import me.planetguy.remaininmotion.spectre.BlockSpectre;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.WorldUtil;
import me.planetguy.remaininmotion.util.transformations.Rotator;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityCarriageRotator extends TileEntityCarriageDrive {

	private int	directionIndex;

	@Override
	public CarriagePackage GeneratePackage(TileEntity carriage, Directions CarriageDirection, Directions MotionDirection)
			throws CarriageMotionException {
		if (!DirtyHacks.allowRotation) { throw new CarriageMotionException(Lang.translate(ModRiM.Handle
				+ ".noRotatorCarriage")); }

		CarriagePackage Package = new CarriagePackage(this, carriage, Directions.Null);

		Package.axis = directionIndex;

		Package.blacklistByRotation = true;

		Package.AddBlock(Package.DriveRecord);

		MultiTypeCarriageUtil.fillPackage(Package, carriage);

		for (BlockRecord record : Package.Body) {
			if (directionIndex == 0 || directionIndex == 1) {
				// TODO collide
			}
		}

		Package.Finalize();

		removeUsedEnergy(Package);

		return (Package);
	}

	@Override
	public boolean Anchored() {
		return false;
	}

	// don't establish placeholders yet - it's very hard to predict where things
	// will go
	@Override
	public void EstablishPlaceholders(CarriagePackage pkg) {
		for (BlockRecord Record : pkg.Body) {
			{
				SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.air, 0);
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

		theEntity.setAxis(directionIndex);

		worldObj.setTileEntity(CarriageX, CarriageY, CarriageZ, theEntity);

		theEntity.Absorb(Package);
	}

	@Override
	public void HandleToolUsage(int side, boolean sneaking) {
		if (sneaking) {
			super.HandleToolUsage(side, true);
		} else {
			directionIndex = (directionIndex + 1) % 6;
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}

	@Override
	public void WriteCommonRecord(NBTTagCompound tag) {
		tag.setByte("axis", (byte) directionIndex);
	}

	@Override
	public void ReadCommonRecord(NBTTagCompound tag) {
		directionIndex = tag.getByte("axis");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		try {
			if (drawSideClosed(side)) {
				return BlockCarriageDrive.InactiveIcon;
			} else {
				return icons[directionIndex][side];
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
		icons = new IIcon[][] { { pivotCW, pivotCCW, arrow, arrow, arrow, arrow },
				{ pivotCCW, pivotCW, arrow, arrow, arrow, arrow }, { arrow, arrow, pivotCW, pivotCCW, arrow, arrow },
				{ arrow, arrow, pivotCCW, pivotCW, arrow, arrow }, { arrow, arrow, arrow, arrow, pivotCW, pivotCCW },
				{ arrow, arrow, arrow, arrow, pivotCCW, pivotCW }, };
	}

	public void setAxis(int axis) {
		directionIndex = axis;
	}

	public boolean drawSideClosed(int side) {
		return super.isSideClosed(side);
	}

	@Override
	public void updateEntity() {
		if (!(CarriageDirection != null && (CarriageDirection.ordinal() == directionIndex || CarriageDirection.Opposite == directionIndex))) {
			CarriageDirection = null;
		}
		super.updateEntity();
	}

	@Override
	public void rotate(ForgeDirection axis) {
		super.rotate(axis);
		directionIndex = Rotator.newSide(directionIndex, axis);
	}

}
