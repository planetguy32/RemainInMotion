package me.planetguy.remaininmotion ;

import net.minecraft.tileentity.TileEntity;

public class CarriageTransduplicatorEntity extends CarriageTranslocatorEntity
{
	public String Player ;

	public int Label ;

	public static java . util . HashMap < String , java . util . HashMap < Integer , java . util . LinkedList < BlockPosition > > > ActiveTranslocatorSets
		= new java . util . HashMap < String , java . util . HashMap < Integer , java . util . LinkedList < BlockPosition > > > ( ) ;

	public void RegisterLabel ( )
	{
		java . util . HashMap < Integer , java . util . LinkedList < BlockPosition > > ActiveTranslocatorSet = ActiveTranslocatorSets . get ( Player ) ;

		if ( ActiveTranslocatorSet == null )
		{
			ActiveTranslocatorSet = new java . util . HashMap < Integer , java . util . LinkedList < BlockPosition > > ( ) ;

			ActiveTranslocatorSets . put ( Player , ActiveTranslocatorSet ) ;
		}

		java . util . LinkedList < BlockPosition > ActiveTranslocators = ActiveTranslocatorSet . get ( Label ) ;

		if ( ActiveTranslocators == null )
		{
			ActiveTranslocators = new java . util . LinkedList < BlockPosition > ( ) ;

			ActiveTranslocatorSet . put ( Label , ActiveTranslocators ) ;
		}

		ActiveTranslocators . add ( GeneratePositionObject ( ) ) ;
	}

	public void ClearLabel ( )
	{
		try
		{
			ActiveTranslocatorSets . get ( Player ) . get ( Label ) . remove ( GeneratePositionObject ( ) ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}
	}

	@Override
	public void Setup ( net . minecraft . entity . player . EntityPlayer Player , net . minecraft . item . ItemStack Item )
	{
		super . Setup ( Player , Item ) ;

		this . Player = CarriageDriveItem . GetPrivateFlag ( Item ) ? Player . username : "" ;
		
		Label = CarriageDriveItem . GetLabel ( Item ) ;

		if ( ! worldObj . isRemote )
		{

			RegisterLabel ( ) ;

			/* dirty hack needed for unknown reason */
			{
				ClearLabel ( ) ;

				RegisterLabel ( ) ;
			}
		}
	}

	@Override
	public void EmitDrops ( Block Block , int Meta )
	{
		EmitDrop ( Block , CarriageDriveItem . Stack ( Meta , Tier , ! Player . equals ( "" ) , Label ) ) ;
	}

	@Override
	public void Initialize ( )
	{
		super . Initialize ( ) ;

		if ( ! worldObj . isRemote )
		{
			if ( Player != null )
			{
				RegisterLabel ( ) ;
			}
		}
	}

	@Override
	public void Finalize ( )
	{
		if ( ! worldObj . isRemote )
		{
			ClearLabel ( ) ;
		}
	}

	@Override
	public void ReadCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		super . ReadCommonRecord ( TagCompound ) ;

		Player = TagCompound . getString ( "Player" ) ;
		
		Label = TagCompound . getInteger ( "Label" ) ;
	}

	@Override
	public void WriteCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		super . WriteCommonRecord ( TagCompound ) ;

		TagCompound . setString ( "Player" , Player ) ;

