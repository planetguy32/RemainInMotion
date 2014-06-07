package me.planetguy.remaininmotion ;

public class CarriageDrive extends Block
{
	public CarriageDrive ( )
	{
		super ( Configuration . BlockIds . CarriageDrive , blockIron , CarriageDriveItem . class , HarvestToolTypes . Pickaxe ,
			
				CarriageEngineEntity . class , 
				CarriageMotorEntity . class , 
				ModInteraction . ComputerCraft . CarriageControllerEntity , 
				CarriageTranslocatorEntity . class,
				CarriageTransduplicatorEntity.class) ;
	}

	public enum Types
	{
		Engine(1.0),
		Motor(1.01) ,
		Controller(1.1) ,
		Translocator(4.0),
		Transduplicator(4.0);

		public net . minecraft . util . Icon NormalIcon ;

		public net . minecraft . util . Icon ContinuousIcon ;

		public net . minecraft . util . Icon NormalActiveIcon ;

		public net . minecraft . util . Icon ContinuousActiveIcon ;

		public double MaxBurden =1000.0;

		public double EnergyConsumption ;
		
		private Types(double energy){
			this.EnergyConsumption=energy;
		}
	}

	public enum Tiers
	{
		wood (1.0,1.0);
		
		private Tiers(double burden, double power){
			this.EnergyConsumptionFactor=power;
			this.MaxBurdenFactor=burden;
		}

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
			//Throwable . printStackTrace ( ) ; //Fix log spam with MapWriter

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
			//Throwable . printStackTrace ( ) ;

			return ( Block.blockIron.getIcon ( 0 , 0 ) ) ;
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
			CarriageDriveEntity cde=(  CarriageDriveEntity ) World . getBlockTileEntity ( X , Y , Z );
			cde.lastUsingPlayer=Player;
			System.out.println("Put player in CDE");
			cde. HandleToolUsage ( Side , Player . isSneaking ( ) ) ;
			
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
