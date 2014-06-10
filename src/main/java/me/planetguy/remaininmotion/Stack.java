package me.planetguy.remaininmotion ;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class Stack
{
	public static net . minecraft . item . ItemStack Tag ( net . minecraft . item . ItemStack stack )
	{
		stack . stackTagCompound = new net . minecraft . nbt . NBTTagCompound ( ) ;
		
		return ( stack ) ;
	}

	public static net . minecraft . item . ItemStack Resize ( net . minecraft . item . ItemStack Item , int Quantity )
	{
		Item . stackSize = Quantity ;

		return ( Item ) ;
	}
	
	public static net . minecraft . item . ItemStack New ( net . minecraft . item . Item Item , int Damage , int Quantity )
	{
		return new ItemStack( Item , Quantity, Damage );
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . item . Item Item , Enum Type , int Quantity )
	{
		return ( New ( Item , Type . ordinal ( ) , Quantity ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . item . Item Item , int Damage )
	{
		return ( New ( Item , Damage , 1 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . item . Item Item , Enum Type )
	{
		return ( New ( Item , Type , 1 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . item . Item Item )
	{
		return ( New ( Item , 0 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . block . Block Block , int Damage , int Quantity )
	{
		return ( New ( Item.getItemFromBlock(Block) , Damage , Quantity ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . block . Block Block , Enum Type , int Quantity )
	{
		return ( New ( Block , Type . ordinal ( ) , Quantity ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . block . Block Block , int Damage )
	{
		return ( New ( Block , Damage , 1 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . block . Block Block , Enum Type )
	{
		return ( New ( Block , Type , 1 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . block . Block Block )
	{
		return ( New ( Block , 0 ) ) ;
	}
}
