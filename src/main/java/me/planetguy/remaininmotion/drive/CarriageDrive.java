package me.planetguy.remaininmotion.drive ;

import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.base.RIMBlock;
import me.planetguy.remaininmotion.core.Core;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class CarriageDrive extends RIMBlock
{
	public CarriageDrive ( )
	{
		super ( Blocks.iron_block , CarriageDriveItem . class , HarvestToolTypes . Pickaxe ,
			
				CarriageEngineEntity . class , 
				CarriageMotorEntity . class , 
				Core . CarriageControllerEntity , 
				CarriageTranslocatorEntity . class,
				CarriageTransduplicatorEntity.class,
				CarriageAdapterEntity.class) ;
	}

	public enum Types
	{
		Engine(1.0),
		Motor(1.01) ,
		Controller(1.1) ,
		Translocator(4.0),
		Transduplicator(0.0),
		Adapter(1.0);

		public IIcon NormalIcon ;

		public IIcon ContinuousIcon ;

		public IIcon NormalActiveIcon ;

		public IIcon ContinuousActiveIcon ;

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

	public static IIcon InactiveIcon ;

	public static IIcon DyeIconSet ;

	public static IIcon PublicIcon ;

	public static IIcon PrivateToSelfIcon ;

	public static IIcon PrivateToOtherIcon ;

	@Override
	public void AddShowcaseStacks ( java . util . List Showcase )
	{
		for ( Types Type : Types . values ( ) )
		{
			if ( ( Type == Types . Controller ) && ( Core . CarriageControllerEntity == null ) )
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
	public void registerBlockIcons ( IIconRegister IconRegister )
	{
		for ( Types Type : Types . values ( ) )
		{
			if ( Type == Types . Controller )
			{
				if ( Core . CarriageControllerEntity == null )
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
	public IIcon getIcon ( int Side , int Meta )
	{
		try
		{
			return ( Types . values ( ) [ Meta ] . NormalIcon ) ;
		}
		catch ( Throwable Throwable )
		{
			//Throwable . printStackTrace ( ) ; //Fix log spam with MapWriter

			return ( RIMBlocks . Spectre . getIcon ( 0 , 0 ) ) ;
		}
	}

	@Override
	public IIcon getIcon ( net . minecraft . world . IBlockAccess World , int X , int Y , int Z , int Side )
	{
		try
		{
			CarriageDriveEntity Drive = ( CarriageDriveEntity ) World . getTileEntity ( X , Y , Z ) ;

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

			return ( Blocks.iron_block.getIcon ( 0 , 0 ) ) ;
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
			CarriageDriveEntity cde=(  CarriageDriveEntity ) World . getTileEntity ( X , Y , Z );
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
	public void onNeighborBlockChange ( net . minecraft . world . World World , int X , int Y , int Z , Block Id )
	{
		try
		{
			( ( CarriageDriveEntity ) World . getTileEntity ( X , Y , Z ) ) . HandleNeighbourBlockChange ( ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}
	}
}
