package me.planetguy.remaininmotion.base ;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public abstract class BlockItem extends net . minecraft . item . ItemBlock
{
	public BlockItem ( Block id )
	{
		super (id ) ;

		setHasSubtypes ( true ) ;
	}

	@Override
	public int getMetadata ( int Damage )
	{
		return ( Damage ) ;
	}

	public static int GetBlockType ( net . minecraft . item . ItemStack Item )
	{
		return ( Item . getItemDamage() ) ;
	}

	public void AddTooltip ( net . minecraft . item . ItemStack Item , java . util . List TooltipLines )
	{
	}

	@Override
	public void addInformation ( net . minecraft . item . ItemStack Item , net . minecraft . entity . player . EntityPlayer Player , java . util . List TooltipLines , boolean AdvancedTooltipsActive )
	{
		AddTooltip ( Item , TooltipLines ) ;
	}
	
	public String getItemStackDisplayName(ItemStack stk){
		return getItemDisplayName(stk);
	}
	
	public abstract String getItemDisplayName ( net . minecraft . item . ItemStack Item );
}
