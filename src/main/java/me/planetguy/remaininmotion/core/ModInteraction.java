package me.planetguy.remaininmotion.core ;

import me.planetguy.remaininmotion.CarriageControllerEntity;
import me.planetguy.remaininmotion.util.Reflection;

public abstract class ModInteraction
{
	public abstract static class OmniTools
	{
		public static Class ItemWrench ;

		public static void Establish ( )
		{
			ItemWrench = Reflection . EstablishClass ( "omnitools.item.ItemWrench" ) ;
		}
	}

	public abstract static class ComputerCraft
	{
		public static Class carriageControllerEntity ;

		public static void Establish ( )
		{
			carriageControllerEntity = CarriageControllerEntity.class;
		}
	}

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
			MultipartSaveLoad = Reflection . EstablishClass ( "codechicken.multipart.handler.MultipartSaveLoad" ) ;

			MultipartSaveLoad_loadingWorld_$eq = Reflection . EstablishMethod ( MultipartSaveLoad , "loadingWorld_$eq" , net . minecraft . world . World . class ) ;

			MultipartSPH = Reflection . EstablishClass ( "codechicken.multipart.handler.MultipartSPH" ) ;

			MultipartSPH_onChunkWatch = Reflection . EstablishMethod ( MultipartSPH , "onChunkWatch" , net . minecraft . entity . player . EntityPlayer . class , net . minecraft . world . chunk . Chunk . class ) ;

			TileMultipart = Reflection . EstablishClass ( "codechicken.multipart.TileMultipart" ) ;

			TileMultipart_createFromNBT = Reflection . EstablishMethod ( TileMultipart , "createFromNBT" , net . minecraft . nbt . NBTTagCompound . class ) ;

			TileMultipart_onChunkLoad = Reflection . EstablishMethod ( TileMultipart , "onChunkLoad" ) ;

			MultipartHelper = Reflection . EstablishClass ( "codechicken.multipart.MultipartHelper" ) ;

			MultipartHelper_createTileFromNBT = Reflection . EstablishMethod ( MultipartHelper , "createTileFromNBT" , net . minecraft . world . World . class , net . minecraft . nbt . NBTTagCompound . class ) ;

			MultipartHelper_sendDescPacket = Reflection . EstablishMethod ( MultipartHelper , "sendDescPacket" , net . minecraft . world . World . class , net . minecraft . tileentity . TileEntity . class ) ;
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
		OmniTools . Establish ( ) ;

		ComputerCraft . Establish ( ) ;

		ForgeMultipart . Establish ( ) ;

		{
			PendingBlockUpdateSetField = Reflection . EstablishField ( net . minecraft . world . WorldServer . class , "pendingTickListEntriesHashSet" ) ;

			RemovePendingBlockUpdate = Reflection . EstablishMethod ( net . minecraft . world . WorldServer . class , "removeNextTickIfNeeded" , net . minecraft . world . NextTickListEntry . class ) ;
		}

		{
			BC_TileGenericPipe = Reflection . EstablishClass ( "buildcraft.transport.TileGenericPipe" ) ;

			BC_TileGenericPipe_pipe = Reflection . EstablishField ( BC_TileGenericPipe , "pipe" ) ;

			BC_TileGenericPipe_initialize = Reflection . EstablishMethod ( BC_TileGenericPipe , "initialize" , BC_Pipe ) ;

			BC_Pipe = Reflection . EstablishClass ( "buildcraft.transport.Pipe" ) ;

			BC_Pipe_transport = Reflection . EstablishField ( BC_Pipe , "transport" ) ;

			BC_PipeTransportItems = Reflection . EstablishClass ( "buildcraft.transport.PipeTransportItems" ) ;

			BC_PipeTransportItems_delay = Reflection . EstablishField ( BC_PipeTransportItems , "delay" ) ;

			BC_PipeTransportItems_delayedEntitiesToLoad = Reflection . EstablishField ( BC_PipeTransportItems , "delayedEntitiesToLoad" ) ;

			BC_PipeTransportItems_travelingEntities = Reflection . EstablishField ( BC_PipeTransportItems , "travelingEntities" ) ;

			BC_EntityData = Reflection . EstablishClass ( "buildcraft.transport.EntityData" ) ;

			BC_EntityData_item = Reflection . EstablishField ( BC_EntityData , "item" ) ;

			BC_EntityPassiveItem = Reflection . EstablishClass ( "buildcraft.core.EntityPassiveItem" ) ;

			BC_EntityPassiveItem_setWorld = Reflection . EstablishMethod ( BC_EntityPassiveItem , "setWorld" , net . minecraft . world . World . class ) ;

			BC_EntityPassiveItem_getEntityId = Reflection . EstablishMethod ( BC_EntityPassiveItem , "getEntityId" ) ;

			BC_EntityPassiveItem_position = Reflection . EstablishField ( BC_EntityPassiveItem , "position" ) ;

			BC_Position = Reflection . EstablishClass ( "buildcraft.api.core.Position" ) ;

			BC_Position_x = Reflection . EstablishField ( BC_Position , "x" ) ;

			BC_Position_y = Reflection . EstablishField ( BC_Position , "y" ) ;

			BC_Position_z = Reflection . EstablishField ( BC_Position , "z" ) ;
		}
	}
}
