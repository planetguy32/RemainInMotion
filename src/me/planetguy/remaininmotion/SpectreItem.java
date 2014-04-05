package me.planetguy.remaininmotion ;

public class SpectreItem extends BlockItem
{
	public SpectreItem ( int Id )
	{
		super ( Id ) ;
	}

	@Override
	public String getItemDisplayName ( net . minecraft . item . ItemStack Item )
	{
		return ( "Carriage Spectre" ) ;
	}

	@Override
	public void AddTooltip ( net . minecraft . item . ItemStack Item , java . util . List TooltipLines )
	{
		TooltipLines . add ( "NOT VALID FOR DIRECT USAGE" ) ;
	}
}
