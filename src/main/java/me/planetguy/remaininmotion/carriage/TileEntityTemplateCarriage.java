package me.planetguy.remaininmotion.carriage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.BlockRecordList;
import me.planetguy.remaininmotion.BlockRecordSet;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.Stack;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.spectre.RemIMRotator;
import me.planetguy.remaininmotion.util.WorldUtil;
import me.planetguy.remaininmotion.util.transformations.Matrices;
import me.planetguy.remaininmotion.util.transformations.Matrix;

public class TileEntityTemplateCarriage extends TileEntityCarriage {
	public BlockRecordList	Pattern;

	@Override
	public void EmitDrops(BlockRiM Block, int Meta) {
		super.EmitDrops(Block, Meta);

		if (Pattern == null) { return; }

		int PatternSize = Pattern.size();

		while (PatternSize > 64) {
			EmitDrop(Block, Stack.New(Block, Meta, 64));

			PatternSize -= 64;
		}

		EmitDrop(Block, Stack.New(Block, Meta, PatternSize));
	}

	@Override
	public void ToggleSide(int Side, boolean Sneaking) {
		if (Pattern == null) {
			AbsorbPattern();

			if (Pattern == null) { return; }
		} else {
			if (Sneaking) {
				ReabsorbPattern();
			} else {
				RenderPattern = !RenderPattern;
			}
		}

		Propagate();
	}

	public boolean IsUnpatternedTemplateCarriage(BlockRecord Record) {
		boolean IsUnpatternedTemplateCarriage = false;

		Record.Identify(worldObj);

		if (Record.block == RIMBlocks.Carriage) {
			if (Record.Meta == BlockCarriage.Types.Template.ordinal()) {
				if (((TileEntityTemplateCarriage) Record.Entity).Pattern == null) {
					IsUnpatternedTemplateCarriage = true;
				}
			}
		}

		Record.Entity = null;

		return (IsUnpatternedTemplateCarriage);
	}

	public void AbsorbPattern() {
		BlockRecordSet Pattern = new BlockRecordSet();

		BlockRecordSet BlocksChecked = new BlockRecordSet();

		BlocksChecked.add(new BlockRecord(xCoord, yCoord, zCoord));

		BlockRecordSet BlocksToCheck = new BlockRecordSet();

		BlocksToCheck.add(new BlockRecord(xCoord, yCoord, zCoord));

		while (BlocksToCheck.size() > 0) {
			BlockRecord Record = BlocksToCheck.pollFirst();

			for (Directions Direction : Directions.values()) {
				BlockRecord NextRecord = Record.NextInDirection(Direction);

				if (!BlocksChecked.add(NextRecord)) {
					continue;
				}

				if (IsUnpatternedTemplateCarriage(NextRecord)) {
					Pattern.add(NextRecord);

					BlocksToCheck.add(NextRecord);
				}
			}
		}

		if (Pattern.size() == 0) { return; }

		for (BlockRecord PatternBlock : Pattern) {
			WorldUtil.ClearBlock(worldObj, PatternBlock.X, PatternBlock.Y, PatternBlock.Z);

			PatternBlock.X -= xCoord;
			PatternBlock.Y -= yCoord;
			PatternBlock.Z -= zCoord;
		}

		for (Directions Direction : Directions.values()) {
			SideClosed[Direction.ordinal()] = true;
		}

		this.Pattern = new BlockRecordList();

		this.Pattern.addAll(Pattern);
	}

	public void ReabsorbPattern() {
		BlockRecordSet Pattern = new BlockRecordSet();

		for (BlockRecord Position : this.Pattern) {
			Position.X += xCoord;
			Position.Y += yCoord;
			Position.Z += zCoord;

			Pattern.add(Position);
		}

		BlockRecordSet NewPositions = new BlockRecordSet();

		BlockRecordSet DeadPositions = new BlockRecordSet();

		for (BlockRecord Position : Pattern) {
			for (Directions Direction : Directions.values()) {
				BlockRecord Record = Position.NextInDirection(Direction);

				if (Pattern.contains(Record)) {
					continue;
				}

				if (NewPositions.contains(Record)) {
					continue;
				}

				if (IsUnpatternedTemplateCarriage(Record)) {
					NewPositions.add(Record);
				}
			}

			if (IsUnpatternedTemplateCarriage(Position)) {
				DeadPositions.add(Position);
			}
		}

		Debug.dbg(NewPositions);

		for (BlockRecord Position : DeadPositions) {
			WorldUtil.ClearBlock(worldObj, Position.X, Position.Y, Position.Z);

			Pattern.remove(Position);
		}

		for (BlockRecord Position : NewPositions) {
			WorldUtil.ClearBlock(worldObj, Position.X, Position.Y, Position.Z);

			Pattern.add(Position);
		}

		for (BlockRecord Position : Pattern) {
			Position.X -= xCoord;
			Position.Y -= yCoord;
			Position.Z -= zCoord;
		}

		this.Pattern.clear();

		this.Pattern.addAll(Pattern);

		Debug.dbg(Pattern);

		Debug.dbg(this.Pattern);
	}

