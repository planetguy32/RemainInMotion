package me.planetguy.remaininmotion.core ;

public abstract class Recipe implements net . minecraft . item . crafting . IRecipe
{
	public abstract net . minecraft . item . ItemStack Process ( net . minecraft . inventory . InventoryCrafting Inventory ) ;

	@Override
	public boolean matches ( net . minecraft . inventory . InventoryCrafting Inventory , net . minecraft . world . World World )
	{
		return ( Process ( Inventory ) != null ) ;
	}

	@Override
	public net . minecraft . item . ItemStack getCraftingResult ( net . minecraft . inventory . InventoryCrafting Inventory )
	{
		return ( Process ( Inventory ) ) ;
	}

	@Override
	public int getRecipeSize ( )
	{
		return ( 0 ) ;
	}

	@Override
	public net . minecraft . item . ItemStack getRecipeOutput ( )
	{
		return ( null ) ;
	}
}
