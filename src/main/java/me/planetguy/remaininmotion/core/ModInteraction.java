package me.planetguy.remaininmotion.core ;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.item.ItemStack;
import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.Reflection;
import me.planetguy.remaininmotion.drive.CarriageControllerEntity;
import me.planetguy.remaininmotion.util.general.Computers;

public abstract class ModInteraction
{

	public abstract static class ForgeMultipart
	{
		public static Class MultipartSaveLoad ;
		public static java . lang . reflect . Method MultipartSaveLoad_loadingWorld_$eq ;

		public static Class MultipartSPH ;
		public static java . lang . reflect . Method MultipartSPH_onChunkWatch ;

		public static Class TileMultipart ;
		public static java . lang . reflect . Method TileMultipart_createFromNBT ;
		public static java . lang . reflect . Method TileMultipart_onChunkLoad ;

		public static Class MultipartHelper ;
		public static java . lang . reflect . Method MultipartHelper_createTileFromNBT ;
		public static java . lang . reflect . Method MultipartHelper_sendDescPacket ;

		public static void Establish ( )
		{
			MultipartSaveLoad = ModInteraction.getClass( "codechicken.multipart.handler.MultipartSaveLoad" ) ;

			MultipartSaveLoad_loadingWorld_$eq = getMethod ( MultipartSaveLoad , "loadingWorld_$eq" , net . minecraft . world . World . class ) ;

			MultipartSPH = ModInteraction.getClass ( "codechicken.multipart.handler.MultipartSPH" ) ;

			MultipartSPH_onChunkWatch = getMethod ( MultipartSPH , "onChunkWatch" , net . minecraft . entity . player . EntityPlayer . class , net . minecraft . world . chunk . Chunk . class ) ;

			TileMultipart = ModInteraction.getClass ( "codechicken.multipart.TileMultipart" ) ;

			TileMultipart_createFromNBT = getMethod ( TileMultipart , "createFromNBT" , net . minecraft . nbt . NBTTagCompound . class ) ;

			TileMultipart_onChunkLoad = getMethod ( TileMultipart , "onChunkLoad" ) ;

			MultipartHelper = ModInteraction.getClass ( "codechicken.multipart.MultipartHelper" ) ;

			MultipartHelper_createTileFromNBT = getMethod ( MultipartHelper , "createTileFromNBT" , net . minecraft . world . World . class , net . minecraft . nbt . NBTTagCompound . class ) ;

			MultipartHelper_sendDescPacket = getMethod ( MultipartHelper , "sendDescPacket" , net . minecraft . world . World . class , net . minecraft . tileentity . TileEntity . class ) ;
		}
	}

	public static java . lang . reflect . Field PendingBlockUpdateSetField ;
	public static java . lang . reflect . Method RemovePendingBlockUpdate ;

	public static Class BC_TileGenericPipe ;
	public static java . lang . reflect . Field BC_TileGenericPipe_pipe ;
	public static java . lang . reflect . Method BC_TileGenericPipe_initialize ;
	public static Class BC_Pipe ;
	public static java . lang . reflect . Field BC_Pipe_transport ;
	public static Class BC_PipeTransportItems ;
	public static java . lang . reflect . Field BC_PipeTransportItems_delay ;
	public static java . lang . reflect . Field BC_PipeTransportItems_delayedEntitiesToLoad ;
	public static java . lang . reflect . Field BC_PipeTransportItems_travelingEntities ;
	public static Class BC_EntityData ;
	public static java . lang . reflect . Field BC_EntityData_item ;
	public static Class BC_EntityPassiveItem ;
	public static java . lang . reflect . Method BC_EntityPassiveItem_setWorld ;
	public static java . lang . reflect . Method BC_EntityPassiveItem_getEntityId ;
	public static java . lang . reflect . Field BC_EntityPassiveItem_position ;
	public static Class BC_Position ;
	public static java . lang . reflect . Field BC_Position_x ;
	public static java . lang . reflect . Field BC_Position_y ;
	public static java . lang . reflect . Field BC_Position_z ;

