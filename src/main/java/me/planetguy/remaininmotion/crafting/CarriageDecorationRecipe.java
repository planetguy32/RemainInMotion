package me.planetguy.remaininmotion.crafting ;

import me.planetguy.remaininmotion.base.BlockItem;
import me.planetguy.remaininmotion.base.Recipe;
import me.planetguy.remaininmotion.carriage.CarriageItem;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class CarriageDecorationRecipe extends Recipe
{
	@Override
	public net . minecraft . item . ItemStack Process ( net . minecraft . inventory . InventoryCrafting Inventory )
	{
		net . minecraft . item . ItemStack Carriage = null ;

		net . minecraft . item . ItemStack Decoration = null ;

		int InventorySize = Inventory . getSizeInventory ( ) ;

		for ( int Index = 0 ; Index < InventorySize ; Index ++ )
		{
			net . minecraft . item . ItemStack Item = Inventory . getStackInSlot ( Index ) ;

			if ( Item == null )
			{
				continue ;
			}

			if ( Item.getItem() instanceof ItemBlock && ((ItemBlock)Item.getItem()).field_150939_a == RIMBlocks . Carriage )
			{
				if ( Carriage != null )
				{
					return ( null ) ;
				}

				Carriage = Item ;

				continue ;
			}

			if ( Decoration != null )
			{
				return ( null ) ;
			}

			Decoration = Item ;
		}

		if ( Carriage == null )
		{
			return ( null ) ;
		}

		int Tier = CarriageItem . GetTier ( Carriage ) ;

		int DecorationId = CarriageItem . GetDecorationId ( Carriage ) ;

		if ( DecorationId == 0 )
		{
			if ( Decoration == null )
			{
				return ( null ) ;
			}

			if ( ! ( Decoration . getItem ( ) instanceof net . minecraft . item . ItemBlock ) )
			{
				return ( null ) ;
			}

			DecorationId = Item.getIdFromItem(Decoration.getItem()) ;

			int DecorationMeta = Decoration . getItem ( ) . getMetadata ( Decoration . getItemDamage() ) ;

			return ( CarriageItem . Stack ( Carriage . getItemDamage() , Tier , DecorationId , DecorationMeta ) ) ;
		}

		if ( Decoration != null )
		{
			return ( null ) ;
		}

		return ( CarriageItem . Stack ( BlockItem . GetBlockType ( Carriage ) , Tier ) ) ;
	}
}
