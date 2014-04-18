package me.planetguy.remaininmotion.base ;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.core.Mod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;

public abstract class Item extends net . minecraft . item . Item
{
	public Item ( )
	{
		super (  ) ;

		setUnlocalizedName ( Mod . Handle + "_" + getClass ( ) . getSimpleName ( ) ) ;

		setHasSubtypes ( true ) ;

		setCreativeTab ( CreativeTab . Instance ) ;

		GameRegistry.registerItem( this , getUnlocalizedName ( ) ) ;
	}

	public void AddShowcaseStacks ( java . util . List Showcase )
	{
	}

    @SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(net.minecraft.item.Item p_150895_1_, CreativeTabs p_150895_2_, List Showcase)
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
	public boolean doesSneakBypassUse ( net . minecraft . world . World World , int X , int Y , int Z ,EntityPlayer player)
	{
		return ( true ) ;
	}
}
