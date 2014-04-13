package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.base.BlockItem;
import me.planetguy.remaininmotion.core.Blocks;
import me.planetguy.remaininmotion.core.Recipe;
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

			if ( Item .getItem() instanceof ItemBlock && ((ItemBlock)Item.getItem()).field_150939_a==Blocks.Carriage )
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

		net.minecraft.block.Block DecorationId = CarriageItem . GetDecorationId ( Carriage ) ;

		if ( DecorationId == null )
		{
			if ( Decoration == null )
			{
				return ( null ) ;
			}

			if ( ! ( Decoration . getItem ( ) instanceof net . minecraft . item . ItemBlock ) )
			{
				return ( null ) ;
			}

			DecorationId = ((ItemBlock)Decoration.getItem()).field_150939_a;

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
