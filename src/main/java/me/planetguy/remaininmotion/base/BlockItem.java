package me.planetguy.remaininmotion.base ;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public abstract class BlockItem extends ItemBlockWithMetadata
{
	public BlockItem ( Block id )
	{
		super (id, id ) ;

		setHasSubtypes ( true ) ;
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
	
	 public String getUnlocalizedName(ItemStack s){
		 return super.getUnlocalizedName(s)+"."+s.getItemDamage();
	 }
	
}
