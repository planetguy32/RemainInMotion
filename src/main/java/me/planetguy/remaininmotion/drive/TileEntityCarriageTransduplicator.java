package me.planetguy.remaininmotion.drive;

import java.util.HashMap;
import java.util.LinkedList;

import me.planetguy.remaininmotion.util.Position.BlockPosition;
import me.planetguy.remaininmotion.util.Position.BlockRecord;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
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
	public String																Player;

	public int																	Label;

	public static HashMap<String, HashMap<Integer, LinkedList<BlockPosition>>>	ActiveTranslocatorSets	= new HashMap<String, HashMap<Integer, LinkedList<BlockPosition>>>();

	@Override
	public void RegisterLabel() {
		HashMap<Integer, LinkedList<BlockPosition>> ActiveTranslocatorSet = ActiveTranslocatorSets.get(Player);

		if (ActiveTranslocatorSet == null) {
			ActiveTranslocatorSet = new HashMap<Integer, LinkedList<BlockPosition>>();

			ActiveTranslocatorSets.put(Player, ActiveTranslocatorSet);
		}

		LinkedList<BlockPosition> ActiveTranslocators = ActiveTranslocatorSet.get(Label);

		if (ActiveTranslocators == null) {
			ActiveTranslocators = new LinkedList<BlockPosition>();

			ActiveTranslocatorSet.put(Label, ActiveTranslocators);
		}

		ActiveTranslocators.add(GeneratePositionObject());
	}

	@Override
	public void ClearLabel() {
		try {
			ActiveTranslocatorSets.get(Player).get(Label).remove(GeneratePositionObject());
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();
		}
	}

	@Override
	public void Setup(EntityPlayer Player, ItemStack Item) {
		super.Setup(Player, Item);

		this.Player = ItemCarriageDrive.GetPrivateFlag(Item) ? Player.getDisplayName() : "";

		Label = ItemCarriageDrive.GetLabel(Item);

		if (!worldObj.isRemote) {

			RegisterLabel();

			/* dirty hack needed for unknown reason */
			{
				ClearLabel();

				RegisterLabel();
			}
		}
	}

	@Override
	public void EmitDrops(BlockRiM Block, int Meta) {
		EmitDrop(Block, ItemCarriageDrive.Stack(Meta, Tier, !Player.equals(""), Label));
	}

	@Override
	public void Initialize() {
		super.Initialize();

		if (!worldObj.isRemote) {
			if (Player != null) {
				RegisterLabel();
			}
		}
	}

	@Override
	public void Finalize() {
		if (!worldObj.isRemote) {
			ClearLabel();
		}
	}

	@Override
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		super.ReadCommonRecord(TagCompound);

		Player = TagCompound.getString("Player");

		Label = TagCompound.getInteger("Label");
	}

	@Override
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		super.WriteCommonRecord(TagCompound);

		TagCompound.setString("Player", Player);

		TagCompound.setInteger("Label", Label);
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
			ActiveTranslocators = ActiveTranslocatorSets.get(Player).get(Label);
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
	public CarriagePackage GeneratePackage(TileEntity carriage, Directions CarriageDirection, Directions MotionDirection)
			throws CarriageMotionException {
		CarriagePackage Package = new CarriagePackage(this, carriage, null);

		MultiTypeCarriageUtil.fillPackage(Package, carriage);

		if (Package.Body.contains(Package.driveRecord)) { throw (new CarriageMotionException(
				"carriage is attempting to grab translocator")); }

		Package.Finalize();

		return (Package);
	}

	@Override
	public void InitiateMotion(CarriagePackage Package) {
		Package.Translocator.ToggleActivity();

		super.InitiateMotion(Package);
	}

	@Override
	public void EstablishPlaceholders(CarriagePackage Package) {
		for (BlockRecord Record : Package.NewPositions) {
			// SneakyWorldUtil . SetBlock ( worldObj , Record . X + xCoord ,
			// Record . Y + yCoord , Record . Z + zCoord , RIMBlocks . Spectre ,
			// Spectre . Types . Supportive . ordinal ( ) ) ;

			SneakyWorldUtil.SetBlock(Package.Translocator.getWorldObj(), Record.X + Package.Translocator.xCoord,
					Record.Y + Package.Translocator.yCoord, Record.Z + Package.Translocator.zCoord, RIMBlocks.Spectre,
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
