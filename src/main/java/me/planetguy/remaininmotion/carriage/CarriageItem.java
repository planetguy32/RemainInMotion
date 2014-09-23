package me.planetguy.remaininmotion.carriage ;

import java.util.Arrays;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.Stack;
import me.planetguy.remaininmotion.base.BlockItem;
import me.planetguy.remaininmotion.base.RIMBlock;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.Mod;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;

public class CarriageItem extends BlockItem
{
	public CarriageItem ( Block b )
	{
		super (b ) ;
	}

	public static int GetDecorationId ( net . minecraft . item . ItemStack Item )
	{
		if ( Item . stackTagCompound != null )
		{
			return ( Item . stackTagCompound . getInteger ( "DecorationId" ) ) ;
		}

		return ( Item . getItemDamage() >>> 8 ) ;
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
		return ( Stack ( Type , Tier , 0 , 0 ) ) ;
	}

	public static net . minecraft . item . ItemStack Stack ( int Type , int Tier , int DecorationId , int DecorationMeta )
	{
		net . minecraft . item . ItemStack Item = Stack . Tag ( Stack . New ( RIMBlocks . Carriage , Type ) ) ;

		Item . stackTagCompound . setInteger ( "DecorationId" , DecorationId ) ;

		Item . stackTagCompound . setInteger ( "DecorationMeta" , DecorationMeta ) ;

		Item . stackTagCompound . setInteger ( "Tier" , Tier ) ;

		return ( Item ) ;
	}

	/*
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

					/* renaming is intentional * /
					return ( "Support Carriage" ) ;

				case Structure :

					return ( "Structure Carriage" ) ;

				case Support :

					/* renaming is intentional * /
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
	*/

	@Override
	public void AddTooltip ( net . minecraft . item . ItemStack Item , java . util . List TooltipLines )
	{
		if ( Configuration . Cosmetic . ShowHelpInTooltips )
		{
			try
			{
				for(String s:Lang.translate(Mod.Handle+".carriage.tooltip."+GetBlockType(Item)).split("##/##")){
					TooltipLines.add(s);
				}
				
				/*
				switch ( Carriage . Types . values ( ) [ GetBlockType ( Item ) ] )
				{
					case Frame :

						TooltipLines . add ( "Carries blocks directly touching it" ) ;

						break ;
						
					case Support :

						TooltipLines . add ( "Carries straight line of blocks" ) ;
						TooltipLines . add ( "(Formerly known as 'Support Carriage'.)" ) ;

						break ;
					
					case Structure :

						TooltipLines . add ( "Carries entire cuboid of world" ) ;

						break ;

					case Platform :

						TooltipLines . add ( "Carries entire body of blocks it's connected to" ) ;
						TooltipLines . add ( "(Formerly known as 'Platform Carriage'.)" ) ;

						break ;

					case Template :

						TooltipLines . add ( "Carries blocks in the exact pattern prepared" ) ;

						break ;
				}
				*/
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
			TooltipLines . add ( Arrays.asList(Lang.translate(Mod.Handle+".pleaseUpdateCarriage").split("##/##")) ) ;
		}

		net . minecraft . item . ItemStack Decoration = Stack . New ( RIMBlock.getBlockById(DecorationId), GetDecorationMeta ( Item ) ) ;

		try
		{
			TooltipLines . add ( Lang.translate(Mod.Handle+".decoration") + Decoration . getItem ( ) . getItemStackDisplayName ( Decoration ) ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			TooltipLines . add ( Lang.translate(Mod.Handle+".decoration")+"!!ERR!!" ) ;
		}
	}
}
