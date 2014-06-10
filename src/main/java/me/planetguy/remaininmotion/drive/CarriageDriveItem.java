package me.planetguy.remaininmotion.drive ;

import net.minecraft.block.Block;
import me.planetguy.remaininmotion.Stack;
import me.planetguy.remaininmotion.Vanilla;
import me.planetguy.remaininmotion.Vanilla.DyeTypes;
import me.planetguy.remaininmotion.base.BlockItem;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.Configuration.Cosmetic;

public class CarriageDriveItem extends BlockItem
{
	public CarriageDriveItem ( Block Id )
	{
		super ( Id ) ;
	}

	public static boolean GetPrivateFlag ( net . minecraft . item . ItemStack Item )
	{
		if ( Item . stackTagCompound != null )
		{
			return ( Item . stackTagCompound . getBoolean ( "Private" ) ) ;
		}

		return ( ( ( Item . getItemDamage() >>> 4 ) & 0x1 ) == 1 ) ;
	}

	public static int GetLabel ( net . minecraft . item . ItemStack Item )
	{
		if ( Item . stackTagCompound != null )
		{
			return ( Item . stackTagCompound . getInteger ( "Label" ) ) ;
		}

		return ( Item . getItemDamage() >>> 5 ) ;
	}

	public static int GetTier ( net . minecraft . item . ItemStack Item )
	{
		if ( Item . stackTagCompound != null )
		{
			return ( Item . stackTagCompound . getInteger ( "Tier" ) ) ;
		}

		return ( 0 ) ;
	}

	public static int AddDyeToLabel ( int Label , Vanilla . DyeTypes DyeType )
	{
		return ( Label | ( 1 << DyeType . ordinal ( ) ) ) ;
	}

	public static boolean LabelHasDye ( int Label , Vanilla . DyeTypes DyeType )
	{
		return ( ( ( Label >>> DyeType . ordinal ( ) ) & 0x1 ) == 1 ) ;
	}

	public static net . minecraft . item . ItemStack Stack ( int Type , int Tier )
	{
		return ( Stack ( Type , Tier , false , 0 ) ) ;
	}

	public static net . minecraft . item . ItemStack Stack ( int Type , int Tier , boolean Private , int Label )
	{
		net . minecraft . item . ItemStack Item = Stack . Tag ( Stack . New ( RIMBlocks . CarriageDrive , Type ) ) ;

		Item . stackTagCompound . setBoolean ( "Private" , Private ) ;

		Item . stackTagCompound . setInteger ( "Label" , Label ) ;

		Item . stackTagCompound . setInteger ( "Tier" , Tier ) ;

		return ( Item ) ;
	}

	@Override
	public String getItemDisplayName ( net . minecraft . item . ItemStack Item )
	{
		try
		{
			switch ( CarriageDrive . Types . values ( ) [ GetBlockType ( Item ) ] )
			{
				case Engine :

					return ( "Carriage Engine" ) ;

				case Motor :

					return ( "Carriage Motor" ) ;

				case Translocator :

					return ( "Carriage Translocator" ) ;

				case Controller :

					return ( "Carriage Controller" ) ;
				
				case Transduplicator:
					return ("Carriage Transduplicator");
			}
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}

		return ( "INVALID CARRIAGE DRIVE" ) ;
	}

	@Override
	public void AddTooltip ( net . minecraft . item . ItemStack Item , java . util . List TooltipLines )
	{
		int Type = GetBlockType ( Item ) ;

		if ( Configuration . Cosmetic . ShowHelpInTooltips )
		{
			try
			{
				switch ( CarriageDrive . Types . values ( ) [ Type ] )
				{
					case Engine :

						TooltipLines . add ( "Moves with the carriage" ) ;

						break ;

					case Motor :

						TooltipLines . add ( "Moves the carriage while staying put" ) ;

						break ;

					case Translocator :

						TooltipLines . add ( "Teleports the carriage to a remote position" ) ;

						break ;

					case Controller :

						TooltipLines . add ( "Moves according to ComputerCraft control" ) ;

						break ;
				}
			}
			catch ( Throwable Throwable )
			{
				Throwable . printStackTrace ( ) ;

				return ;
			}
		}

		if ( Type == CarriageDrive . Types . Translocator . ordinal ( ) )
		{
			if ( Item . stackTagCompound == null )
			{
				TooltipLines . add ( "ITEM NEEDS CONVERSION TO NEW FORMAT" ) ;

				TooltipLines . add ( "(craft with screwdriver to convert)" ) ;
			}

			boolean Private = GetPrivateFlag ( Item ) ;

			int Label = GetLabel ( Item ) ;

			if ( Private )
			{
				TooltipLines . add ( "Label (private):" ) ;
			}
			else
			{
				TooltipLines . add ( "Label:" ) ;
			}

			if ( Label == 0 )
			{
				TooltipLines . add ( "<blank>" ) ;
			}
			else
			{
				for ( Vanilla . DyeTypes DyeType : Vanilla . DyeTypes . values ( ) )
				{
					if ( LabelHasDye ( Label , DyeType ) )
					{
						TooltipLines . add ( " - " + DyeType . name ( ) ) ;
					}
				}
			}
		}
	}
}
