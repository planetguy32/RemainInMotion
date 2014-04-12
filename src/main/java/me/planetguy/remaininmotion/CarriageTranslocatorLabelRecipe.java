package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.core.Blocks;
import me.planetguy.remaininmotion.core.Recipe;
import me.planetguy.remaininmotion.util.Vanilla;

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
			net . minecraft . item . ItemStack Item = Inventory . getStackInSlot ( Index ) ;

			if ( Item == null )
			{
				continue ;
			}

			if ( Item . itemID == Blocks . CarriageDrive . blockID )
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
			else if ( Item . itemID == net . minecraft . item . Item . dyePowder . itemID )
			{
				if ( DyesToAdd [ Item . getItemDamage() ] )
				{
					return ( null ) ;
				}

				DyesToAdd [ Item . getItemDamage() ] = true ;

				DyeFound = true ;

				continue ;
			}
			else if ( Item . itemID == net . minecraft . item . Item . comparator . itemID )
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
