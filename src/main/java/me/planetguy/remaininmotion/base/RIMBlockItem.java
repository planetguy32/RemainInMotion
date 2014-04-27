package me.planetguy.remaininmotion.base ;

public abstract class RIMBlockItem extends net . minecraft . item . ItemBlock
{
	public RIMBlockItem ( net.minecraft.block.Block Id )
	{
		super ( Id ) ;

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
	
}
