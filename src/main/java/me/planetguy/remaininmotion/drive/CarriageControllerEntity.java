/*
import me.planetguy.remaininmotion.CarriageMotionException
import me.planetguy.remaininmotion.CarriageObstructionException
import me.planetguy.remaininmotion.CarriagePackage
import me.planetguy.remaininmotion.Directions
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil
import net.minecraft.tileentity.TileEntity
import me.planetguy.remaininmotion.util.general.TComputerInterface._
import me.planetguy.util.ECIExpose

object Commands extends Enumeration {

	val move = new Commands()

	val anchored_move = new Commands()

	val check_anchored_move = new Commands()

	val unanchored_move = new Commands()

	val check_unanchored_move = new Commands()

	class Commands extends Val

	implicit def convertValue(v: Value): Commands = v.asInstanceOf[Commands]
}

class CarriageControllerEntity extends CarriageDriveEntity with EasyComputerInterfaceUtil {

  var Simulating: Boolean = _

  var MotionDirection: Directions = _

  var Error: CarriageMotionException = _

  var Obstructed: Boolean = _

  var ObstructionX: Int = _

  var ObstructionY: Int = _

  var ObstructionZ: Int = _
  
  val motionThread=java.util.concurrent.Executors.newSingleThreadExecutor();

  override def HandleToolUsage(Side: Int, Sneaking: Boolean) {
  }

  override def updateEntity() {
    this.synchronized {
      if (worldObj.isRemote) {
        return
      }
      if (Stale) {
        HandleNeighbourBlockChange()
      }
      if (MotionDirection == null) {
        return
      }
      try {
        Move()
      } catch {
        case error: CarriageMotionException => {
          this.Error = error
          if (Error.isInstanceOf[CarriageObstructionException]) {
            Obstructed = true
            ObstructionX = Error.asInstanceOf[CarriageObstructionException].X
            ObstructionY = Error.asInstanceOf[CarriageObstructionException].Y
            ObstructionZ = Error.asInstanceOf[CarriageObstructionException].Z
          }
        }
      }
      MotionDirection = null
      notify()
    }
  }

  var anchored: Boolean = _

  override def Anchored(): Boolean = (anchored)

  def AssertArgumentCount(Arguments: Array[Any], ArgumentCount: Int) {
    if (Arguments.length < ArgumentCount) {
      throw (new Exception("too few arguments"))
    }
    if (Arguments.length > ArgumentCount) {
      throw (new Exception("too many arguments"))
    }
  }

  def ParseBooleanArgument(Argument: Any, Label: String): Boolean = {
    Argument.asInstanceOf[java.lang.Boolean]
  }

  def ParseDirectionArgument(Argument: Any): Directions = {
    if (Argument.isInstanceOf[java.lang.Double]) {
      return (Directions.values()(Math.round(Argument.asInstanceOf[java.lang.Double]).toInt))
    }
    try {
      val Direction = Argument.asInstanceOf[String]
      if (Direction.equalsIgnoreCase("down") || Direction.equalsIgnoreCase("negy")) {
        return (Directions.NegY)
      }
      if (Direction.equalsIgnoreCase("up") || Direction.equalsIgnoreCase("posy")) {
        return (Directions.PosY)
      }
      if (Direction.equalsIgnoreCase("north") || Direction.equalsIgnoreCase("negz")) {
        return (Directions.NegZ)
      }
      if (Direction.equalsIgnoreCase("south") || Direction.equalsIgnoreCase("posz")) {
        return (Directions.PosZ)
      }
      if (Direction.equalsIgnoreCase("west") || Direction.equalsIgnoreCase("negx")) {
        return (Directions.NegX)
      }
      if (Direction.equalsIgnoreCase("east") || Direction.equalsIgnoreCase("posx")) {
        return (Directions.PosX)
      }
    } catch {
      case throwable: Throwable => 
    }
    throw (new Exception("invalid direction"))
  }

  def SetupMotion(MotionDirection: Directions, Simulating: Boolean, anchored: Boolean) {
	  this.synchronized{
		  this.Simulating = Simulating
		  this.anchored = anchored
		  this.MotionDirection = MotionDirection
	  }
  }

  @ECIExpose
  def move(arguments: Array[Any]): Array[Any] = {
	var ret: Array[Any]=null
	motionThread.submit(new Runnable(){
		def run(){
    AssertArgumentCount(arguments, 3)
    SetupMotion(ParseDirectionArgument(arguments(0)), ParseBooleanArgument(arguments(1), "simulation"), ParseBooleanArgument(arguments(2), "anchoring"))
    Error = null
    Obstructed = false
    try {
      while (MotionDirection != null) {
        wait()
      }
    } catch {
      case exc: Exception => 
    }
    if (Error == null) {
      ret= (Array(true))
    }
    if (Obstructed == false) {
      ret= (Array(false, Error.getMessage))
    }
    ret=(Array(false, Error.getMessage, ObstructionX, ObstructionY, ObstructionZ))
  }
	})
	ret
  }

  def Move() {
    if (Active) {
      throw (new CarriageMotionException("controller already active"))
    }
    if (CarriageDirection == null) {
      throw (new CarriageMotionException("no carriage or too many carriages attached to controller"))
    }
    val Package = PreparePackage(MotionDirection)
    if (Simulating) {
      return
    }
    InitiateMotion(Package)
  }

  override def GeneratePackage(carriage: TileEntity, CarriageDirection: Directions, MotionDirection: Directions): CarriagePackage = {
    var Package: CarriagePackage = null
    if (Anchored) {
      if (MotionDirection == CarriageDirection) {
        throw (new CarriageMotionException("cannot push carriage away from controller in anchored mode"))
      }
      if (MotionDirection == CarriageDirection.Opposite()) {
        throw (new CarriageMotionException("cannot pull carriage into controller in anchored mode"))
      }
      Package = new CarriagePackage(this, carriage, MotionDirection)
      MultiTypeCarriageUtil.fillPackage(Package, carriage)
      if (Package.Body.contains(Package.DriveRecord)) {
        throw (new CarriageMotionException("carriage is attempting to move controller while in anchored mode"))
      }
      if (Package.Body.contains(Package.DriveRecord.NextInDirection(MotionDirection.Opposite()))) {
        throw (new CarriageMotionException("carriage is obstructed by controller while in anchored mode"))
      }
    } else {
      Package = new CarriagePackage(this, carriage, MotionDirection)
      Package.AddBlock(Package.DriveRecord)
      if (MotionDirection != CarriageDirection) {
        Package.AddPotentialObstruction(Package.DriveRecord.NextInDirection(MotionDirection))
      }
      MultiTypeCarriageUtil.fillPackage(Package, carriage)
    }
    Package.Finalize()
    (Package)
  }
  
  


Original Java:*/
package me.planetguy.remaininmotion.drive ;