	public boolean	RenderPattern;

	@Override
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		super.ReadCommonRecord(TagCompound);

		if (TagCompound.hasKey("Pattern")) {
			Debug.dbg("Found PatternRecord");
			NBTTagList PatternRecord = TagCompound.getTagList("Pattern", 10);

			Pattern = new BlockRecordList();

			int PatternSize = PatternRecord.tagCount();

			Debug.dbg("PatternRecord size=" + PatternSize);
			for (int Index = 0; Index < PatternSize; Index++) {
				NBTTagCompound PatternBlockRecord = PatternRecord.getCompoundTagAt(Index);

				Pattern.add(new BlockRecord(PatternBlockRecord.getInteger("X"), PatternBlockRecord.getInteger("Y"),
						PatternBlockRecord.getInteger("Z")));
			}
		} else {
			Pattern = null;
		}

		RenderPattern = TagCompound.getBoolean("RenderPattern");

	}

	@Override
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		super.WriteCommonRecord(TagCompound);

		if (Pattern != null) {
			NBTTagList PatternRecord = new NBTTagList();

			for (BlockRecord PatternBlock : Pattern) {
				NBTTagCompound PatternBlockRecord = new NBTTagCompound();

				PatternBlockRecord.setInteger("X", PatternBlock.X);
				PatternBlockRecord.setInteger("Y", PatternBlock.Y);
				PatternBlockRecord.setInteger("Z", PatternBlock.Z);

				PatternRecord.appendTag(PatternBlockRecord);
			}

			TagCompound.setTag("Pattern", PatternRecord);
		}

		TagCompound.setBoolean("RenderPattern", RenderPattern);
	}

	@Override
	public void fillPackage(CarriagePackage Package) throws CarriageMotionException {
		if (Pattern == null) { throw (new CarriageMotionException("template carriage has not yet been patterned")); }

		Package.AddBlock(Package.AnchorRecord);

		if (Package.MotionDirection != null) {
			Package.AddPotentialObstruction(Package.AnchorRecord.NextInDirection(Package.MotionDirection));
		}

		for (BlockRecord PatternBlock : Pattern) {
			BlockRecord Record = new BlockRecord(PatternBlock.X + xCoord, PatternBlock.Y + yCoord, PatternBlock.Z
					+ zCoord);

			if (worldObj.isAirBlock(Record.X, Record.Y, Record.Z)) {
				continue;
			}

			Record.Identify(worldObj);

			Package.AddBlock(Record);

			if (Package.MotionDirection != null) {
				Package.AddPotentialObstruction(Record.NextInDirection(Package.MotionDirection));
			}
		}
	}

	@Override
	public String toString() {
		return "Template carriage " + Pattern;
	}

	public void rotate(ForgeDirection axis) {
		for (BlockRecord toRotateRecord : Pattern) {
			rotateOrthogonal(Directions.values()[axis.ordinal()], toRotateRecord);
		}
		Propagate();
	}
	
	public void rotateOrthogonal(Directions clockwiseFace, BlockRecord pos) {
		Matrix coordsMatrixNew = new Matrix(new double[][] { { pos.X }, { pos.Y }, { pos.Z } });
		Matrix rotation = Matrices.ccwRotMatrices[clockwiseFace.ordinal()];
		Matrix newCoords = rotation.crossProduct(coordsMatrixNew);
		pos.X = (int) (newCoords.matrix[0][0]);
		pos.Y = (int) (newCoords.matrix[1][0]);
		pos.Z = (int) (newCoords.matrix[2][0]);
	}

}
