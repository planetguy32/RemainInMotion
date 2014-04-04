package JAKJ . RedstoneInMotion ;

public abstract class Stack
{
	public static net . minecraft . item . ItemStack Tag ( net . minecraft . item . ItemStack Item )
	{
		Item . stackTagCompound = new net . minecraft . nbt . NBTTagCompound ( "tag" ) ;

		return ( Item ) ;
	}

	public static net . minecraft . item . ItemStack Resize ( net . minecraft . item . ItemStack Item , int Quantity )
	{
		Item . stackSize = Quantity ;

		return ( Item ) ;
	}

	public static net . minecraft . item . ItemStack New ( int Id , int Damage , int Quantity )
	{
		return ( new net . minecraft . item . ItemStack ( Id , Quantity , Damage ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( int Id , int Damage )
	{
		return ( New ( Id , Damage , 1 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( int Id )
	{
		return ( New ( Id , 0 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . item . Item Item , int Damage , int Quantity )
	{
		return ( New ( Item . itemID , Damage , Quantity ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . item . Item Item , Enum Type , int Quantity )
	{
		return ( New ( Item , Type . ordinal ( ) , Quantity ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . item . Item Item , int Damage )
	{
		return ( New ( Item , Damage , 1 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . item . Item Item , Enum Type )
	{
		return ( New ( Item , Type , 1 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . item . Item Item )
	{
		return ( New ( Item , 0 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . block . Block Block , int Damage , int Quantity )
	{
		return ( New ( Block . blockID , Damage , Quantity ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . block . Block Block , Enum Type , int Quantity )
	{
		return ( New ( Block , Type . ordinal ( ) , Quantity ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . block . Block Block , int Damage )
	{
		return ( New ( Block , Damage , 1 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . block . Block Block , Enum Type )
	{
		return ( New ( Block , Type , 1 ) ) ;
	}

	public static net . minecraft . item . ItemStack New ( net . minecraft . block . Block Block )
	{
		return ( New ( Block , 0 ) ) ;
	}
}
