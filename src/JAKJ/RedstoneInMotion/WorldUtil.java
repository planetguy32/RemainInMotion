package JAKJ . RedstoneInMotion ;

public abstract class WorldUtil
{
	public static int OverworldId = 0 ;
	public static int NetherId = -1 ;
	public static int EndId = 1 ;

	public static net . minecraft . world . WorldServer GetWorld ( int Dimension )
	{
		return ( GameUtil . GetServer ( ) . worldServerForDimension ( Dimension ) ) ;
	}

	public static void ClearBlock ( net . minecraft . world . World World , int X , int Y , int Z )
	{
		SetBlock ( World , X , Y , Z , 0 , 0 ) ;
	}

	public static void SetBlock ( net . minecraft . world . World World , int X , int Y , int Z , int Id , int Meta )
	{
		World . setBlock ( X , Y , Z , Id , Meta , 0x3 ) ;
	}
}
