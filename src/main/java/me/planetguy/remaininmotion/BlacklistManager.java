package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlacklistManager
{
	
	public static BlacklistManager blacklistHard=new BlacklistManager(), blacklistSoft=new BlacklistManager();
	
	
	public java . util . HashSet < Block > BlacklistedIds = new java . util . HashSet < Block > ( ) ;

	public java . util . HashSet < BlockInt > BlacklistedIdAndMetaPairs = new java . util . HashSet < BlockInt > ( ) ;

	
	public void blacklist ( Block Id )
	{
		BlacklistedIds . add ( Id ) ;
	}

	public void blacklist ( Block Id , int Meta )
	{
		BlacklistedIdAndMetaPairs . add ( new BlockInt(Id, Meta) ) ;
	}

	public boolean Lookup ( BlockRecord record )
	{
		return lookup(record.World,record.X, record.Y, record.Z);
	}
	
	public boolean lookup(World w, int x, int y, int z){
		int meta=w.getBlockMetadata(x, y, z);
		Block block=w.getBlock(x, y, z);
		if(BlacklistedIds.contains(block) || BlacklistedIds.contains(new BlockInt(block, meta))){
			return true;
		}else{
			return false;
		}
	}

	public static void Initialize ( )
	{
		blacklistSoft.blacklist ( RIMBlocks . Spectre ) ;
		
		blacklistSoft.blacklist(Blocks.air);
		
		Debug.dbg(blacklistSoft.BlacklistedIds);
		
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
	
	private class BlockInt{
		
		public final Block block;
		public final int meta;
		
		public BlockInt(Block b, int i){
			this.block=b;
			this.meta=i;
		}
		
		public boolean equals(Object o){
			if(o instanceof BlockInt){
				return ((BlockInt) o).block==block && ((BlockInt)o).meta == meta;
			}else{
				return false;
			}
		}
		
		public int hashCode(){
			return block.hashCode() ^ meta;
		}
	}
}
