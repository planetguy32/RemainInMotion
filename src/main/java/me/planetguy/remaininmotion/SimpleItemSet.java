package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.base.Item;
import me.planetguy.remaininmotion.core.RIMBlocks;

public class SimpleItemSet extends Item
{

	public SimpleItemSet ( )
	{
		super ( ) ;
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
	public void registerIcons ( net . minecraft . client . renderer . texture . IIconRegister IconRegister )
	{
		for ( Types Type : Types . values ( ) )
		{
			Type . Icon = Registry . RegisterIcon ( IconRegister , Type . name ( ) ) ;
		}
	}

	@Override
	public net . minecraft . util . IIcon getIconFromDamage ( int Damage )
	{
		try
		{
			return ( Types . values ( ) [ Damage ] . Icon ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			return ( RIMBlocks . Spectre . getIcon ( 0 , 0 ) ) ;
		}
	}
}
