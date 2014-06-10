package me.planetguy.remaininmotion.base ;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.core.Mod;

public abstract class Item extends net . minecraft . item . Item
{
	public Item ( )
	{
		super ( ) ;

		setUnlocalizedName ( Mod . Handle + "_" + getClass ( ) . getSimpleName ( ) ) ;

		setHasSubtypes ( true ) ;

		setCreativeTab ( CreativeTab . Instance ) ;

		cpw . mods . fml . common . registry . GameRegistry . registerItem ( this , getUnlocalizedName ( ) , Mod . Handle ) ;
	}

	public void AddShowcaseStacks ( java . util . List Showcase )
	{
	}

	@Override
	public void getSubItems (net.minecraft.item.Item i, CreativeTabs CreativeTab , java . util . List Showcase )
	{
		AddShowcaseStacks ( Showcase ) ;
	}

	public void AddTooltip ( net . minecraft . item . ItemStack Item , java . util . List TooltipLines )
	{
	}

	@Override
	public void addInformation ( net . minecraft . item . ItemStack Item , net . minecraft . entity . player . EntityPlayer Player , java . util . List TooltipLines , boolean Advanced )
	{
		AddTooltip ( Item , TooltipLines ) ;
	}

	@Override
	public boolean doesSneakBypassUse ( World w , int X , int Y , int Z, EntityPlayer player )
	{
		return ( true ) ;
	}
}
