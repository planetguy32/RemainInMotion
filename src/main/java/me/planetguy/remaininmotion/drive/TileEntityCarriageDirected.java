package me.planetguy.remaininmotion.drive;

import javax.swing.text.html.HTML.Tag;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.CarriageMatchers;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriageObstructionException;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.core.RiMConfiguration.CarriageMotion;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive.Types;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;

public class TileEntityCarriageDirected extends TileEntityCarriageEngine {

	private Directions	pointedDir=Directions.NegY;
	
	public static IIcon[][] cachedIcons;

	@Override
	public Directions getSignalDirection() {
		if(super.getSignalDirection() != null)
			return pointedDir;
		else
			return null;
	}

	@Override
	public boolean onRightClicked(int side, EntityPlayer player) {
		if (player.getHeldItem() != null
				&& player.getHeldItem().getItem() == Items.stick
				) {
			pointedDir = Directions.values()[side].Opposite();
			Debug.dbg(pointedDir.ordinal());
			Propagate();
			return true;
		}
		return false;
	}

	public void HandleNeighbourBlockChange() {
		Stale = false;

		CarriageDirection = null;

		boolean CarriageDirectionValid = true;

		setSignalDirection(null);

		for (Directions Direction : Directions.values()) {
			int X = xCoord + Direction.DeltaX;
			int Y = yCoord + Direction.DeltaY;
			int Z = zCoord + Direction.DeltaZ;

			if (worldObj.isAirBlock(X, Y, Z)) {
				continue;
			}

			if (isSideClosed(Direction.ordinal())) {
				continue;
			}

			net.minecraft.block.Block Id = worldObj.getBlock(X, Y, Z);
			net.minecraft.tileentity.TileEntity te = worldObj.getTileEntity(X, Y, Z);

			Moveable m = CarriageMatchers.getMover(Id, worldObj.getBlockMetadata(X, Y, Z), te);

			if (m != null) {
				if (CarriageDirection != null) {
					CarriageDirectionValid = false;
				} else {
					CarriageDirection = Direction;
				}
			}
			if (Id.isProvidingWeakPower(worldObj, X, Y, Z, Direction.ordinal()) > 0) {
				setSignalDirection(Directions.NegX); //doesn't matter
			}
		}

		if (!CarriageDirectionValid) {
			CarriageDirection = null;
		}

	}
	
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		super.WriteCommonRecord(TagCompound);
		if(pointedDir != null)
			TagCompound.setByte("pointedDir", (byte) pointedDir.ordinal());
	}
	
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		super.ReadCommonRecord(TagCompound);
		if(TagCompound.hasKey("pointedDir"))
			pointedDir=Directions.values()[TagCompound.getByte("pointedDir")];
	}
	
	public IIcon getIcon(int side, int meta) {
		try {
			return cachedIcons[pointedDir.ordinal()][side];
		} catch (Throwable Throwable) {
			 Throwable . printStackTrace ( ) ;

			return (Blocks.iron_block.getIcon(0, 0));
		}
	}
	
	public static void setupIcons(IIcon face, IIcon sid0, IIcon sid2, IIcon sid3, IIcon back) {
		IIcon sid1=new IconFlipped(sid3, true, false);
		
		Debug.mark();
		
		//TODO fix this icon matrix
		cachedIcons=new IIcon[][] {
				{back, face, sid0, sid0, sid0, sid0},
				{face, back, sid2, sid2, sid2, sid2},
				{sid2, sid2, back, face, sid1, sid3},
				{sid0, sid0, face, back, sid3, sid1},
				{sid1, sid1, sid3, sid1, back, face},//TODO
				{sid3, sid3, sid1, sid3, face, back}//TODO
		};
	}


}
