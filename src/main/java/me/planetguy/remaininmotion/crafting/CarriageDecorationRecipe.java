package me.planetguy.remaininmotion.crafting;

import me.planetguy.remaininmotion.util.Stack;
import me.planetguy.remaininmotion.base.ICamouflageable;
import me.planetguy.remaininmotion.base.Recipe;
import me.planetguy.remaininmotion.carriage.BlockCarriage;
import me.planetguy.remaininmotion.carriage.ItemCarriage;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CarriageDecorationRecipe extends Recipe {
	@Override
	public ItemStack Process(InventoryCrafting Inventory) {
		ItemStack Carriage = null;

		ItemStack decorationStack = null;

		int InventorySize = Inventory.getSizeInventory();

		for (int Index = 0; Index < InventorySize; Index++) {
			ItemStack Item = Inventory.getStackInSlot(Index);

			if (Item == null) {
				continue;
			}

			if (Item.getItem() instanceof ItemBlock
					&& ((ItemBlock) Item.getItem()).field_150939_a instanceof ICamouflageable) {
				if (Carriage != null) { return (null); }

				Carriage = Item;

				continue;
			}

			if (decorationStack != null) { return (null); }

			decorationStack = Item;
		}

		if (Carriage == null) { return (null); }

		Block decoration = ItemCarriage.GetDecorationBlock(Carriage);

		if (decorationStack == null) { return (null); }

		if (!(decorationStack.getItem() instanceof net.minecraft.item.ItemBlock)) { return (null); }

		decoration = ((ItemBlock) decorationStack.getItem()).field_150939_a;

		int DecorationMeta = decorationStack.getItem().getMetadata(decorationStack.getItemDamage());

		ItemStack stk;

		if(Carriage.getItem() instanceof ItemBlock 
				&& ((ItemBlock)Carriage.getItem()).field_150939_a == RIMBlocks.plainFrame)
			stk=new ItemStack(RIMBlocks.Carriage, 1, BlockCarriage.Types.Frame.ordinal());
		else
			stk	= new ItemStack(Carriage.getItem(), 1, Carriage.getItemDamage());

		Stack.Tag(stk);

		stk.stackTagCompound.setInteger("DecorationId", Block.getIdFromBlock(decoration));

		stk.stackTagCompound.setInteger("DecorationMeta", DecorationMeta);

		return stk;
	}
}
