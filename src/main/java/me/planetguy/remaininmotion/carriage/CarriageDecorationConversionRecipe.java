package me.planetguy.remaininmotion.carriage ;

import me.planetguy.remaininmotion.Items;
import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.ToolItemSet.Types;
import me.planetguy.remaininmotion.base.RIMBlockItem;
import me.planetguy.remaininmotion.core.Recipe;

public class CarriageDecorationConversionRecipe extends Recipe
{
	@Override
	public net . minecraft . item . ItemStack Process ( net . minecraft . inventory . InventoryCrafting Inventory )
	{
		net . minecraft . item . ItemStack Carriage = null ;

		boolean ScrewdriverPresent = false ;

		int InventorySize = Inventory . getSizeInventory ( ) ;

		for ( int Index = 0 ; Index < InventorySize ; Index ++ )
		{
			net . minecraft . item . ItemStack Item = Inventory . getStackInSlot ( Index ) ;

			if ( Item == null )
			{
				continue ;
			}

			if ( Item.getItem() instanceof RIMBlockItem )
			{
				if ( Carriage != null )
				{
					return ( null ) ;
				}

				Carriage = Item ;

				continue ;
			}

			if ( Item.getItem()  == Items . ToolItemSet )
			{
				if ( Item . getItemDamage() == ToolItemSet . Types . Screwdriver . ordinal ( ) )
				{
					if ( ScrewdriverPresent )
					{
						return ( null ) ;
					}

					ScrewdriverPresent = true ;

					continue ;
				}
			}

			return ( null ) ;
		}

		if ( Carriage == null )
		{
			return ( null ) ;
		}

		if ( Carriage . stackTagCompound != null )
		{
			return ( null ) ;
		}

		if ( ! ScrewdriverPresent )
		{
			return ( null ) ;
		}

		net.minecraft.block.Block DecorationId = CarriageItem . GetDecorationId ( Carriage ) ;

		if ( DecorationId == null )
		{
			return ( null ) ;
		}

		return ( CarriageItem . Stack ( RIMBlockItem . GetBlockType ( Carriage ) , 0 , DecorationId , CarriageItem . GetDecorationMeta ( Carriage ) ) ) ;
	}
}
