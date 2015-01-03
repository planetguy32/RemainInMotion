package me.planetguy.remaininmotion.carriage;

import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.base.TileEntityRiM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityCarriage extends TileEntityRiM implements Moveable, ICloseable {
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

	@Override
	public boolean isSideClosed(int side) {
		return SideClosed[side];
	}

	public int	DecorationId;

	public int	DecorationMeta;

	public int	Tier;

	@Override
	public void Setup(EntityPlayer Player, ItemStack Item) {
		DecorationId = ItemCarriage.GetDecorationId(Item);

		DecorationMeta = ItemCarriage.GetDecorationMeta(Item);

		Tier = ItemCarriage.GetTier(Item);
	}

	@Override
	public void EmitDrops(BlockRiM Block, int Meta) {
		EmitDrop(Block, ItemCarriage.Stack(Meta, Tier, DecorationId, DecorationMeta));
	}

	@Override
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		for (int Index = 0; Index < SideClosed.length; Index++) {
			SideClosed[Index] = TagCompound.getBoolean("SideClosed" + Index);
		}

		DecorationId = TagCompound.getInteger("DecorationId");

		DecorationMeta = TagCompound.getInteger("DecorationMeta");

		Tier = TagCompound.getInteger("Tier");
	}

	@Override
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		for (int Index = 0; Index < SideClosed.length; Index++) {
			TagCompound.setBoolean("SideClosed" + Index, SideClosed[Index]);
		}

		TagCompound.setInteger("DecorationId", DecorationId);

		TagCompound.setInteger("DecorationMeta", DecorationMeta);

		TagCompound.setInteger("Tier", Tier);
	}

	@Override
	public abstract void fillPackage(CarriagePackage Package) throws CarriageMotionException;

	public void rotateSpecial(ForgeDirection axis) {}

}
