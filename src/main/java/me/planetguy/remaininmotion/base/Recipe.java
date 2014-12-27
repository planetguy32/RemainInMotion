package me.planetguy.remaininmotion.base;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public abstract class Recipe implements IRecipe {
	public abstract ItemStack Process(InventoryCrafting Inventory);

	@Override
	public boolean matches(InventoryCrafting Inventory, World World) {
		return (Process(Inventory) != null);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting Inventory) {
		return (Process(Inventory));
	}

	@Override
	public int getRecipeSize() {
		return (0);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return (null);
	}
}
