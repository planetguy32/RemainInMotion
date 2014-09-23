package me.planetguy.remaininmotion.crafting ;

import me.planetguy.remaininmotion.Vanilla;
import me.planetguy.remaininmotion.base.BlockItem;
import me.planetguy.remaininmotion.base.Recipe;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.drive.CarriageDrive;
import me.planetguy.remaininmotion.drive.CarriageDriveItem;
import net.minecraft.init.Items;

public class CarriageTranslocatorLabelRecipe extends Recipe
{
	@Override
	public net . minecraft . item . ItemStack Process ( net . minecraft . inventory . InventoryCrafting Inventory )
	{
		net . minecraft . item . ItemStack Drive = null ;

		boolean [ ] DyesToAdd = new boolean [ Vanilla . DyeTypes . values ( ) . length ] ;

		boolean DyeFound = false ;

		boolean ComparatorPresent = false ;

		int InventorySize = Inventory . getSizeInventory ( ) ;

		for ( int Index = 0 ; Index < InventorySize ; Index ++ )
		{
			net . minecraft . item . ItemStack stack = Inventory . getStackInSlot ( Index ) ;

			if ( stack == null )
			{
				continue ;
			}

			if ( stack.getItem().equals( net.minecraft.item.Item.getItemFromBlock(RIMBlocks . CarriageDrive )))
			{
				if ( BlockItem . GetBlockType ( stack ) == CarriageDrive . Types . Translocator . ordinal ( ) )
				{
					if ( Drive != null )
					{
						return ( null ) ;
					}

					Drive = stack ;

					continue ;
				}
			}
			else if ( stack.getItem() == Items.dye )
			{
				if ( DyesToAdd [ stack . getItemDamage() ] )
				{
					return ( null ) ;
				}

				DyesToAdd [ stack . getItemDamage() ] = true ;

				DyeFound = true ;

				continue ;
			}
			else if ( stack.getItem().equals(Items.comparator) )
			{
				if ( ComparatorPresent == true )
				{
					return ( null ) ;
				}

				ComparatorPresent = true ;

				continue ;
			}

			return ( null ) ;
		}

		if ( Drive == null )
		{
			return ( null ) ;
		}

		int Tier = CarriageDriveItem . GetTier ( Drive ) ;

		if ( ( ! ComparatorPresent ) && ( ! DyeFound ) )
		{
			return ( CarriageDriveItem . Stack ( CarriageDrive . Types . Translocator . ordinal ( ) , Tier ) ) ;
		}

		boolean Private = CarriageDriveItem . GetPrivateFlag ( Drive ) ;

		int Label = CarriageDriveItem . GetLabel ( Drive ) ;

		if ( ComparatorPresent )
		{
			if ( Private )
			{
				return ( null ) ;
			}

			Private = true ;
		}

		for ( Vanilla . DyeTypes DyeType : Vanilla . DyeTypes . values ( ) )
		{
			if ( DyesToAdd [ DyeType . ordinal ( ) ] )
			{
				if ( CarriageDriveItem . LabelHasDye ( Label , DyeType ) )
				{
					return ( null ) ;
				}

				Label = CarriageDriveItem . AddDyeToLabel ( Label , DyeType ) ;
			}
		}

		return ( CarriageDriveItem . Stack ( CarriageDrive . Types . Translocator . ordinal ( ) , Tier , Private , Label ) ) ;
	}
}
