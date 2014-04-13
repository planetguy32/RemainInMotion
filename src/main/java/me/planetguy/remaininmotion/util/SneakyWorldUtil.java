package me.planetguy.remaininmotion.util ;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public abstract class SneakyWorldUtil
{
	public static void SetBlock ( net . minecraft . world . World World , int X , int Y , int Z , Block spectre , int Meta )
	{
		net . minecraft . world . chunk . Chunk Chunk = World . getChunkFromBlockCoords ( X , Z ) ;

		int ChunkX = X & 0xF ;
		int ChunkY = Y & 0xF ;
		int ChunkZ = Z & 0xF ;

		Chunk . removeTileEntity ( ChunkX , Y , ChunkZ ) ;

		int LayerY = Y >> 4 ;
		ExtendedBlockStorage[] storageArrays=(ExtendedBlockStorage[]) Reflection.stealField(Chunk, "storageArrays");

		if ( storageArrays [ LayerY ] == null )
		{
			storageArrays [ LayerY ] = new net . minecraft . world . chunk . storage . ExtendedBlockStorage ( ( LayerY ) << 4 , ! World . provider . hasNoSky ) ;
		}

		storageArrays [ LayerY ] . func_150818_a ( ChunkX , ChunkY , ChunkZ , spectre ) ;

		storageArrays [ LayerY ] . setExtBlockMetadata ( ChunkX , ChunkY , ChunkZ , Meta ) ;

		Chunk . isModified = true ;

		World . markBlockForUpdate ( X , Y , Z ) ;
	}

	public static void SetTileEntity ( net . minecraft . world . World World , int X , int Y , int Z , net . minecraft . tileentity . TileEntity Entity )
	{
		if ( (Boolean)Reflection.stealField(World,"field_147481_N"))
		{
			((List)Reflection.stealField(World ,"addedTileEntityList")) . add ( Entity ) ;
		}
		else
		{
			World . loadedTileEntityList . add ( Entity ) ;
		}

		World . getChunkFromBlockCoords ( X , Z ) . func_150812_a ( X & 0xF , Y , Z & 0xF , Entity ) ;
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
			if ( (Integer)Reflection.runMethod(Chunk, "getBlockLightOpacity",ChunkX , Y , ChunkZ ) > 0 )
			{
				if ( Y >= HeightMapValue )
				{
					Reflection.runMethod(Chunk,"relightBlock", ChunkX , Y + 1 , ChunkZ ) ;
				}
			}
			else if ( Y == HeightMapValue - 1 )
			{
				Reflection.runMethod(Chunk,"relightBlock", ChunkX , Y , ChunkZ ) ;
			}

			Reflection.runMethod(Chunk,"propagateSkylightOcclusion",  ChunkX , ChunkZ ) ;
		}

		World . func_147451_t ( X , Y , Z ) ;
	}

	public static void NotifyBlocks ( net . minecraft . world . World World , int X , int Y , int Z , Block OldId , Block NewId )
	{
		World . notifyBlockChange ( X , Y , Z , OldId ) ;

		if ( NewId == null )
		{
			return ;
		}

		if ( ( World . getTileEntity ( X , Y , Z ) != null ) || ( NewId. hasComparatorInputOverride ( ) ) )
		{
			World . func_147453_f ( X , Y , Z , NewId ) ;
		}
	}

	public static void RefreshBlock ( net . minecraft . world . World World , int X , int Y , int Z , net.minecraft.block.Block OldId , Block NewId )
	{
		UpdateLighting ( World , X , Y , Z ) ;

		NotifyBlocks ( World , X , Y , Z , OldId , NewId ) ;
	}
}
