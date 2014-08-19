package me.planetguy.remaininmotion.crafting ;

import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.base.BlockItem;
import me.planetguy.remaininmotion.base.Recipe;
import me.planetguy.remaininmotion.core.Items;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.drive.CarriageDrive;
import me.planetguy.remaininmotion.drive.CarriageDriveItem;
import me.planetguy.remaininmotion.util.ItemUtil;

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
			net . minecraft . item . ItemStack Item = Inventory . getStackInSlot ( Index ) ;

			if ( Item == null )
			{
				continue ;
			}

			if ( ItemUtil.matches(Item, RIMBlocks.CarriageDrive) )
			{
				if ( BlockItem . GetBlockType ( Item ) == CarriageDrive . Types . Translocator . ordinal ( ) )
				{
					if ( Drive != null )
					{
						return ( null ) ;
					}

					Drive = Item ;

					continue ;
				}
			}
			else if (ItemUtil.matches(Item, Items.ToolItemSet))
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

		return ( CarriageDriveItem . Stack ( BlockItem . GetBlockType ( Drive ) , 0 , CarriageDriveItem . GetPrivateFlag ( Drive ) , CarriageDriveItem . GetLabel ( Drive ) ) ) ;
	}
}
