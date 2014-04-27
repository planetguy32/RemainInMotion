package me.planetguy.remaininmotion ;

import net.minecraft.item.Item;
import me.planetguy.remaininmotion.base.RIMBlockItem;
import me.planetguy.remaininmotion.core.Blocks;
import me.planetguy.remaininmotion.core.Recipe;

public class CarriageTranslocatorLabelConversionRecipe extends Recipe
{
	@Override
	public net . minecraft . item . ItemStack Process ( net . minecraft . inventory . InventoryCrafting Inventory )
	{
		net . minecraft . item . ItemStack Drive = null ;

		boolean ScrewdriverPresent = false ;

		int InventorySize = Inventory . getSizeInventory ( ) ;

		for ( int Index = 0 ; Index < InventorySize ; Index ++ )
		{
			net . minecraft . item . ItemStack itemStack = Inventory . getStackInSlot ( Index ) ;

			if ( itemStack == null )
			{
				continue ;
			}

			if ( itemStack.getItem() == Item.getItemFromBlock(Blocks . CarriageDrive) )
			{
				if ( RIMBlockItem . GetBlockType ( itemStack ) == CarriageDrive . Types . Translocator . ordinal ( ) )
				{
					if ( Drive != null )
					{
						return ( null ) ;
					}

					Drive = itemStack ;

					continue ;
				}
			}
			else if ( itemStack.getItem() == Items . ToolItemSet )
			{
				if ( itemStack . getItemDamage() == ToolItemSet . Types . Screwdriver . ordinal ( ) )
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

		if ( Drive == null )
		{
			return ( null ) ;
		}

		if ( Drive . stackTagCompound != null )
		{
			return ( null ) ;
		}

		if ( ! ScrewdriverPresent )
		{
			return ( null ) ;
		}

		return ( CarriageDriveItem . Stack ( RIMBlockItem . GetBlockType ( Drive ) , 0 , CarriageDriveItem . GetPrivateFlag ( Drive ) , CarriageDriveItem . GetLabel ( Drive ) ) ) ;
	}
}
