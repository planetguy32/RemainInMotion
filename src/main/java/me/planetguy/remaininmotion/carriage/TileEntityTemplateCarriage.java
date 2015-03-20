package me.planetguy.remaininmotion.carriage;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.position.BlockRecordList;
import me.planetguy.remaininmotion.util.position.BlockRecordSet;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.util.WorldUtil;
import me.planetguy.remaininmotion.util.transformations.Matrices;
import me.planetguy.remaininmotion.util.transformations.Matrix;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityTemplateCarriage extends TileEntityCarriage {
	public BlockRecordList	Pattern;

	/**
	 * Shim for memory carriage
	 */

	protected void emitParentDrops(BlockRiM block, int meta) {
		super.EmitDrops(block, meta);
	}

	@Override
	public void EmitDrops(BlockRiM Block, int Meta) {
		super.EmitDrops(Block, Meta);

		if (Pattern == null) { return; }

		int PatternSize = Pattern.size();

		ItemStack item;

		while (PatternSize > 64) {
			item = ItemCarriage.Stack(Meta);
			item.stackSize = 64;
			EmitDrop(Block, item);

			PatternSize -= 64;
		}
		item = ItemCarriage.Stack(Meta);
		item.stackSize = PatternSize;

		EmitDrop(Block, item);
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

	public boolean isBlockValidMarkerForPattern(BlockRecord Record) {
		boolean IsUnpatternedTemplateCarriage = false;

		Record.Identify(worldObj);

		if (Record.block == RIMBlocks.Carriage) {
			if (Record.Meta == BlockCarriage.Types.Template.ordinal()) {
				if (((TileEntityTemplateCarriage) Record.entity).Pattern == null) {
					IsUnpatternedTemplateCarriage = true;
				}
			}
		}

		Record.entity = null;

		return (IsUnpatternedTemplateCarriage);
	}

	public void AbsorbPattern() {
		BlockRecordSet Pattern = new BlockRecordSet();

		BlockRecordSet BlocksChecked = new BlockRecordSet();

		BlocksChecked.add(new BlockRecord(xCoord, yCoord, zCoord));

		BlockRecordSet BlocksToCheck = new BlockRecordSet();

		BlocksToCheck.add(new BlockRecord(xCoord, yCoord, zCoord));

		while (BlocksToCheck.size() > 0 && BlocksChecked.size() < RiMConfiguration.Carriage.MaxTemplateBurden) {
			BlockRecord Record = BlocksToCheck.pollFirst();

			for (Directions Direction : Directions.values()) {
				BlockRecord NextRecord = Record.NextInDirection(Direction);

				if (!BlocksChecked.add(NextRecord)) {
					continue;
				}

				if (isBlockValidMarkerForPattern(NextRecord)) {
					Pattern.add(NextRecord);

					BlocksToCheck.add(NextRecord);
				}
			}
		}

		if (Pattern.size() == 0) { return; }

		for (BlockRecord PatternBlock : Pattern) {
			erase(PatternBlock);

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

	protected void erase(BlockRecord record) {
		WorldUtil.ClearBlock(worldObj, record.X, record.Y, record.Z);
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

				if (isBlockValidMarkerForPattern(Record)) {
					NewPositions.add(Record);
				}
			}

			if (isBlockValidMarkerForPattern(Position)) {
				DeadPositions.add(Position);
			}
		}

		if (RiMConfiguration.Debug.verbose) {
			Debug.dbg(NewPositions);
		}

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

		if (RiMConfiguration.Debug.verbose) {
			Debug.dbg(Pattern);

			Debug.dbg(this.Pattern);
		}
	}

	public boolean	RenderPattern;

	@Override
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		super.ReadCommonRecord(TagCompound);

		if (TagCompound.hasKey("Pattern")) {
			if (RiMConfiguration.Debug.verbose) {
				Debug.dbg("Found PatternRecord");
			}
			NBTTagList PatternRecord = TagCompound.getTagList("Pattern", 10);

			Pattern = new BlockRecordList();

			int PatternSize = PatternRecord.tagCount();

			if (RiMConfiguration.Debug.verbose) {
				Debug.dbg("PatternRecord size=" + PatternSize);
			}
			for (int Index = 0; Index < PatternSize; Index++) {
				NBTTagCompound PatternBlockRecord = PatternRecord.getCompoundTagAt(Index);

				Pattern.add(new BlockRecord(PatternBlockRecord.getShort("X"), PatternBlockRecord.getShort("Y"),
						PatternBlockRecord.getShort("Z")));
			}
		} else {
			Pattern = null;
		}

		RenderPattern = TagCompound.getBoolean("RenderPattern");

	}
	
	private void writePattern(NBTTagCompound TagCompound) {
		if (Pattern != null) {
            NBTTagList PatternRecord = new NBTTagList();

            for (BlockRecord PatternBlock : Pattern) {
                NBTTagCompound PatternBlockRecord = new NBTTagCompound();

                // Byte = 8bit
                // Short = 16bit
                // Integer = 32bit
                // Java default Byte is signed, -128 to 127 inclusive
                // Short is -32,768 to 32,767 inclusive
                // Integer is -2^31 to 2^31-1, Minecraft will kick me before it lets me make a castle that big move....
                // We save 6 bytes of data for every block in a pattern this way
                // Note changing this is only OK since these use relative location
                PatternBlockRecord.setShort("X", (short) PatternBlock.X);
                PatternBlockRecord.setShort("Y", (short) PatternBlock.Y);
                PatternBlockRecord.setShort("Z", (short) PatternBlock.Z);

                PatternRecord.appendTag(PatternBlockRecord);
            }

            TagCompound.setTag("Pattern", PatternRecord);
        }
    }

	@Override
	public void WriteServerRecord(NBTTagCompound TagCompound) {
		super.WriteCommonRecord(TagCompound);

		writePattern(TagCompound);
	}
	
	@Override
	public void WriteClientRecord(NBTTagCompound TagCompound) {
		super.WriteClientRecord(TagCompound);
        if(RenderPattern) {
        	TagCompound.setBoolean("RenderPattern", RenderPattern);
            writePattern(TagCompound);
        }
	}

	@Override
	public void fillPackage(CarriagePackage cPackage) throws CarriageMotionException {
		if (Pattern == null) {
			updatePattern();
		}

		cPackage.AddBlock(cPackage.AnchorRecord);

		if (cPackage.MotionDirection != null) {
			cPackage.AddPotentialObstruction(cPackage.AnchorRecord.NextInDirection(cPackage.MotionDirection));
		}

		for (BlockRecord PatternBlock : Pattern) {
			BlockRecord Record = new BlockRecord(PatternBlock.X + xCoord, PatternBlock.Y + yCoord, PatternBlock.Z
					+ zCoord);

			if (worldObj.isAirBlock(Record.X, Record.Y, Record.Z)) {
				continue;
			}

			Record.Identify(worldObj);

			cPackage.AddBlock(Record);

			if (cPackage.MotionDirection != null) {
				cPackage.AddPotentialObstruction(Record.NextInDirection(cPackage.MotionDirection));
			}
		}
	}

	public void updatePattern() throws CarriageMotionException {
		throw (new CarriageMotionException("template carriage has not yet been patterned"));
	}

	@Override
	public String toString() {
		return "Template carriage " + Pattern;
	}

	@Override
	public void rotateSpecial(ForgeDirection axis) {
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
