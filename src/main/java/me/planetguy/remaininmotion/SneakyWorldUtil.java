package me.planetguy.remaininmotion ;

public abstract class SneakyWorldUtil
{
	public static void SetBlock ( net . minecraft . world . World World , int X , int Y , int Z , int Id , int Meta )
	{
		net . minecraft . world . chunk . Chunk Chunk = World . getChunkFromBlockCoords ( X , Z ) ;

		int ChunkX = X & 0xF ;
		int ChunkY = Y & 0xF ;
		int ChunkZ = Z & 0xF ;

		Chunk . removeChunkBlockTileEntity ( ChunkX , Y , ChunkZ ) ;

		int LayerY = Y >> 4 ;

		if ( Chunk . storageArrays [ LayerY ] == null )
		{
			Chunk . storageArrays [ LayerY ] = new net . minecraft . world . chunk . storage . ExtendedBlockStorage ( ( LayerY ) << 4 , ! World . provider . hasNoSky ) ;
		}

		Chunk . storageArrays [ LayerY ] . setExtBlockID ( ChunkX , ChunkY , ChunkZ , Id ) ;

		Chunk . storageArrays [ LayerY ] . setExtBlockMetadata ( ChunkX , ChunkY , ChunkZ , Meta ) ;

		Chunk . isModified = true ;

		World . markBlockForUpdate ( X , Y , Z ) ;
	}

	public static void SetTileEntity ( net . minecraft . world . World World , int X , int Y , int Z , net . minecraft . tileentity . TileEntity Entity )
	{
		if ( World . scanningTileEntities )
		{
			World . addedTileEntityList . add ( Entity ) ;
		}
		else
		{
			World . loadedTileEntityList . add ( Entity ) ;
		}

		World . getChunkFromBlockCoords ( X , Z ) . setChunkBlockTileEntity ( X & 0xF , Y , Z & 0xF , Entity ) ;
	}

	/* out of context, this is woefully redundant and inefficient, and really needs to be fixed */
	public static void UpdateLighting ( net . minecraft . world . World World , int X , int Y , int Z )
	{
		net . minecraft . world . chunk . Chunk Chunk = World . getChunkFromBlockCoords ( X , Z ) ;

		int ChunkX = X & 0xF ;
		int ChunkY = Y & 0xF ;
		int ChunkZ = Z & 0xF ;

		int HeightMapIndex = ChunkZ << 4 | ChunkX ;

		if ( Y >= Chunk . precipitationHeightMap [ HeightMapIndex ] - 1 )
		{
			Chunk . precipitationHeightMap [ HeightMapIndex ] = -999 ;
		}

		int HeightMapValue = Chunk . heightMap [ HeightMapIndex ] ;

		if ( Y >= HeightMapValue )
		{
			Chunk . generateSkylightMap ( ) ;
		}
		else
		{
			if ( Chunk . getBlockLightOpacity ( ChunkX , Y , ChunkZ ) > 0 )
			{
				if ( Y >= HeightMapValue )
				{
					Chunk . relightBlock ( ChunkX , Y + 1 , ChunkZ ) ;
				}
			}
			else if ( Y == HeightMapValue - 1 )
			{
				Chunk . relightBlock ( ChunkX , Y , ChunkZ ) ;
			}

			Chunk . propagateSkylightOcclusion ( ChunkX , ChunkZ ) ;
		}

		World . updateAllLightTypes ( X , Y , Z ) ;
	}

	public static void NotifyBlocks ( net . minecraft . world . World World , int X , int Y , int Z , int OldId , int NewId )
	{
		World . notifyBlockChange ( X , Y , Z , OldId ) ;

		if ( NewId == 0 )
		{
			return ;
		}

		if ( ( World . getBlockTileEntity ( X , Y , Z ) != null ) || ( Block . Get ( NewId ) . hasComparatorInputOverride ( ) ) )
		{
			World . func_96440_m ( X , Y , Z , NewId ) ;
		}
	}

	public static void RefreshBlock ( net . minecraft . world . World World , int X , int Y , int Z , int OldId , int NewId )
	{
		UpdateLighting ( World , X , Y , Z ) ;

		NotifyBlocks ( World , X , Y , Z , OldId , NewId ) ;
	}
}
