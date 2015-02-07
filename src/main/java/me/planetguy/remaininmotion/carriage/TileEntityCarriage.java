package me.planetguy.remaininmotion.carriage;

import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.api.ConnectabilityState;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.base.TileEntityCamouflageable;
import me.planetguy.remaininmotion.base.TileEntityRiM;
import me.planetguy.remaininmotion.util.transformations.ArrayRotator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityCarriage extends TileEntityCamouflageable implements Moveable, ICloseable {
	@Override
	public boolean canUpdate() {
		return (false);
	}

	public boolean[]	SideClosed	= new boolean[Directions.values().length];

	public void ToggleSide(int Side, boolean Sneaking) {
		if (Sneaking) {
			Side = Directions.values()[Side].Opposite().ordinal();
		}

		SideClosed[Side] = !SideClosed[Side];

		Propagate();
	}
	
	public ConnectabilityState isSideClosed(int side) {
		return treatSideAsClosed(side) ? ConnectabilityState.CLOSED :ConnectabilityState.OPEN;
	}

	public boolean treatSideAsClosed(int side) {
		return SideClosed[side];
	}

	@Override
	public void EmitDrops(BlockRiM Block, int Meta) {
		EmitDrop(Block, ItemCarriage.Stack(Meta, Block.getIdFromBlock(getDecoration()), getDecorationMeta()));
	}

	@Override
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		for (int Index = 0; Index < SideClosed.length; Index++) {
			SideClosed[Index] = TagCompound.getBoolean("SideClosed" + Index);
		}

		Decoration = Block.getBlockById(TagCompound.getInteger("DecorationId"));

		DecorationMeta = TagCompound.getInteger("DecorationMeta");
	}

	@Override
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		for (int Index = 0; Index < SideClosed.length; Index++) {
			TagCompound.setBoolean("SideClosed" + Index, SideClosed[Index]);
		}

		TagCompound.setInteger("DecorationId", Block.getIdFromBlock(Decoration));

		TagCompound.setInteger("DecorationMeta", DecorationMeta);
	}

	@Override
	public abstract void fillPackage(CarriagePackage Package) throws CarriageMotionException;

	@Override
	public void rotateSpecial(ForgeDirection axis) {
		ArrayRotator.rotate(SideClosed, axis);
	}
	
	

}
