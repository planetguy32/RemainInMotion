package me.planetguy.remaininmotion;

import me.planetguy.lib.util.Blacklist;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.Configuration.Carriage;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BlacklistManager {

	public static Blacklist blacklistHard=new Blacklist();
	public static Blacklist blacklistSoft=new Blacklist();
	public static void Initialize ( )
	{
		blacklistSoft.blacklist ( RIMBlocks . Spectre ) ;
		
		blacklistSoft.blacklist(Blocks.air);
		
		if ( Configuration . Carriage . BlacklistBedrock )
		{
			blacklistSoft.blacklist ( Blocks.bedrock ) ;
		}
	
		if ( Configuration . Carriage . BlacklistByPiston )
		{
			blacklistSoft.blacklist ( Blocks.obsidian ) ;
	
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
						blacklistSoft.blacklist ( block ) ;
	
						continue ;
					}
				}catch(NullPointerException npe){
					blacklistSoft.blacklist(block);
				}
	
				if ( block . getMobilityFlag ( ) == 2 )
				{
					blacklistSoft.blacklist ( block ) ;
	
					continue ;
				}
			}
		}
	}

}
