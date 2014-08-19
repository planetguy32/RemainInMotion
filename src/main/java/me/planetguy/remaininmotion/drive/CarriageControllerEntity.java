package me.planetguy.remaininmotion.drive ;

import me.planetguy.lib.cc.SPMethod;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriageObstructionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.tileentity.TileEntity;

public class CarriageControllerEntity extends CarriageDriveEntity
{
	
	public Object ThreadLockObject = new Object ( ) ;

	public boolean Simulating ;

	public Directions MotionDirection ;

	public CarriageMotionException Error ;

	public boolean Obstructed ;

	public int ObstructionX ;
	public int ObstructionY ;
	public int ObstructionZ ;

	@Override
	public void HandleToolUsage ( int Side , boolean Sneaking )
	{
		
	}

	@Override
	public synchronized void updateEntity ( )
	{
		if ( worldObj . isRemote )
		{
			return ;
		}

		if ( Stale )
		{
			HandleNeighbourBlockChange ( ) ;
		}

		if ( MotionDirection == null )
		{
			return ;
		}

		try
		{
			Move ( ) ;
		}
		catch ( CarriageMotionException Error )
		{
			this . Error = Error ;

			if ( Error instanceof CarriageObstructionException )
			{
				Obstructed = true ;

				ObstructionX = ( ( CarriageObstructionException ) Error ) . X ;
				ObstructionY = ( ( CarriageObstructionException ) Error ) . Y ;
				ObstructionZ = ( ( CarriageObstructionException ) Error ) . Z ;
			}
		}

		MotionDirection = null ;

		notify ( ) ;
	}

	public boolean Anchored ;

	@Override
	public boolean Anchored ( )
	{
		return ( Anchored ) ;
	}

	public enum Commands
	{
		move ,
		anchored_move ,
		check_anchored_move ,
		unanchored_move ,
		check_unanchored_move ;
	}

	public void AssertArgumentCount ( Object [ ] Arguments , int ArgumentCount ) throws Exception
	{
		if ( Arguments . length < ArgumentCount )
		{
			throw ( new Exception ( "too few arguments" ) ) ;
		}

		if ( Arguments . length > ArgumentCount )
		{
			throw ( new Exception ( "too many arguments" ) ) ;
		}
	}

	public boolean ParseBooleanArgument ( Object Argument , String Label ) throws Exception
	{
		try
		{
			return ( ( Boolean ) Argument ) ;
		}
		catch ( Throwable Throwable )
		{
			throw ( new Exception ( "invalid " + Label + " flag" ) ) ;
		}
	}

	public Directions ParseDirectionArgument ( Object Argument ) throws Exception
	{
		if ( Argument instanceof Double )
		{
			try
			{
				return ( Directions . values ( ) [ ( int ) Math . round ( ( Double ) Argument ) ] ) ;
			}
			catch ( Throwable Throwable )
			{
				throw ( new Exception ( "direction index out of range" ) ) ;
			}
		}

		try
		{
			String Direction = ( String ) Argument ;

			if ( Direction . equalsIgnoreCase ( "down" ) || Direction . equalsIgnoreCase ( "negy" ) )
			{
				return ( Directions . NegY ) ;
			}

			if ( Direction . equalsIgnoreCase ( "up" ) || Direction . equalsIgnoreCase ( "posy" ) )
			{
				return ( Directions. PosY ) ;
			}

			if ( Direction . equalsIgnoreCase ( "north" ) || Direction . equalsIgnoreCase ( "negz" ) )
			{
				return ( Directions . NegZ ) ;
			}

			if ( Direction . equalsIgnoreCase ( "south" ) || Direction . equalsIgnoreCase ( "posz" ) )
			{
				return ( Directions . PosZ ) ;
			}

			if ( Direction . equalsIgnoreCase ( "west" ) || Direction . equalsIgnoreCase ( "negx" ) )
			{
				return ( Directions . NegX ) ;
			}

			if ( Direction . equalsIgnoreCase ( "east" ) || Direction . equalsIgnoreCase ( "posx" ) )
			{
				return ( Directions . PosX ) ;
			}
		}
		catch ( Throwable Throwable )
		{
		}

		throw ( new Exception ( "invalid direction" ) ) ;
	}

	public void SetupMotion ( Directions MotionDirection , boolean Simulating , boolean Anchored )
	{
		this . MotionDirection = MotionDirection ;

		this . Simulating = Simulating ;

		this . Anchored = Anchored ;
	}

	@SPMethod
	public Object[] move(Object... Arguments ) throws Exception{

		AssertArgumentCount ( Arguments , 3 ) ;

		SetupMotion ( ParseDirectionArgument ( Arguments [ 0 ] ) , ParseBooleanArgument ( Arguments [ 1 ] , "simulation" ) , ParseBooleanArgument ( Arguments [ 2 ] , "anchoring" ) ) ;

		Error = null ;

		Obstructed = false ;

		try
		{
			while ( MotionDirection != null )
			{
				wait ( ) ;
			}
		
		}
		catch ( Exception exc )
		{
			//exc.printStackTrace();
		}

		if ( Error == null )
		{
			return ( new Object [ ] { true } ) ;
		}

		if ( Obstructed == false )
		{
			return ( new Object [ ] { false , Error . getMessage ( ) } ) ;
		}

		return ( new Object [ ] { false , Error . getMessage ( ) , ObstructionX , ObstructionY , ObstructionZ } ) ;
	}

	public void Move ( ) throws CarriageMotionException
	{
		if ( Active )
		{
			throw ( new CarriageMotionException ( "controller already active" ) ) ;
		}

		if ( CarriageDirection == null )
		{
			throw ( new CarriageMotionException ( "no carriage or too many carriages attached to controller" ) ) ;
		}

		CarriagePackage Package = PreparePackage ( MotionDirection ) ;

		if ( Simulating )
		{
			return ;
		}

		InitiateMotion ( Package ) ;
	}

	@Override
	public CarriagePackage GeneratePackage ( TileEntity carriage , Directions CarriageDirection , Directions MotionDirection ) throws CarriageMotionException
	{
		CarriagePackage Package ;

		if ( Anchored )
		{
			if ( MotionDirection == CarriageDirection )
			{
				throw ( new CarriageMotionException ( "cannot push carriage away from controller in anchored mode" ) ) ;
			}

			if ( MotionDirection == CarriageDirection . Opposite ( ) )
			{
				throw ( new CarriageMotionException ( "cannot pull carriage into controller in anchored mode" ) ) ;
			}

			Package = new CarriagePackage ( this , carriage , MotionDirection ) ;
			
			MultiTypeCarriageUtil.fillPackage(Package, carriage ) ;

			if ( Package . Body . contains ( Package . DriveRecord ) )
			{
				throw ( new CarriageMotionException ( "carriage is attempting to move controller while in anchored mode" ) ) ;
			}

			if ( Package . Body . contains ( Package . DriveRecord . NextInDirection ( MotionDirection . Opposite ( ) ) ) )
			{
				throw ( new CarriageMotionException ( "carriage is obstructed by controller while in anchored mode" ) ) ;
			}
		}
		else
		{
			Package = new CarriagePackage ( this , carriage , MotionDirection ) ;

			Package . AddBlock ( Package . DriveRecord ) ;

			if ( MotionDirection != CarriageDirection )
			{
				Package . AddPotentialObstruction ( Package . DriveRecord . NextInDirection ( MotionDirection ) ) ;
			}
			
			MultiTypeCarriageUtil.fillPackage(Package, carriage ) ;

		}

		Package . Finalize ( ) ;

		return ( Package ) ;
	}

}
