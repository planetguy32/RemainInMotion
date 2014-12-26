package me.planetguy.remaininmotion.base;

import net.minecraft.item.ItemStack;

public abstract class Recipe implements net.minecraft.item.crafting.IRecipe {
	public abstract ItemStack Process(net.minecraft.inventory.InventoryCrafting Inventory);

	@Override
	public boolean matches(net.minecraft.inventory.InventoryCrafting Inventory, net.minecraft.world.World World) {
		return (Process(Inventory) != null);
	}

	@Override
	public ItemStack getCraftingResult(net.minecraft.inventory.InventoryCrafting Inventory) {
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
