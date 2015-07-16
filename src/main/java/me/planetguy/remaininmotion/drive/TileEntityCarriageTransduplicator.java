package me.planetguy.remaininmotion.drive;

import java.util.HashMap;
import java.util.LinkedList;

import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.util.position.BlockPosition;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.spectre.BlockSpectre;
import me.planetguy.remaininmotion.spectre.TileEntityTransduplicativeSpectre;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCarriageTransduplicator extends TileEntityCarriageTranslocator {

	public static HashMap<String, HashMap<Integer, LinkedList<BlockPosition>>>	ActiveTransduplicatorSets	= new HashMap<String, HashMap<Integer, LinkedList<BlockPosition>>>();

	public HashMap<String, HashMap<Integer, LinkedList<BlockPosition>>> getRegistry(){
		return ActiveTranslocatorSets;
	}
	
	@Override
	public void EmitDrops(BlockRiM Block, int Meta) {
		
	}

	@Override
	public boolean Anchored() {
		return (true);
	}

	@Override
	public CarriagePackage PreparePackage(Directions MotionDirection) throws CarriageMotionException {
		CarriagePackage Package = prepareDefaultPackage(null);

		TileEntityCarriageTransduplicator Target = null;

		LinkedList<BlockPosition> ActiveTranslocators;

		try {
			ActiveTranslocators = getRegistry().get(Player).get(Label);
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();

			throw (new CarriageMotionException("translocator array is corrupt"));
		}

		for (int Index = 0; Index < ActiveTranslocators.size(); Index++) {
			BlockPosition Position = ActiveTranslocators.get(Index);

			try {
				TileEntityCarriageTransduplicator Translocator = (TileEntityCarriageTransduplicator) WorldUtil
						.GetWorld(Position.Dimension).getTileEntity(Position.X, Position.Y, Position.Z);

				if (Translocator == this) {
					continue;
				}

				boolean TargetValid = true;

				for (BlockRecord Record : Package.NewPositions) {
					if (!Translocator.worldObj.isAirBlock(Record.X + Translocator.xCoord, Record.Y
							+ Translocator.yCoord, Record.Z + Translocator.zCoord)) {
						TargetValid = false;

						break;
					}
				}

				if (TargetValid) {
					Target = Translocator;

					break;
				}
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}
		}

		if (Target == null) { throw (new CarriageMotionException(
				"no other matching translocators available with space to receive carriage assembly")); }

		Package.Translocator = Target;

		return (Package);
	}

	@Override
	public void EstablishPlaceholders(CarriagePackage pack) {
		for (BlockRecord Record : pack.NewPositions) {
			// SneakyWorldUtil . SetBlock ( worldObj , Record . X + xCoord ,
			// Record . Y + yCoord , Record . Z + zCoord , RIMBlocks . Spectre ,
			// Spectre . Types . Supportive . ordinal ( ) ) ;

			SneakyWorldUtil.SetBlock(pack.Translocator.getWorldObj(), Record.X + pack.Translocator.xCoord,
					Record.Y + pack.Translocator.yCoord, Record.Z + pack.Translocator.zCoord, RIMBlocks.Spectre,
					BlockSpectre.Types.Supportive.ordinal());
		}
	}

	@Override
	public void EstablishSpectre(CarriagePackage Package) {
		WorldUtil.SetBlock(worldObj, Package.AnchorRecord.X, Package.AnchorRecord.Y, Package.AnchorRecord.Z,
				RIMBlocks.Spectre, BlockSpectre.Types.Transduplicative.ordinal());

		((TileEntityTransduplicativeSpectre) worldObj.getTileEntity(Package.AnchorRecord.X, Package.AnchorRecord.Y,
				Package.AnchorRecord.Z)).AbsorbSource(Package);

		int NewX = Package.AnchorRecord.X - xCoord + Package.Translocator.xCoord;
		int NewY = Package.AnchorRecord.Y - yCoord + Package.Translocator.yCoord;
		int NewZ = Package.AnchorRecord.Z - zCoord + Package.Translocator.zCoord;

		WorldUtil.SetBlock(Package.Translocator.getWorldObj(), NewX, NewY, NewZ, RIMBlocks.Spectre,
				BlockSpectre.Types.Transduplicative.ordinal());

		((TileEntityTransduplicativeSpectre) Package.Translocator.getWorldObj().getTileEntity(NewX, NewY, NewZ))
				.AbsorbSink(Package);
	}
}
