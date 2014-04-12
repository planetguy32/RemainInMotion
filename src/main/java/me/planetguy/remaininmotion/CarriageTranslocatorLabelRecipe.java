package me.planetguy.remaininmotion ;

import net.minecraft.item.Item;
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
			net . minecraft . item . ItemStack itemStack = Inventory . getStackInSlot ( Index ) ;

			if ( itemStack == null )
			{
				continue ;
			}

			if ( itemStack.getItem() == Item.getItemFromBlock(Blocks . CarriageDrive ))
			{
				if ( BlockItem . GetBlockType ( itemStack ) == CarriageDrive . Types . Translocator . ordinal ( ) )
				{
					if ( Drive != null )
					{
						return ( null ) ;
					}

					Drive = itemStack ;

					continue ;
				}
			}
			else if ( itemStack.getItem() == net . minecraft . item . Item.itemRegistry.getObject("dye") )
			{
				if ( DyesToAdd [ itemStack . getItemDamage() ] )
				{
					return ( null ) ;
				}

				DyesToAdd [ itemStack . getItemDamage() ] = true ;

				DyeFound = true ;

				continue ;
			}
			else if ( itemStack .getItem() == net . minecraft . item . Item.itemRegistry.getObject("comparator"))
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
