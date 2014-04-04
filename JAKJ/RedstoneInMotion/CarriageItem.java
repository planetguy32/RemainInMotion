package JAKJ . RedstoneInMotion ;

public class CarriageItem extends BlockItem
{
	public CarriageItem ( int Id )
	{
		super ( Id ) ;
	}

	public static int GetDecorationId ( net . minecraft . item . ItemStack Item )
	{
		if ( Item . stackTagCompound != null )
		{
			return ( Item . stackTagCompound . getInteger ( "DecorationId" ) ) ;
		}

		return ( Item . itemDamage >>> 8 ) ;
	}

	public static int GetDecorationMeta ( net . minecraft . item . ItemStack Item )
	{
		if ( Item . stackTagCompound != null )
		{
			return ( Item . stackTagCompound . getInteger ( "DecorationMeta" ) ) ;
		}

		return ( ( Item . itemDamage >>> 4 ) & 0xF ) ;
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
		return ( Stack ( Type , Tier , 0 , 0 ) ) ;
	}

	public static net . minecraft . item . ItemStack Stack ( int Type , int Tier , int DecorationId , int DecorationMeta )
	{
		net . minecraft . item . ItemStack Item = Stack . Tag ( Stack . New ( Blocks . Carriage , Type ) ) ;

		Item . stackTagCompound . setInteger ( "DecorationId" , DecorationId ) ;

		Item . stackTagCompound . setInteger ( "DecorationMeta" , DecorationMeta ) ;

		Item . stackTagCompound . setInteger ( "Tier" , Tier ) ;

		return ( Item ) ;
	}

	@Override
	public String getItemDisplayName ( net . minecraft . item . ItemStack Item )
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

		int DecorationId = GetDecorationId ( Item ) ;

		if ( DecorationId == 0 )
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
			TooltipLines . add ( "Decoration: " + Decoration . getItem ( ) . getItemDisplayName ( Decoration ) ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			TooltipLines . add ( "Decoration: <invalid>" ) ;
		}
	}
}
