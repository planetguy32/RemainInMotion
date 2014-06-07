package me.planetguy.remaininmotion ;

public class TransduplicativeSpectreEntity extends TeleportativeSpectreEntity
{

	public void Release(){
		doRelease();
		for(BlockRecord r:Body)
			ShiftBlockPosition(r);
		doRelease();
	}
	
	public void doRelease ( )
	{
		for ( BlockRecord Record : Body )
		{

			SneakyWorldUtil . SetBlock ( worldObj , Record . X , Record . Y , Record . Z , Record . Id , Record . Meta ) ;
		}

		BlockRecordList PipesToInitialize = new BlockRecordList ( ) ;

		if ( ModInteraction . ForgeMultipart . MultipartSaveLoad_loadingWorld_$eq != null )
		{
			try
			{
				ModInteraction . ForgeMultipart . MultipartSaveLoad_loadingWorld_$eq . invoke ( null , worldObj ) ;
			}
			catch ( Throwable Throwable )
			{
				Throwable . printStackTrace ( ) ;
			}
		}

		BlockRecordList MultipartTilesToInitialize = new BlockRecordList ( ) ;

		java . util . HashMap < net . minecraft . world . chunk . Chunk , java . util . HashMap < Object , net . minecraft . tileentity . TileEntity > > MultipartTileSetsToPropagate =
			new java . util . HashMap < net . minecraft . world . chunk . Chunk , java . util . HashMap < Object , net . minecraft . tileentity . TileEntity > > ( ) ;

		for ( BlockRecord Record : Body )
		{
			if ( Record . EntityRecord != null )
			{
				Record . EntityRecord . setInteger ( "x" , Record . X ) ;
				Record . EntityRecord . setInteger ( "y" , Record . Y ) ;
				Record . EntityRecord . setInteger ( "z" , Record . Z ) ;

				if ( Record . EntityRecord . getString ( "id" ) . equals ( "savedMultipart") )
				{
					try
					{
						if ( ModInteraction . ForgeMultipart . MultipartHelper_createTileFromNBT != null )
						{
							Record . Entity = ( net . minecraft . tileentity . TileEntity ) ModInteraction . ForgeMultipart . MultipartHelper_createTileFromNBT
								. invoke ( null , worldObj , Record . EntityRecord ) ;
						}
						else
						{
							Record . Entity = ( net . minecraft . tileentity . TileEntity ) ModInteraction . ForgeMultipart . TileMultipart_createFromNBT . invoke ( null , Record . EntityRecord ) ;

							MultipartContainerBlockId = Record . Id ;

							net . minecraft . world . chunk . Chunk Chunk = worldObj . getChunkFromBlockCoords ( Record . X , Record . Z ) ;

							java . util . HashMap < Object , net . minecraft . tileentity . TileEntity > MultipartTilesToPropagate = MultipartTileSetsToPropagate . get ( Chunk ) ;

							if ( MultipartTilesToPropagate == null )
							{
								MultipartTilesToPropagate = new java . util . HashMap < Object , net . minecraft . tileentity . TileEntity > ( ) ;

								MultipartTileSetsToPropagate . put ( Chunk , MultipartTilesToPropagate ) ;
							}

							MultipartTilesToPropagate . put ( Record . Entity , Record . Entity ) ;
						}

						MultipartTilesToInitialize . add ( Record ) ;
					}
					catch ( Throwable Throwable )
					{
						Throwable . printStackTrace ( ) ;

						continue ;
					}
				}
				else
				{
					Record . Entity = net . minecraft . tileentity . TileEntity . createAndLoadEntity ( Record . EntityRecord ) ;
				}

				SneakyWorldUtil . SetTileEntity ( worldObj , Record . X , Record . Y , Record . Z , Record . Entity ) ;

				if ( Configuration . DirtyHacks . UpdateBuildcraftPipes )
				{
					if ( ModInteraction . BC_TileGenericPipe != null )
					{
						if ( ModInteraction . BC_TileGenericPipe . isInstance ( Record . Entity ) )
						{
							PipesToInitialize . add ( Record ) ;
						}
					}
				}
			}
		}

		for ( BlockRecord Record : MultipartTilesToInitialize )
		{
			try
			{
				ModInteraction . ForgeMultipart . TileMultipart_onChunkLoad . invoke ( Record . Entity ) ;
			}
			catch ( Throwable Throwable )
			{
				Throwable . printStackTrace ( ) ;
			}
		}

		if ( ModInteraction . ForgeMultipart . MultipartHelper_sendDescPacket != null )
		{
			for ( BlockRecord Record : MultipartTilesToInitialize )
			{
				try
				{
					ModInteraction . ForgeMultipart . MultipartHelper_sendDescPacket . invoke ( null , worldObj , Record . Entity ) ;
				}
				catch ( Throwable Throwable )
				{
					Throwable . printStackTrace ( ) ;
				}
			}
		}
		else
		{
			for ( java . util . Map . Entry < net . minecraft . world . chunk . Chunk , java . util . HashMap < Object , net . minecraft . tileentity . TileEntity > > MultipartTilesToPropagate
				: MultipartTileSetsToPropagate . entrySet ( ) )
			{
				net . minecraft . world . chunk . Chunk Chunk = MultipartTilesToPropagate . getKey ( ) ;

				java . util . Map SavedTileEntityMap = Chunk . chunkTileEntityMap ;

				Chunk . chunkTileEntityMap = MultipartTilesToPropagate . getValue ( ) ;

				try
				{
					for ( net . minecraft . entity . player . EntityPlayerMP Player : ( ( java . util . List < net . minecraft . entity . player . EntityPlayerMP > )
						( ( net . minecraft . world . WorldServer ) worldObj ) . getPlayerManager ( ) . getOrCreateChunkWatcher ( Chunk . xPosition , Chunk . zPosition , false ) . playersInChunk ) )
					{
						if ( ! Player . loadedChunks . contains ( Chunk . getChunkCoordIntPair ( ) ) )
						{
							try
							{
								if ( ModInteraction . ForgeMultipart . MultipartSaveLoad_loadingWorld_$eq == null )
								{
									MultipartPropagationPacket . Dispatch ( Player , MultipartTilesToPropagate . getValue ( ) . values ( ) ) ;
								}

								ModInteraction . ForgeMultipart . MultipartSPH_onChunkWatch . invoke ( null , Player , Chunk ) ;
							}
							catch ( Throwable Throwable )
							{
								Throwable . printStackTrace ( ) ;
							}
						}
					}
				}
				catch ( Throwable Throwable )
				{
					Throwable . printStackTrace ( ) ;
				}

				Chunk . chunkTileEntityMap = SavedTileEntityMap ;
			}
		}

		for ( BlockRecord Record : PipesToInitialize )
		{
			try
			{
				Object Pipe = ModInteraction . BC_TileGenericPipe_pipe . get ( Record . Entity ) ;

				ModInteraction . BC_TileGenericPipe_initialize . invoke ( Record . Entity , Pipe ) ;

				Object Transport = ModInteraction . BC_Pipe_transport . get ( Pipe ) ;

				if ( ! ModInteraction . BC_PipeTransportItems . isInstance ( Transport ) )
				{
					continue ;
				}

				ModInteraction . BC_PipeTransportItems_delay . set ( Transport , -1 ) ;

				java . util . List DelayedEntities = ( java . util . List ) ModInteraction . BC_PipeTransportItems_delayedEntitiesToLoad . get ( Transport ) ;

				java . util . Map EntityMap = ( java . util . Map ) ModInteraction . BC_PipeTransportItems_travelingEntities . get ( Transport ) ;

				for ( Object Entity : DelayedEntities )
				{
					Object Item = ModInteraction . BC_EntityData_item . get ( Entity ) ;

					ModInteraction . BC_EntityPassiveItem_setWorld . invoke ( Item , worldObj ) ;

					int Id = ( Integer ) ModInteraction . BC_EntityPassiveItem_getEntityId . invoke ( Item ) ;

					EntityMap . put ( Id , Entity ) ;

					Object ItemPosition = ModInteraction . BC_EntityPassiveItem_position . get ( Item ) ;

					ShiftPipeItemPosition ( ItemPosition ) ;
				}

				DelayedEntities . clear ( ) ;
			}
			catch ( Throwable Throwable )
			{
				Throwable . printStackTrace ( ) ;
			}
		}

		try
		{
			CarriageDriveEntity Drive = ( CarriageDriveEntity ) worldObj . getBlockTileEntity ( DriveRecord . X , DriveRecord . Y , DriveRecord . Z ) ;

			if ( ! DriveIsAnchored )
			{
				Drive . Active = true ;
			}

			Drive . ToggleActivity ( ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}

		SneakyWorldUtil . RefreshBlock ( worldObj , xCoord , yCoord , zCoord , Blocks . Spectre . blockID , 0 ) ;

		for ( BlockRecord Record : Body )
		{
			SneakyWorldUtil . RefreshBlock ( worldObj , Record . X , Record . Y , Record . Z , 0 , Record . Id ) ;
		}

		int PendingBlockUpdateCount = PendingBlockUpdates . tagCount ( ) ;

		for ( int Index = 0 ; Index < PendingBlockUpdateCount ; Index ++ )
		{
			ScheduleShiftedBlockUpdate ( ( net . minecraft . nbt . NBTTagCompound ) PendingBlockUpdates . tagAt ( Index ) ) ;
		}
	}
	
}
