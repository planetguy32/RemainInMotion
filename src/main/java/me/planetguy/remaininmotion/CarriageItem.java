package me.planetguy.remaininmotion ;

import net.minecraft.block.Block;

public class CarriageItem extends BlockItem
{
	
	public static net.minecraft.item.Item instance;
	
	public CarriageItem (Block b )
	{
		super (b ) ;
		instance=this;
	}

	public static Block GetDecorationId ( net . minecraft . item . ItemStack Item )
	{
		if ( Item . stackTagCompound != null )
		{
			return ( Block.getBlockById(Item . stackTagCompound . getInteger ( "DecorationId" ) )) ;
		}

		return ( Block.getBlockById(Item . getItemDamage() >>> 8 )) ;
	}

	public static int GetDecorationMeta ( net . minecraft . item . ItemStack Item )
	{
		if ( Item . stackTagCompound != null )
		{
			return ( Item . stackTagCompound . getInteger ( "DecorationMeta" ) ) ;
		}

		return ( ( Item . getItemDamage() >>> 4 ) & 0xF ) ;
	}

	public static int GetTier ( net . minecraft . item . ItemStack Item )
	{
		if ( Item . stackTagCompound != null )
		{
			return ( Item . stackTagCompound . getInteger ( "Tier" ) ) ;
		}

		return ( 0 ) ;
	}

	public static net . minecraft . item . ItemStack Stack ( int Type , int Tier )
	{
		return ( Stack ( Type , Tier , null , 0 ) ) ;
	}

	public static net . minecraft . item . ItemStack Stack ( int Type , int Tier , Block decorationId , int DecorationMeta )
	{
		net . minecraft . item . ItemStack Item = Stack . Tag ( Stack . New ( Blocks . Carriage , Type ) ) ;

		if(decorationId!=null){
			Item . stackTagCompound . setInteger ( "DecorationId" , Block.blockRegistry.getIDForObject(decorationId) ) ;
		}else{
			Item . stackTagCompound . setInteger ( "DecorationId" , 0 ) ;
		}
		Item . stackTagCompound . setInteger ( "DecorationMeta" , DecorationMeta ) ;

		Item . stackTagCompound . setInteger ( "Tier" , Tier ) ;

		return ( Item ) ;
	}

	@Override
	public String getItemStackDisplayName ( net . minecraft . item . ItemStack Item )
	{
		try
		{
			switch ( Carriage . Types . values ( ) [ GetBlockType ( Item ) ] )
			{
				case Frame :

					return ( "Frame Carriage" ) ;

				case Platform :

					/* renaming is intentional */
					return ( "Support Carriage" ) ;

				case Structure :

					return ( "Structure Carriage" ) ;

				case Support :

					/* renaming is intentional */
					return ( "Platform Carriage" ) ;

				case Template :

					return ( "Template Carriage" ) ;
			}
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}

		return ( "INVALID CARRIAGE" ) ;
	}

	@Override
	public void AddTooltip ( net . minecraft . item . ItemStack Item , java . util . List TooltipLines )
	{
		if ( Configuration . Cosmetic . ShowHelpInTooltips )
		{
			try
			{
				switch ( Carriage . Types . values ( ) [ GetBlockType ( Item ) ] )
				{
					case Frame :

						TooltipLines . add ( "Carries blocks directly touching it" ) ;

						break ;

					case Platform :

						TooltipLines . add ( "Carries entire body of blocks it's connected to" ) ;
						TooltipLines . add ( "(Formerly known as 'Platform Carriage'.)" ) ;

						break ;

					case Structure :

						TooltipLines . add ( "Carries entire cuboid of world" ) ;

						break ;

					case Support :

						TooltipLines . add ( "Carries straight line of blocks" ) ;
						TooltipLines . add ( "(Formerly known as 'Support Carriage'.)" ) ;

						break ;

					case Template :

						TooltipLines . add ( "Carries blocks in the exact pattern prepared" ) ;

						break ;
				}
			}
			catch ( Throwable Throwable )
			{
				Throwable . printStackTrace ( ) ;

				return ;
			}
		}

		Block DecorationId = GetDecorationId ( Item ) ;

		if ( DecorationId==null )
		{
			return ;
		}

		if ( Item . stackTagCompound == null )
		{
			TooltipLines . add ( "ITEM NEEDS CONVERSION TO NEW FORMAT" ) ;

			TooltipLines . add ( "(craft with screwdriver to convert)" ) ;
		}

		net . minecraft . item . ItemStack Decoration = Stack . New ( DecorationId , GetDecorationMeta ( Item ) ) ;

		try
		{
			TooltipLines . add ( "Decoration: " + Decoration . getItem ( ) . getItemStackDisplayName ( Decoration ) ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			TooltipLines . add ( "Decoration: <invalid>" ) ;
		}
	}
}
