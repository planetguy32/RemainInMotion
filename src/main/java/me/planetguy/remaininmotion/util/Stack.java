package me.planetguy.remaininmotion.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class Stack {
	public static ItemStack Tag(ItemStack stack) {
		stack.stackTagCompound = new NBTTagCompound();

		return (stack);
	}

	public static ItemStack Resize(ItemStack Item, int Quantity) {
		Item.stackSize = Quantity;

		return (Item);
	}

	public static ItemStack New(Item Item, int Damage, int Quantity) {
		return new ItemStack(Item, Quantity, Damage);
	}

	public static ItemStack New(Item Item, Enum Type, int Quantity) {
		return (New(Item, Type.ordinal(), Quantity));
	}

	public static ItemStack New(Item Item, int Damage) {
		return (New(Item, Damage, 1));
	}

	public static ItemStack New(Item Item, Enum Type) {
		return (New(Item, Type, 1));
	}

	public static ItemStack New(Item Item) {
		return (New(Item, 0));
	}

	public static ItemStack New(Block Block, int Damage, int Quantity) {
		return (New(Item.getItemFromBlock(Block), Damage, Quantity));
	}

	public static ItemStack New(Block Block, Enum Type, int Quantity) {
		return (New(Block, Type.ordinal(), Quantity));
	}

	public static ItemStack New(Block Block, int Damage) {
		return (New(Block, Damage, 1));
	}

	public static ItemStack New(Block Block, Enum Type) {
		return (New(Block, Type, 1));
	}

	public static ItemStack New(Block Block) {
		return (New(Block, 0));
	}
}