	public static void Establish ( )
	{
		Wrenches.init();
		
		Computers . setup ( ) ;

		if(Computers.load)
			Core.CarriageControllerEntity = CarriageControllerEntity.class;
		
		ForgeMultipart . Establish ( ) ;
		
		{
			PendingBlockUpdateSetField = getField ( net . minecraft . world . WorldServer . class , "tickEntryQueue" ) ;

			RemovePendingBlockUpdate = getMethod ( net . minecraft . world . WorldServer . class , "removeNextTickIfNeeded" , net . minecraft . world . NextTickListEntry . class ) ;
		}

		{
			BC_TileGenericPipe = getClass ( "buildcraft.transport.TileGenericPipe" ) ;

			BC_TileGenericPipe_pipe = getField ( BC_TileGenericPipe , "pipe" ) ;

			BC_TileGenericPipe_initialize = getMethod ( BC_TileGenericPipe , "initialize" , BC_Pipe ) ;

			BC_Pipe = getClass ( "buildcraft.transport.Pipe" ) ;

			BC_Pipe_transport = getField ( BC_Pipe , "transport" ) ;

			BC_PipeTransportItems = getClass ( "buildcraft.transport.PipeTransportItems" ) ;

			BC_PipeTransportItems_delay = getField ( BC_PipeTransportItems , "delay" ) ;

			BC_PipeTransportItems_delayedEntitiesToLoad = getField ( BC_PipeTransportItems , "delayedEntitiesToLoad" ) ;

			BC_PipeTransportItems_travelingEntities = getField ( BC_PipeTransportItems , "travelingEntities" ) ;

			BC_EntityData = getClass ( "buildcraft.transport.EntityData" ) ;

			BC_EntityData_item = getField ( BC_EntityData , "item" ) ;

			BC_EntityPassiveItem = getClass ( "buildcraft.core.EntityPassiveItem" ) ;

			BC_EntityPassiveItem_setWorld = getMethod ( BC_EntityPassiveItem , "setWorld" , net . minecraft . world . World . class ) ;

			BC_EntityPassiveItem_getEntityId = getMethod ( BC_EntityPassiveItem , "getEntityId" ) ;

			BC_EntityPassiveItem_position = getField ( BC_EntityPassiveItem , "position" ) ;

			BC_Position = getClass ( "buildcraft.api.core.Position" ) ;

			BC_Position_x = getField ( BC_Position , "x" ) ;

			BC_Position_y = getField ( BC_Position , "y" ) ;

			BC_Position_z = getField ( BC_Position , "z" ) ;
		}
	}
	
	static Class getClass(String string) {
		try {
			return Class.forName(string);
		}  catch (Exception e){
			
		}
		return null;
	}

	private static Method getMethod(Class c, String string, Class... params) {
		try {
			return c.getDeclaredMethod(string, params);
		} catch (Exception e){
			
		}
		return null;
	}

	private static Field getField(Class c, String string) {
		try {
			return c.getField(string);
		} catch (Exception e){
			
		}
		return null;
	}
	
	public static abstract class Wrenches{
		
		static ArrayList<Class> wrenchClasses=new ArrayList<Class>();
		
		
		public static void init(){
			for(String className:new String[]{
					//can add or remove FQCNs here
					//should probably prefer API interface names where possible
					//note that many mods implement BC wrench API only if BC is installed
					"buildcraft.api.tools.IToolWrench",				//Buildcraft
					"resonant.core.content.ItemScrewdriver",		//Resonant Induction
					"ic2.core.item.tool.ItemToolWrench",			//IC2
					"ic2.core.item.tool.ItemToolWrenchElectric",	//IC2 (more)
					"mrtjp.projectred.api.IScrewdriver",			//Project Red
					"mods.railcraft.api.core.items.IToolCrowbar",	//Railcraft
					"com.bluepowermod.items.ItemScrewdriver",		//BluePower
					"cofh.api.item.IToolHammer",					//Thermal Expansion and compatible
					"appeng.items.tools.quartz.ToolQuartzWrench",	//Applied Energistics
					"crazypants.enderio.api.tool.ITool",			//Ender IO
					"mekanism.api.IMekWrench",						//Mekanism
			}){
				try {
					wrenchClasses.add(Class.forName(className));
				} catch (ClassNotFoundException e) {
					Debug.dbg("Failed to load wrench class "+className);
				}
			}
		}
		
		public static boolean isAWrench(ItemStack stk){
			for(Class c:wrenchClasses){ //must iterate - testing isAssignableFrom, not equals
				if(stk !=null && stk.getItem() !=null && stk.getItem().getClass().isAssignableFrom(c))
					return true;
			}
			return false;
		}
		
	}
}
