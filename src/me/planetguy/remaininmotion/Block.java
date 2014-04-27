package me.planetguy.remaininmotion ;

import java.util.Arrays;

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
			
			System.out.println("Meta: "+Meta+","+Arrays.toString(TileEntityClasses));

			return ( null ) ;
		}
	}

	public Block ( int Id , net . minecraft . block . Block Template , Class < ? extends BlockItem > BlockItemClass , Class < ? extends TileEntity > ... TileEntityClasses )
	{
		super ( Id , new net . minecraft . block . material . Material ( Template . blockMaterial . materialMapColor ) ) ;

		setUnlocalizedName ( Mod . Handle + "_" + getClass ( ) . getSimpleName ( ) ) ;

		setHardness ( Template . blockHardness ) ;

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

	public Block ( int Id , net . minecraft . block . Block Template , Class < ? extends BlockItem > BlockItemClass , String HarvestToolType , Class < ? extends TileEntity > ... TileEntityClasses )
	{
		this ( Id , Template , BlockItemClass , TileEntityClasses ) ;

		net . minecraftforge . common . MinecraftForge . setBlockHarvestLevel ( this , HarvestToolType , 0 ) ;

		setCreativeTab ( CreativeTab . Instance ) ;
	}

	public static net . minecraft . block . Block Get ( int Id )
	{
		return ( net . minecraft . block . Block . blocksList [ Id ] ) ;
	}

	public void AddShowcaseStacks ( java . util . List Showcase )
	{
	}

	@Override
	public void getSubBlocks ( int Id , net . minecraft . creativetab . CreativeTabs CreativeTab , java . util . List Showcase )
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
			( ( TileEntity ) World . getBlockTileEntity ( X , Y , Z ) ) . Setup ( ( net . minecraft . entity . player . EntityPlayer ) Entity , Item ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}
	}

	@Override
	public boolean removeBlockByPlayer ( net . minecraft . world . World World , net . minecraft . entity . player . EntityPlayer Player , int X , int Y , int Z )
	{
		if ( ! World . isRemote )
		{
			if ( ! Player . capabilities . isCreativeMode )
			{
				try
				{
					( ( TileEntity ) World . getBlockTileEntity ( X , Y , Z ) ) . EmitDrops ( this , World . getBlockMetadata ( X , Y , Z ) ) ;
				}
				catch ( Throwable Throwable )
				{
					Throwable . printStackTrace ( ) ;
				}
			}
		}

		return ( super . removeBlockByPlayer ( World , Player , X , Y , Z ) ) ;
	}
}
