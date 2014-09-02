package me.planetguy.util

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.HashMap
import cpw.mods.fml.common.Optional
import li.cil.oc.api.network.Arguments
import li.cil.oc.api.network.Component
import li.cil.oc.api.network.Context
import li.cil.oc.api.network.ManagedPeripheral
import li.cil.oc.api.network.SimpleComponent
import dan200.computercraft.api.lua.ILuaContext
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.peripheral.IPeripheral
import TComputerInterface._
import scala.collection.JavaConversions._
import me.planetguy.util.TComputerInterface

object TComputerInterface {

  def getClassNameInLuaFormat(o: AnyRef): String = {
    val className = o.getClass.getSimpleName
    val b = new StringBuilder()
    for (c <- className.toCharArray()) {
      if (java.lang.Character.isUpperCase(c)) {
        b.append("_" + java.lang.Character.toLowerCase(c))
      } else {
        b.append(c)
      }
    }
    b.toString
  }

  object EasyComputerInterfaceUtil {

    private val globalMap: HashMap[Class[_], HashMap[String, Method]] = new HashMap[Class[_], HashMap[String, Method]]()

    def initialize(c: Class[_]): HashMap[String, Method] = {
      if (globalMap.containsKey(c)) globalMap.get(c) else {
        val methodsMap = new HashMap[String, Method]()
        for (m <- c.getMethods if m.isAnnotationPresent(classOf[ECIExpose])) methodsMap.put(m.getName, m)
        globalMap.put(c, methodsMap)
        methodsMap
      }
    }
  }

  trait EasyComputerInterfaceUtil extends TComputerInterface {

    private val methodsMap: HashMap[String, Method] = EasyComputerInterfaceUtil.initialize(this.getClass)

    override def getProvidedMethods(): Array[String] = {
      methodsMap.keySet.toArray(Array.ofDim[String](0))
    }

    override def runMethod(method: String, arguments: Array[Object]): Array[Object] = {
      methodsMap.get(method).invoke(EasyComputerInterfaceUtil.this, arguments).asInstanceOf[Array[Object]]
    }
  }
}

@Optional.InterfaceList(
    Array(new Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers"),
        new Optional.Interface(iface = "li.cil.oc.api.network.ManagedPeripheral", modid = "OpenComputers"), 
        new Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "ComputerCraft")))
trait TComputerInterface extends IPeripheral with SimpleComponent with ManagedPeripheral {

  private val luaFormattedName: String = getClassNameInLuaFormat(this)

  /*
   * CC API implementation
   */
  
  override def getType(): String = luaFormattedName

  override def getMethodNames(): Array[String] = getProvidedMethods

  @Optional.Method(modid = "ComputerCraft")
  override def callMethod(computer: IComputerAccess, context: ILuaContext, method: Int, arguments: Array[Object]): Array[Object] = {
    runMethod(getMethodNames()(method), arguments)
  }

  @Optional.Method(modid = "ComputerCraft")
  override def attach(computer: IComputerAccess) {
  }

  @Optional.Method(modid = "ComputerCraft")
  override def detach(computer: IComputerAccess) {
  }

  @Optional.Method(modid = "ComputerCraft")
  override def equals(other: IPeripheral): Boolean = this == other

  /*
   * OC API implementation
   */
  
  override def methods(): Array[String] = getProvidedMethods

  @Optional.Method(modid = "OpenComputers")
  override def invoke(method: String, context: Context, args: Arguments): Array[Object] = runMethod(method, toObjectArray(args))

  override def getComponentName(): String = luaFormattedName

  @Optional.Method(modid = "OpenComputers")
  private def toObjectArray(args: Arguments): Array[Object] = {
    val objs = Array.ofDim[Object](args.count())
    for (i <- 0 until objs.length) {
      objs(i) = if (args.isBoolean(i)) args.checkBoolean(i).asInstanceOf[Object] else if (args.isDouble(i)) args.checkDouble(i).asInstanceOf[Object] else if (args.isString(i)) args.checkString(i) else null
    }
    objs
  }

  /*
   * Our own API
   */
  
  def getProvidedMethods(): Array[String]

  def runMethod(method: String, arguments: Array[Object]): Array[Object]
}