		TagCompound . setInteger ( "Label" , Label ) ;
	}

	@Override
	public boolean Anchored ( )
	{
		return ( true ) ;
	}

	@Override
	public CarriagePackage PreparePackage ( Directions MotionDirection ) throws CarriageMotionException
	{
		CarriagePackage Package = prepareDefaultPackage ( null ) ;

		CarriageTransduplicatorEntity Target = null ;

		java . util . LinkedList < BlockPosition > ActiveTranslocators ;
		
		try
		{
			ActiveTranslocators = ActiveTranslocatorSets . get ( Player ) . get ( Label ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			throw ( new CarriageMotionException ( "translocator array is corrupt" ) ) ;
		}

		for ( int Index = 0 ; Index < ActiveTranslocators . size ( ) ; Index ++ )
		{
			BlockPosition Position = ActiveTranslocators . get ( Index ) ;

			try
			{
				CarriageTransduplicatorEntity Translocator = ( CarriageTransduplicatorEntity ) WorldUtil . GetWorld ( Position . Dimension ) . getBlockTileEntity ( Position . X , Position . Y , Position . Z ) ;

				if ( Translocator == this )
				{
					continue ;
				}

				boolean TargetValid = true ;

				for ( BlockRecord Record : Package . NewPositions )
				{
					if ( ! Translocator . worldObj . isAirBlock ( Record . X + Translocator . xCoord , Record . Y + Translocator . yCoord , Record . Z + Translocator . zCoord ) )
					{
						TargetValid = false ;

						break ;
					}
				}

				if ( TargetValid )
				{
					Target = Translocator ;

					break ;
				}
			}
			catch ( Throwable Throwable )
			{
				Throwable . printStackTrace ( ) ;
			}
		}

		if ( Target == null )
		{
			throw ( new CarriageMotionException ( "no other matching translocators available with space to receive carriage assembly" ) ) ;
		}

		Package . Translocator = Target ;

		return ( Package ) ;
	}

	@Override
	public CarriagePackage GeneratePackage ( TileEntity carriage , Directions CarriageDirection , Directions MotionDirection ) throws CarriageMotionException
	{
		CarriagePackage Package = new CarriagePackage ( this , carriage , null ) ;

		TEAccessUtil.fillPackage(Package , carriage) ;

		if ( Package . Body . contains ( Package . DriveRecord ) )
		{
			throw ( new CarriageMotionException ( "carriage is attempting to grab translocator" ) ) ;
		}

		Package . Finalize ( ) ;

		return ( Package ) ;
	}

	@Override
	public void InitiateMotion ( CarriagePackage Package )
	{
		Package . Translocator . ToggleActivity ( ) ;

		super . InitiateMotion ( Package ) ;
	}

	@Override
	public void EstablishPlaceholders ( CarriagePackage Package )
	{
		for ( BlockRecord Record : Package . NewPositions )
		{
			//SneakyWorldUtil . SetBlock ( worldObj , Record . X + xCoord , Record . Y + yCoord , Record . Z + zCoord , Blocks . Spectre . blockID , Spectre . Types . Supportive . ordinal ( ) ) ;

			SneakyWorldUtil . SetBlock ( Package . Translocator . worldObj , Record . X + Package . Translocator . xCoord , Record . Y + Package . Translocator . yCoord , Record . Z + Package . Translocator . zCoord ,
				Blocks . Spectre . blockID , Spectre . Types . Supportive . ordinal ( ) ) ;
		}
	}

	@Override
	public void EstablishSpectre ( CarriagePackage Package )
	{
		WorldUtil . SetBlock ( worldObj , Package . AnchorRecord . X , Package . AnchorRecord . Y , Package . AnchorRecord . Z , Blocks . Spectre . blockID , Spectre . Types . Transduplicative . ordinal ( ) ) ;

		( ( TransduplicativeSpectreEntity ) worldObj . getBlockTileEntity ( Package . AnchorRecord . X , Package . AnchorRecord . Y , Package . AnchorRecord . Z ) ) . AbsorbSource ( Package ) ;

		int NewX = Package . AnchorRecord . X - xCoord + Package . Translocator . xCoord ;
		int NewY = Package . AnchorRecord . Y - yCoord + Package . Translocator . yCoord ;
		int NewZ = Package . AnchorRecord . Z - zCoord + Package . Translocator . zCoord ;

		WorldUtil . SetBlock ( Package . Translocator . worldObj , NewX , NewY , NewZ , Blocks . Spectre . blockID , Spectre . Types . Transduplicative . ordinal ( ) ) ;

		( ( TransduplicativeSpectreEntity ) Package . Translocator . worldObj . getBlockTileEntity ( NewX , NewY , NewZ ) ) . AbsorbSink ( Package ) ;
	}
}
