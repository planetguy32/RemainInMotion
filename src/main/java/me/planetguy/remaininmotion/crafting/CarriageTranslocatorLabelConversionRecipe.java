package me.planetguy.remaininmotion.crafting;

import me.planetguy.remaininmotion.base.ToolItemSet;
import me.planetguy.remaininmotion.base.ItemBlockRiM;
import me.planetguy.remaininmotion.base.Recipe;
import me.planetguy.remaininmotion.carriage.ItemCarriage;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMItems;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive;
import me.planetguy.remaininmotion.drive.ItemCarriageDrive;
import me.planetguy.remaininmotion.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class CarriageTranslocatorLabelConversionRecipe extends Recipe {
	@Override
	public ItemStack Process(InventoryCrafting Inventory) {
		ItemStack Drive = null;

		boolean ScrewdriverPresent = false;

		int InventorySize = Inventory.getSizeInventory();

		for (int Index = 0; Index < InventorySize; Index++) {
			ItemStack Item = Inventory.getStackInSlot(Index);

			if (Item == null) {
				continue;
			}

			if (ItemUtil.matches(Item, RIMBlocks.CarriageDrive)) {
				if (ItemBlockRiM.GetBlockType(Item) == BlockCarriageDrive.Types.Translocator.ordinal()) {
					if (Drive != null) { return (null); }

					Drive = Item;

					continue;
				}
			} else if (ItemUtil.matches(Item, RiMItems.ToolItemSet)) {
				if (Item.getItemDamage() == ToolItemSet.Types.Screwdriver.ordinal()) {
					if (ScrewdriverPresent) { return (null); }

					ScrewdriverPresent = true;

					continue;
				}
			}

			return (null);
		}

		if (Drive == null) { return (null); }

		if (Drive.stackTagCompound != null) { return (null); }

		if (!ScrewdriverPresent) { return (null); }

        ItemStack stk = (ItemCarriageDrive.Stack(ItemBlockRiM.GetBlockType(Drive), 0, ItemCarriageDrive.GetPrivateFlag(Drive),
                ItemCarriageDrive.GetLabel(Drive)));

        Block decoration = ItemCarriage.GetDecorationBlock(Drive);

        int decorationMeta = -1;

        if(decoration != null) {
            decorationMeta = ItemCarriage.GetDecorationMeta(Drive);
        }

        if(decoration != null) {
            stk.stackTagCompound.setInteger("DecorationId", Block.getIdFromBlock(decoration));
            stk.stackTagCompound.setInteger("DecorationMeta", decorationMeta);
        }

		return stk;
	}
}
