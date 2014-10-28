package me.planetguy.lib.util ;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRecord implements Comparable < BlockRecord >
{
	public int x ;
	public int y ;
	public int z ;

	@Override
	public String toString ( )
	{
		return ( "(" + x + "," + y + "," + z + ")" ) ;
	}
	
	public BlockRecord(TileEntity te){
		this(te.xCoord, te.yCoord, te.zCoord);
	}

	public BlockRecord ( int X , int Y , int Z )
	{
		this . x = X ;
		this . y = Y ;
		this . z = Z ;
	}

	public BlockRecord ( BlockRecord Record )
	{
		x = Record .x ;
		y = Record .y ;
		z = Record .z ;
	}

	public void Shift ( ForgeDirection Direction )
	{
		x += Direction . offsetX ;
		y += Direction . offsetY ;
		z += Direction . offsetZ ;
	}

	public BlockRecord NextInDirection ( ForgeDirection Direction )
	{
		return ( new BlockRecord ( x + Direction . offsetX , y + Direction . offsetY , z + Direction . offsetZ ) ) ;
	}

	@Override
	public int compareTo ( BlockRecord Target )
	{
		int Result = x - Target .x ;

		if ( Result == 0 )
		{
			Result = y - Target .y ;

			if ( Result == 0 )
			{
				Result = z - Target .z ;
			}
		}

		return ( Result ) ;
	}

	public Block block ;

	public int meta ;

	public net . minecraft . tileentity . TileEntity tileEntity ;

	public net.minecraft.world.World world;

	public void Identify ( net . minecraft . world . World World )
	{
		this.world=World;
		
		block = World . getBlock ( x , y , z ) ;

		meta = World . getBlockMetadata ( x , y , z ) ;

		tileEntity = World . getTileEntity ( x , y , z ) ;
	}

	public static BlockRecord Identified ( TileEntity Anchor , int X , int Y , int Z )
	{
		BlockRecord Record = new BlockRecord ( X , Y , Z ) ;

		Record . Identify ( Anchor.getWorldObj() ) ;

		return ( Record ) ;
	}
	
}
