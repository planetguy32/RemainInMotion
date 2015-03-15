package me.planetguy.remaininmotion.util.Position;

import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.api.event.IBlockPos;
import me.planetguy.remaininmotion.base.TileEntityRiM;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRecord implements Comparable<BlockRecord>, IBlockPos {
	public int	X;
	public int	Y;
	public int	Z;

	@Override
	public String toString() {
		return ("(" + X + "," + Y + "," + Z + ")");
	}

	public BlockRecord(TileEntityRiM te) {
		this(te.xCoord, te.yCoord, te.zCoord);
		Identify(te.getWorldObj());
	}

	public BlockRecord(int X, int Y, int Z) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}

	public BlockRecord(BlockRecord Record) {
		X = Record.X;
		Y = Record.Y;
		Z = Record.Z;
	}

	public void Shift(Directions Direction) {
		X += Direction.DeltaX;
		Y += Direction.DeltaY;
		Z += Direction.DeltaZ;
	}

	public BlockRecord shift(ForgeDirection dir) {
		X += dir.offsetX;
		Y += dir.offsetY;
		Z += dir.offsetZ;
		return this;
	}

	public BlockRecord NextInDirection(Directions Direction) {
		return (new BlockRecord(X + Direction.DeltaX, Y + Direction.DeltaY, Z + Direction.DeltaZ));
	}

	@Override
	public int compareTo(BlockRecord Target) {
		int Result = X - Target.X;

		if (Result == 0) {
			Result = Y - Target.Y;

			if (Result == 0) {
				Result = Z - Target.Z;
			}
		}

		return (Result);
	}

	public Block								block;

	public int									Meta;

	public net.minecraft.tileentity.TileEntity	entity;

	public net.minecraft.nbt.NBTTagCompound		entityRecord;
	public net.minecraft.world.World			World;

	public void Identify(net.minecraft.world.World World) {
		this.World = World;

		block = World.getBlock(X, Y, Z);

		Meta = World.getBlockMetadata(X, Y, Z);

		entity = World.getTileEntity(X, Y, Z);
	}

	public static BlockRecord Identified(TileEntityRiM Anchor, int X, int Y, int Z) {
		BlockRecord Record = new BlockRecord(X, Y, Z);

		Record.Identify(Anchor.getWorldObj());

		return (Record);
	}

    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("X", X);
        tag.setInteger("Y", Y);
        tag.setInteger("Z", Z);

        tag.setInteger("Id",
                Block.getIdFromBlock(block));

        tag.setInteger("Meta", Meta);

        if (entityRecord != null) {
            tag.setTag("EntityRecord", entityRecord);
        }
    }

    public static BlockRecord createFromNBT(NBTTagCompound tag) {
        BlockRecord record = new BlockRecord(tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z"));

        record.block = Block.getBlockById(tag.getInteger("Id"));

        record.Meta = tag.getInteger("Meta");

        if (tag.hasKey("EntityRecord")) {
            record.entityRecord = tag.getCompoundTag("EntityRecord");
        }

        return record;
    }

	@Override
	public net.minecraft.world.World world() {
		return World;
	}

	@Override
	public int x() {
		return X;
	}

	@Override
	public int y() {
		return Y;
	}

	@Override
	public int z() {
		return Z;
	}

	@Override
	public TileEntity entity() {
		return entity;
	}

	@Override
	public NBTTagCompound entityTag() {
		return entityRecord;
	}

}
