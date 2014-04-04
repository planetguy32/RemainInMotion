package JAKJ . RedstoneInMotion ;

public class CreativeTab extends net . minecraft . creativetab . CreativeTabs
{
	public CreativeTab ( )
	{
		super ( Mod . Handle ) ;
	}

	public static CreativeTab Instance ;

	public static void Prepare ( )
	{
		Instance = new CreativeTab ( ) ;
	}

	@Override
	public String getTranslatedTabLabel ( )
	{
		return ( Mod . Title ) ;
	}

	public int TabIconItemIndex ;

	public static void Initialize ( int TabIconItemIndex )
	{
		Instance . TabIconItemIndex = TabIconItemIndex ;
	}

	@Override
	public int getTabIconItemIndex ( )
	{
		return ( TabIconItemIndex ) ;
	}
}
