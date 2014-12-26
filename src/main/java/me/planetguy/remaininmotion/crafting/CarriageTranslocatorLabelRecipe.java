package me.planetguy.remaininmotion.crafting;

import me.planetguy.remaininmotion.Vanilla;
import me.planetguy.remaininmotion.base.ItemBlockRiM;
import me.planetguy.remaininmotion.base.Recipe;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive;
import me.planetguy.remaininmotion.drive.ItemCarriageDrive;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CarriageTranslocatorLabelRecipe extends Recipe {
	@Override
	public ItemStack Process(InventoryCrafting Inventory) {
		ItemStack Drive = null;

		boolean[] DyesToAdd = new boolean[Vanilla.DyeTypes.values().length];

		boolean DyeFound = false;

		boolean ComparatorPresent = false;

		int InventorySize = Inventory.getSizeInventory();

		for (int Index = 0; Index < InventorySize; Index++) {
			ItemStack stack = Inventory.getStackInSlot(Index);

			if (stack == null) {
				continue;
			}

			if (stack.getItem().equals(Item.getItemFromBlock(RIMBlocks.CarriageDrive))) {
				if (ItemBlockRiM.GetBlockType(stack) == BlockCarriageDrive.Types.Translocator.ordinal()) {
					if (Drive != null) { return (null); }

					Drive = stack;

					continue;
				}
			} else if (stack.getItem() == Items.dye) {
				if (DyesToAdd[stack.getItemDamage()]) { return (null); }

				DyesToAdd[stack.getItemDamage()] = true;

				DyeFound = true;

				continue;
			} else if (stack.getItem().equals(Items.comparator)) {
				if (ComparatorPresent == true) { return (null); }

				ComparatorPresent = true;

				continue;
			}

			return (null);
		}

		if (Drive == null) { return (null); }

		int Tier = ItemCarriageDrive.GetTier(Drive);

		if ((!ComparatorPresent) && (!DyeFound)) { return (ItemCarriageDrive.Stack(
				BlockCarriageDrive.Types.Translocator.ordinal(), Tier)); }

		boolean Private = ItemCarriageDrive.GetPrivateFlag(Drive);

		int Label = ItemCarriageDrive.GetLabel(Drive);

		if (ComparatorPresent) {
			if (Private) { return (null); }

			Private = true;
		}

		for (Vanilla.DyeTypes DyeType : Vanilla.DyeTypes.values()) {
			if (DyesToAdd[DyeType.ordinal()]) {
				if (ItemCarriageDrive.LabelHasDye(Label, DyeType)) { return (null); }

				Label = ItemCarriageDrive.AddDyeToLabel(Label, DyeType);
			}
		}

		return (ItemCarriageDrive.Stack(BlockCarriageDrive.Types.Translocator.ordinal(), Tier, Private, Label));
	}
}
