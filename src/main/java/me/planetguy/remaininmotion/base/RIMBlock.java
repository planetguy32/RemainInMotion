package me.planetguy.remaininmotion.base ;

import java.util.Arrays;
import java.util.List;

import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.core.Mod;
import me.planetguy.util.Debug;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class RIMBlock extends net . minecraft . block . Block
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
			
			Debug.dbg("Meta: "+Meta+","+Arrays.toString(TileEntityClasses));

			return ( null ) ;
		}
	}

	public RIMBlock ( net . minecraft . block . Block Template , Class < ? extends BlockItem > BlockItemClass , Class < ? extends TileEntity > ... TileEntityClasses )
	{
		super ( Template.getMaterial() ) ;

		this.setBlockName( Mod . Handle + "_" + getClass ( ) . getSimpleName ( ) ) ;

		setHardness ( Template . getBlockHardness(null, 0, 0, 0) ) ;

		setStepSound ( Template . stepSound ) ;

		cpw . mods . fml . common . registry . GameRegistry . registerBlock ( this , BlockItemClass , getUnlocalizedName ( ) ) ;

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

	public RIMBlock ( net . minecraft . block . Block Template , Class < ? extends BlockItem > BlockItemClass , String HarvestToolType , Class < ? extends TileEntity > ... TileEntityClasses )
	{
		this ( Template , BlockItemClass , TileEntityClasses ) ;

		//TODO net . minecraftforge . common . MinecraftForge . setBlockHarvestLevel ( this , HarvestToolType , 0 ) ;

		setCreativeTab ( CreativeTab . Instance ) ;
	}

	public void AddShowcaseStacks ( java . util . List Showcase )
	{
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List items)
	{
		AddShowcaseStacks ( items ) ;
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
	 public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z)
	{
		if ( ! world . isRemote )
		{
			if ( ! player . capabilities . isCreativeMode )
			{
				try
				{
					( ( TileEntity ) world . getTileEntity ( x , y , z ) ) . EmitDrops ( this , world . getBlockMetadata ( x , y , z ) ) ;
				}
				catch ( Throwable Throwable )
				{
					Throwable . printStackTrace ( ) ;
				}
			}
		}

		return ( super . removedByPlayer ( world , player , x , y , z ) ) ;
	}
	
	public void dropBlockAsItem(World w, int x, int y, int z, ItemStack s)
    {
		super.dropBlockAsItem(w, x, y, z, s);
    }
	
    public float getBlockHardness(World w, int x, int y, int z){
    	Debug.dbg(w.getTileEntity(x, y, z));
    	
    	return super.getBlockHardness(w, x, y, z);
    }

}
