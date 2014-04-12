package me.planetguy.remaininmotion ;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import me.planetguy.remaininmotion.base.Stack;
import me.planetguy.remaininmotion.core.Blocks;
import me.planetguy.remaininmotion.core.Item;

public class SimpleItemSet extends Item
{

	public SimpleItemSet ( )
	{
		super ( ) ;
	}

	public enum Types
	{
		CarriageCrosspiece ,
		CarriagePanel ,
		CarriageFramework ;

		public IIcon Icon ;

		public net . minecraft . item . ItemStack Stack ( )
		{
			return ( Stack . New ( Items . SimpleItemSet , this ) ) ;
		}
	}

	@Override
	public String getItemStackDisplayName ( net . minecraft . item . ItemStack Item )
	{
		try
		{
			switch ( Types . values ( ) [ Item . getItemDamage() ] )
			{
				case CarriageCrosspiece :

					return ( "Carriage Crosspiece" ) ;

				case CarriagePanel :

					return ( "Carriage Panel" ) ;

				case CarriageFramework :

					return ( "Carriage Framework" ) ;
			}
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}

		return ( "INVALID ITEM" ) ;
	}

	@Override
	public void AddShowcaseStacks ( java . util . List Showcase )
	{
		for ( Types Type : Types . values ( ) )
		{
			Showcase . add ( Stack . New ( this , Type ) ) ;
		}
	}

	@Override
	public void registerIcons ( IIconRegister IconRegister )
	{
		for ( Types Type : Types . values ( ) )
		{
			Type . Icon = Registry . RegisterIcon ( IconRegister , Type . name ( ) ) ;
		}
	}

	@Override
	public IIcon getIconFromDamage ( int Damage )
	{
		try
		{
			return ( Types . values ( ) [ Damage ] . Icon ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			return ( Blocks . Spectre . getIcon ( 0 , 0 ) ) ;
		}
	}
}
