package me.planetguy.remaininmotion ;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public abstract class Block extends net . minecraft . block . Block
{
	public int RenderId ;

	@Override
	public int getRenderType ( )
	{
		return ( RenderId ) ;
	}

	public Class < ? extends TileEntity > [ ] TileEntityClasses ;

	@Override
	public boolean hasTileEntity ( int Meta )
	{
		try
		{
			return ( TileEntityClasses [ Meta ] != null ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			return ( false ) ;
		}
	}

	@Override
	public net . minecraft . tileentity . TileEntity createTileEntity ( net . minecraft . world . World World , int Meta )
	{
		try
		{
			return ( TileEntityClasses [ Meta ] . newInstance ( ) ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			return ( null ) ;
		}
	}

	public Block ( net . minecraft . block . Block Template , Class < ? extends BlockItem > BlockItemClass , Class < ? extends TileEntity > ... TileEntityClasses )
	{
		super (Material.iron ) ;

		setBlockName ( Mod . Handle + "_" + getClass ( ) . getSimpleName ( ) ) ;

		setHardness ( (Float) Reflection.stealField("blockHardness", Template) ) ;

		setStepSound ( Template . stepSound ) ;

		cpw . mods . fml . common . registry . GameRegistry . registerBlock ( this , BlockItemClass , getUnlocalizedName ( ) , Mod . Handle ) ;

		this . TileEntityClasses = TileEntityClasses ;

		for ( Class < ? extends TileEntity > TileEntityClass : TileEntityClasses )
		{
			if ( TileEntityClass != null )
			{
				cpw . mods . fml . common . registry . GameRegistry . registerTileEntity ( TileEntityClass , Mod . Handle + "_" + TileEntityClass . getSimpleName ( ) ) ;
			}
		}
	}

	public abstract static class HarvestToolTypes
	{
		public static String Pickaxe = "pickaxe" ;

		public static String Hatchet = "axe" ;
	}

	public Block ( net . minecraft . block . Block Template , Class < ? extends BlockItem > BlockItemClass , String HarvestToolType , Class < ? extends TileEntity > ... TileEntityClasses )
	{
		this ( Template , BlockItemClass , TileEntityClasses ) ;

		setCreativeTab ( CreativeTab . Instance ) ;
	}

	public void AddShowcaseStacks ( java . util . List Showcase )
	{
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List Showcase)
	{
		AddShowcaseStacks ( Showcase ) ;
	}

	@Override
	public int quantityDropped ( int Meta , int Fortune , java . util . Random Random )
	{
		return ( 0 ) ;
	}

	@Override
	public void onBlockPlacedBy ( net . minecraft . world . World World , int X , int Y , int Z , net . minecraft . entity . EntityLivingBase Entity , net . minecraft . item . ItemStack Item )
	{
		super . onBlockPlacedBy ( World , X , Y , Z , Entity , Item ) ;

		try
		{
			( ( TileEntity ) World . getTileEntity ( X , Y , Z ) ) . Setup ( ( net . minecraft . entity . player . EntityPlayer ) Entity , Item ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}
	}

	@Override
	public void breakBlock ( net . minecraft . world . World World , int X , int Y , int Z, net.minecraft.block.Block b, int idk)
	{
		if ( ! World . isRemote )
		{
			try
			{
				( ( TileEntity ) World . getTileEntity ( X , Y , Z ) ) . EmitDrops ( this , World . getBlockMetadata ( X , Y , Z ) ) ;
			}
			catch ( Throwable Throwable )
			{
				Throwable . printStackTrace ( ) ;
			}
		}

		super . breakBlock ( World , X , Y , Z, b, idk );
	}
}
