package me.planetguy.remaininmotion ;

public abstract class CarriagePackageBlacklist
{
	public static java . util . HashSet < Integer > BlacklistedIds = new java . util . HashSet < Integer > ( ) ;

	public static java . util . HashSet < Integer > BlacklistedIdAndMetaPairs = new java . util . HashSet < Integer > ( ) ;

	public static void Add ( int Id )
	{
		BlacklistedIds . add ( Id ) ;
	}

	public static void Add ( int Id , int Meta )
	{
		BlacklistedIdAndMetaPairs . add ( ( Id << 4 ) | Meta ) ;
	}

	public static boolean Lookup ( BlockRecord Block )
	{
		if ( BlacklistedIds . contains ( Block . Id ) )
		{
			return ( true ) ;
		}

		if ( BlacklistedIdAndMetaPairs . contains ( ( Block . Id << 4 ) | Block . Meta ) )
		{
			return ( true ) ;
		}

		return ( false ) ;
	}

	public static void Initialize ( )
	{
		Add ( Blocks . Spectre . blockID ) ;

		if ( Configuration . Carriage . BlacklistBedrock )
		{
			Add ( net . minecraft . block . Block . bedrock . blockID ) ;
		}

		if ( Configuration . Carriage . BlacklistByPiston )
		{
			Add ( net . minecraft . block . Block . obsidian . blockID ) ;

			for ( net . minecraft . block . Block Block : net . minecraft . block . Block . blocksList )
			{
				if ( Block == null )
				{
					continue ;
				}

				if ( Block . blockHardness < 0 )
				{
					Add ( Block . blockID ) ;

					continue ;
				}

				if ( Block . getMobilityFlag ( ) == 2 )
				{
					Add ( Block . blockID ) ;

					continue ;
				}
			}
		}
	}
}
