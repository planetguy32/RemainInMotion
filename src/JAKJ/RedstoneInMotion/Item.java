package JAKJ . RedstoneInMotion ;

public abstract class Item extends net . minecraft . item . Item
{
	public Item ( int Id )
	{
		super ( Id ) ;

		setUnlocalizedName ( Mod . Handle + "_" + getClass ( ) . getSimpleName ( ) ) ;

		setHasSubtypes ( true ) ;

		setCreativeTab ( CreativeTab . Instance ) ;

		cpw . mods . fml . common . registry . GameRegistry . registerItem ( this , getUnlocalizedName ( ) , Mod . Handle ) ;
	}

	public void AddShowcaseStacks ( java . util . List Showcase )
	{
	}

	@Override
	public void getSubItems ( int Id , net . minecraft . creativetab . CreativeTabs CreativeTab , java . util . List Showcase )
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
	public boolean shouldPassSneakingClickToBlock ( net . minecraft . world . World World , int X , int Y , int Z )
	{
		return ( true ) ;
	}
}
