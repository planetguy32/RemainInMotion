package me.planetguy.remaininmotion ;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.RIMBlocks;

public abstract class CarriagePackageBlacklist
{
	public static java . util . HashSet < Integer > BlacklistedIds = new java . util . HashSet < Integer > ( ) ;

	public static java . util . HashSet < Integer > BlacklistedIdAndMetaPairs = new java . util . HashSet < Integer > ( ) ;

	public static void blacklist ( Block Id )
	{
		BlacklistedIds . add ( Block.getIdFromBlock(Id) ) ;
	}

	public static void blacklist ( int Id , int Meta )
	{
		BlacklistedIdAndMetaPairs . add ( ( Id << 4 ) | Meta ) ;
	}

	public static boolean Lookup ( BlockRecord record )
	{
		if ( BlacklistedIds . contains ( record . Id ) )
		{
			return ( true ) ;
		}

		if ( BlacklistedIdAndMetaPairs . contains ( ( Block.getIdFromBlock(record . Id) << 4 ) | record . Meta ) )
		{
			return ( true ) ;
		}

		return ( false ) ;
	}

	public static void Initialize ( )
	{
		blacklist ( RIMBlocks . Spectre ) ;

		if ( Configuration . Carriage . BlacklistBedrock )
		{
			blacklist ( Blocks.bedrock ) ;
		}

		if ( Configuration . Carriage . BlacklistByPiston )
		{
			blacklist ( Blocks.obsidian ) ;

			for ( Object objBlock : Block.blockRegistry )
			{
				Block block=(Block)objBlock;
				
				if ( block == null )
				{
					continue ;
				}

				try{
					if ( block.getBlockHardness(null, 0,0,0) < 0 )
					{
						blacklist ( block ) ;

						continue ;
					}
				}catch(NullPointerException npe){
					blacklist(block);
				}

				if ( block . getMobilityFlag ( ) == 2 )
				{
					blacklist ( block ) ;

					continue ;
				}
			}
		}
	}
}
