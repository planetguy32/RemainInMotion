package me.planetguy.remaininmotion ;

import net.minecraft.block.Block;
import me.planetguy.remaininmotion.core.Blocks;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.util.Reflection;

public abstract class CarriagePackageBlacklist
{
	public static java . util . HashSet < Integer > BlacklistedIds = new java . util . HashSet < Integer > ( ) ;

	public static java . util . HashSet < Integer > BlacklistedIdAndMetaPairs = new java . util . HashSet < Integer > ( ) ;

	public static void Add ( Block b )
	{
		BlacklistedIds . add ( Block.getIdFromBlock(b) ) ;
	}

	public static void Add ( Block b , int Meta )
	{
		BlacklistedIdAndMetaPairs . add ( ( Block.getIdFromBlock(b) << 4 ) | Meta ) ;
	}

	public static boolean Lookup ( BlockRecord block )
	{
		if ( BlacklistedIds . contains ( net.minecraft.block.Block.getIdFromBlock(block.block )) )
		{
			return ( true ) ;
		}

		if ( BlacklistedIdAndMetaPairs . contains ( ( Block.getIdFromBlock(block.block) << 4 ) | block . Meta ) )
		{
			return ( true ) ;
		}

		return ( false ) ;
	}

	public static void Initialize ( )
	{
		Add ( Blocks . Spectre ) ;

		if ( Configuration . Carriage . BlacklistBedrock )
		{
			Add ( Block.getBlockFromName("bedrock") ) ;
		}

		if ( Configuration . Carriage . BlacklistByPiston )
		{
			Add ( Block.getBlockFromName("obsidian")) ;

			for ( Object o:Block.blockRegistry.getKeys())
			{
				Block b=(Block) Block.blockRegistry.getObject(o);
				if ( b == null )
				{
					continue ;
				}

				if ( (Double)Reflection.stealField(b, "blockHardness") < 0 )
				{
					Add ( b ) ;

					continue ;
				}

				if ( b . getMobilityFlag ( ) == 2 )
				{
					Add ( b ) ;

					continue ;
				}
			}
		}
	}
}