import java.lang.reflect.Method;
import java.util.ArrayList;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.registry.LanguageRegistry;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.SimpleComponent;
import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriageObstructionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.core.Mod;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import me.planetguy.remaininmotion.util.general.ECIExpose;
import net.minecraft.tileentity.TileEntity;

@Optional.InterfaceList(value = { 	
		@Optional.Interface(iface="dan200.computercraft.api.peripheral.IPeripheral", modid="ComputerCraft"),
		@Optional.Interface(iface = "li.cil.oc.api.network.ManagedPeripheral", modid = "OpenComputers"),
		@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")})
public class CarriageControllerEntity extends CarriageDriveEntity implements 
IPeripheral,
SimpleComponent, ManagedPeripheral
{
	
	public boolean lastMoveWorked;
	
	public boolean Simulating ;

	public Directions MotionDirection ;

	public CarriageMotionException Error ;

	public boolean Obstructed ;

	public int ObstructionX ;
	public int ObstructionY ;
	public int ObstructionZ ;
	
	private volatile boolean finishedMoving=false;

	@Override
	public void HandleToolUsage ( int Side , boolean Sneaking )
	{
		
	}

	@Override
	public void updateEntity ( )
	{
		synchronized(this){
			
			if ( worldObj . isRemote )
			{
				return ;
			}
			
			if ( CooldownRemaining > 0 )
			{
				CooldownRemaining -- ;

				MarkServerRecordDirty ( ) ;

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

			lastMoveWorked=true;
			
			try
			{
				Move ( ) ;
			}
			catch ( CarriageMotionException Error )
			{
				lastMoveWorked=false;
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
			notify();
		}

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

		throw ( new Exception ( "invalid direction "+ Argument ) ) ;
	}

	public void SetupMotion ( Directions MotionDirection , boolean Simulating , boolean Anchored )
	{
		synchronized(this){
			this . MotionDirection = MotionDirection ;

			this . Simulating = Simulating ;

			this . Anchored = Anchored ;
			
			lastMoveWorked=false;

			this.notify();
		}
	}

	/**
	 * Runs in computer threads - be careful of thread safety
	 */
	@ECIExpose
	public Object[] move(Object[] Arguments ) throws Exception{

		AssertArgumentCount ( Arguments , 3 ) ;

		SetupMotion ( ParseDirectionArgument ( Arguments [ 0 ] ) , ParseBooleanArgument ( Arguments [ 1 ] , "simulation" ) , ParseBooleanArgument ( Arguments [ 2 ] , "anchoring" ) ) ;

		Error = null ;

		Obstructed = false ;

		return new Object[0];
	}
	
	@ECIExpose
	public Object[] status(Object[] args){
		synchronized(this){
			if(Obstructed)
				return new Object[]{
					this.energyStored,
					this.lastMoveWorked,
					Error.getMessage(),
					ObstructionX , ObstructionY , ObstructionZ
			};
			else if(Error==null){
				return new Object[]{this.energyStored,this.lastMoveWorked};
			}else{
				return new Object[]{
						this.energyStored,
						this.lastMoveWorked,
						Error.getMessage()
				};
			}
		}
		
	}
	
	@Override
	public void removeUsedEnergy(CarriagePackage _package) throws CarriageMotionException{
		//use energy iff not just simulating motion
		if(!Simulating)
			super.removeUsedEnergy(_package);
	}


	public void Move ( ) throws CarriageMotionException
	{
		if ( Active || CooldownRemaining>0)
		{
			throw ( new CarriageMotionException ( Lang.translate(Mod.Handle+".active") ) ) ;
		}

		if ( CarriageDirection == null )
		{
			throw ( new CarriageMotionException ( Lang.translate(Mod.Handle+".noValidCarriage") ) ) ;
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
				throw ( new CarriageMotionException ( Lang.translate(Mod.Handle+".noPushWhenAnchored") ) ) ;
			}

			if ( MotionDirection == CarriageDirection . Opposite ( ) )
			{
				throw ( new CarriageMotionException ( Lang.translate(Mod.Handle+".noPullWhenAnchored") ) ) ;
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
	
	public String type(){
		return "carriage";
	}
	
	public Method[] listMethods(){
		ArrayList<Method> methods=new ArrayList<Method>();
		for(Method m:this.getClass().getMethods()){
			if(m.isAnnotationPresent(ECIExpose.class)){
				methods.add(m);
			}
		}
		return methods.toArray(new Method[0]);
	}
	
	public String[] getMethodNames() {
		Method[] methods=listMethods();
		String[] names=new String[methods.length];
		for(int i=0; i<names.length; i++){
			names[i]=methods[i].getName();
		}
		return names;
	}
	
	/* =====================================
	 * ComputerCraft integration
	 * =====================================
	 */
	
	@Override
	public String getType() {
		Debug.mark();
		return type();
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context,
			int method, Object[] arguments) throws LuaException,
			InterruptedException {
		try{
			Method m=listMethods()[method];
			return (Object[]) m.invoke(this, new Object[]{arguments});
		}catch(Exception e){
			e.printStackTrace();
			throw new LuaException(e.getLocalizedMessage());
		}
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
	
	/*
	 * OpenComputers integration
	 */
	
    @Override
    public String getComponentName() {
        return type();
    }

	@Override
	public String[] methods() {
		return getMethodNames();
	}

	@Override
	@Optional.Method(modid="OpenComputers")
	public Object[] invoke(String method, Context context, Arguments args)
			throws Exception {
		for(Method m:listMethods()){
			if(m.getName().equals(method)){
				return (Object[]) m.invoke(this, new Object[]{args.toArray()});
			}
		}
		throw new NoSuchMethodException(method);
	}
	
}

