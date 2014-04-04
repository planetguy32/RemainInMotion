package JAKJ . RedstoneInMotion ;

public class CarriageDrive extends Block
{
	public CarriageDrive ( )
	{
		super ( Configuration . BlockIds . CarriageDrive , blockIron , CarriageDriveItem . class , HarvestToolTypes . Pickaxe ,
			CarriageEngineEntity . class , CarriageMotorEntity . class , ModInteraction . ComputerCraft . CarriageControllerEntity , CarriageTranslocatorEntity . class ) ;
	}

	public enum Types
	{
		Engine ,
		Motor ,
		Controller ,
		Translocator ;

		public net . minecraft . util . Icon NormalIcon ;

		public net . minecraft . util . Icon ContinuousIcon ;

		public net . minecraft . util . Icon NormalActiveIcon ;

		public net . minecraft . util . Icon ContinuousActiveIcon ;

		public double MaxBurden ;

		public double EnergyConsumption ;
	}

	public enum Tiers
	{
		foo ;

		public double MaxBurdenFactor ;

		public double EnergyConsumptionFactor ;
	}

	public static net . minecraft . util . Icon InactiveIcon ;

	public static net . minecraft . util . Icon DyeIconSet ;

	public static net . minecraft . util . Icon PublicIcon ;

	public static net . minecraft . util . Icon PrivateToSelfIcon ;

	public static net . minecraft . util . Icon PrivateToOtherIcon ;

	@Override
	public void AddShowcaseStacks ( java . util . List Showcase )
	{
		for ( Types Type : Types . values ( ) )
		{
			if ( ( Type == Types . Controller ) && ( ModInteraction . ComputerCraft . CarriageControllerEntity == null ) )
			{
				continue ;
			}

			if ( Type == Types . Translocator )
			{
				Showcase . add ( CarriageDriveItem . Stack ( Type . ordinal ( ) , 0 , false , 0 ) ) ;
			}
			else
			{
				Showcase . add ( CarriageDriveItem . Stack ( Type . ordinal ( ) , 0 ) ) ;
			}
		}
	}

	@Override
	public void registerIcons ( net . minecraft . client . renderer . texture . IconRegister IconRegister )
	{
		for ( Types Type : Types . values ( ) )
		{
			if ( Type == Types . Controller )
			{
				if ( ModInteraction . ComputerCraft . CarriageControllerEntity == null )
				{
					continue ;
				}
			}
			else
			{
				Type . ContinuousIcon = Registry . RegisterIcon ( IconRegister , "Carriage" + Type . name ( ) + "_Continuous" ) ;

				Type . ContinuousActiveIcon = Registry . RegisterIcon ( IconRegister , "Carriage" + Type . name ( ) + "_Continuous_Active" ) ;
			}

			Type . NormalIcon = Registry . RegisterIcon ( IconRegister , "Carriage" + Type . name ( ) ) ;

			Type . NormalActiveIcon = Registry . RegisterIcon ( IconRegister , "Carriage" + Type . name ( ) + "_Active" ) ;
		}

		InactiveIcon = Registry . RegisterIcon ( IconRegister , "CarriageDriveInactive" ) ;

		DyeIconSet = Registry . RegisterIcon ( IconRegister , "CarriageTranslocator_LabelDyes" ) ;

		PublicIcon = Registry . RegisterIcon ( IconRegister , "CarriageTranslocator_LabelPublic" ) ;

		PrivateToSelfIcon = Registry . RegisterIcon ( IconRegister , "CarriageTranslocator_LabelPrivateToSelf" ) ;

		PrivateToOtherIcon = Registry . RegisterIcon ( IconRegister , "CarriageTranslocator_LabelPrivateToOther" ) ;
	}

	@Override
	public net . minecraft . util . Icon getIcon ( int Side , int Meta )
	{
		try
		{
			return ( Types . values ( ) [ Meta ] . NormalIcon ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			return ( Blocks . Spectre . getIcon ( 0 , 0 ) ) ;
		}
	}

	@Override
	public net . minecraft . util . Icon getBlockTexture ( net . minecraft . world . IBlockAccess World , int X , int Y , int Z , int Side )
	{
		try
		{
			CarriageDriveEntity Drive = ( CarriageDriveEntity ) World . getBlockTileEntity ( X , Y , Z ) ;

			if ( Drive . SideClosed [ Side ] )
			{
				return ( InactiveIcon ) ;
			}

			Types Type = Types . values ( ) [ World . getBlockMetadata ( X , Y , Z ) ] ;

			if ( Drive . Continuous )
			{
				return ( Drive . Active ? Type . ContinuousActiveIcon : Type . ContinuousIcon ) ;
			}

			return ( Drive . Active ? Type . NormalActiveIcon : Type . NormalIcon ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			return ( Blocks . Spectre . getIcon ( 0 , 0 ) ) ;
		}
	}

	@Override
	public boolean onBlockActivated ( net . minecraft . world . World World , int X , int Y , int Z , net . minecraft . entity . player . EntityPlayer Player , int Side , float HitX , float HitY , float HitZ )
	{
		if ( World . isRemote )
		{
			return ( false ) ;
		}

		if ( ! ToolItemSet . IsScrewdriverOrEquivalent ( Player . inventory . getCurrentItem ( ) ) )
		{
			return ( false ) ;
		}

		try
		{
			( ( CarriageDriveEntity ) World . getBlockTileEntity ( X , Y , Z ) ) . HandleToolUsage ( Side , Player . isSneaking ( ) ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			return ( false ) ;
		}

		return ( true ) ;
	}

	@Override
	public void onNeighborBlockChange ( net . minecraft . world . World World , int X , int Y , int Z , int Id )
	{
		try
		{
			( ( CarriageDriveEntity ) World . getBlockTileEntity ( X , Y , Z ) ) . HandleNeighbourBlockChange ( ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}
	}
}
