package me.planetguy.remaininmotion ;

import cpw.mods.fml.common.Optional;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.world.World;

@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "ComputerCraft")
public class CarriageControllerEntity extends CarriageDriveEntity implements dan200.computercraft.api.peripheral.IPeripheral
{
	
	static{
		IPeripheralProvider ipp=new CarriageControllerPeripheralProvider();
		try {
			Reflection.EstablishMethod(Reflection.EstablishClass("dan200.computercraft.api.ComputerCraftAPI"), "registerPeripheralProvider", Class.forName("dan200.computercraft.api.peripheral.IPeripheralProvider")).invoke(null, ipp);
		} catch (Exception e) {
			System.out.println("Error enabling ComputerCraft integration! It probably won't work!");
			e.printStackTrace();
		}
	}
	
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

	@Override
	public String getType ( )
	{
		return ( "JAKJ_RIM_CarriageController" ) ;
	}

	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheralProvider", modid = "ComputerCraft")
	private static final class CarriageControllerPeripheralProvider implements
			IPeripheralProvider {
		@Override
		public IPeripheral getPeripheral(World world, int x, int y, int z,
				int side) {
			try{
				return (IPeripheral) world.getTileEntity(x, y, z);
			}catch(ClassCastException e){
				return null;
			}
		}
	}

	public enum Commands
	{
		move ,
		anchored_move ,
		check_anchored_move ,
		unanchored_move ,
		check_unanchored_move ;
	}

	@Override
	public String [ ] getMethodNames ( )
	{
		int CommandCount = Commands . values ( ) . length ;

		String [ ] CommandNames = new String [ CommandCount ] ;

		for ( int Index = 0 ; Index < CommandCount ; Index ++ )
		{
			CommandNames [ Index ] = Commands . values ( ) [ Index ] . name ( ) ;
		}

		return ( CommandNames ) ;
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

    public Object[] callMethod( IComputerAccess computer, ILuaContext context, int MethodIndex, Object[] Arguments ) throws Exception{
		try
		{
			switch ( Commands . values ( ) [ MethodIndex ] )
			{
				case move :

					AssertArgumentCount ( Arguments , 3 ) ;

					SetupMotion ( ParseDirectionArgument ( Arguments [ 0 ] ) , ParseBooleanArgument ( Arguments [ 1 ] , "simulation" ) , ParseBooleanArgument ( Arguments [ 2 ] , "anchoring" ) ) ;

					break ;

				case anchored_move :

					AssertArgumentCount ( Arguments , 1 ) ;

					SetupMotion ( ParseDirectionArgument ( Arguments [ 0 ] ) , false , true ) ;

					break ;

				case check_anchored_move :

					AssertArgumentCount ( Arguments , 1 ) ;

					SetupMotion ( ParseDirectionArgument ( Arguments [ 0 ] ) , true , true ) ;

					break ;

				case unanchored_move :

					AssertArgumentCount ( Arguments , 1 ) ;

					SetupMotion ( ParseDirectionArgument ( Arguments [ 0 ] ) , false , false ) ;

					break ;

				case check_unanchored_move :

					AssertArgumentCount ( Arguments , 1 ) ;

					SetupMotion ( ParseDirectionArgument ( Arguments [ 0 ] ) , true , false ) ;

					break ;
			}
		}
		catch ( Throwable Throwable )
		{
			throw ( new Exception ( "no such command" ) ) ;
		}

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
	public CarriagePackage GeneratePackage ( CarriageEntity Carriage , Directions CarriageDirection , Directions MotionDirection ) throws CarriageMotionException
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

			Package = new CarriagePackage ( this , Carriage , MotionDirection ) ;

			Carriage . FillPackage ( Package ) ;

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
			Package = new CarriagePackage ( this , Carriage , MotionDirection ) ;

			Package . AddBlock ( Package . DriveRecord ) ;

			if ( MotionDirection != CarriageDirection )
			{
				Package . AddPotentialObstruction ( Package . DriveRecord . NextInDirection ( MotionDirection ) ) ;
			}

			Carriage . FillPackage ( Package ) ;
		}

		Package . Finalize ( ) ;

		return ( Package ) ;
	}

	@Override
	public void attach(IComputerAccess computer) {
		
	}

	@Override
	public void detach(IComputerAccess computer) {
		
	}

	@Override
	public boolean equals(IPeripheral other) {
		return other==this;
	}
	
}
