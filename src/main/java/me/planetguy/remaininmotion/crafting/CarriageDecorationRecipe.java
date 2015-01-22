package me.planetguy.remaininmotion.crafting;

import me.planetguy.remaininmotion.base.ItemBlockRiM;
import me.planetguy.remaininmotion.base.Recipe;
import me.planetguy.remaininmotion.carriage.ItemCarriage;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CarriageDecorationRecipe extends Recipe {
	@Override
	public ItemStack Process(InventoryCrafting Inventory) {
		ItemStack Carriage = null;

		ItemStack Decoration = null;

		int InventorySize = Inventory.getSizeInventory();

		for (int Index = 0; Index < InventorySize; Index++) {
			ItemStack Item = Inventory.getStackInSlot(Index);

			if (Item == null) {
				continue;
			}

			if (Item.getItem() instanceof ItemBlock
					&& ((ItemBlock) Item.getItem()).field_150939_a == RIMBlocks.Carriage) {
				if (Carriage != null) { return (null); }

				Carriage = Item;

				continue;
			}

			if (Decoration != null) { return (null); }

			Decoration = Item;
		}

		if (Carriage == null) { return (null); }

		int Tier = ItemCarriage.GetTier(Carriage);

		int DecorationId = ItemCarriage.GetDecorationId(Carriage);

		if (DecorationId == 0) {
			if (Decoration == null) { return (null); }

			if (!(Decoration.getItem() instanceof net.minecraft.item.ItemBlock)) { return (null); }

			DecorationId = Item.getIdFromItem(Decoration.getItem());

			int DecorationMeta = Decoration.getItem().getMetadata(Decoration.getItemDamage());

			return (ItemCarriage.Stack(Carriage.getItemDamage(), DecorationId, DecorationMeta));
		}

		if (Decoration != null) { return (null); }

		return (ItemCarriage.Stack(ItemBlockRiM.GetBlockType(Carriage)));
	}
}
