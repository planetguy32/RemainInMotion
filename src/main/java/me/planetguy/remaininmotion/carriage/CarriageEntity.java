package me.planetguy.remaininmotion.carriage ;

import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.base.Block;
import me.planetguy.remaininmotion.base.TileEntity;
import me.planetguy.remaininmotion.util.CarriageMotionException;

public abstract class CarriageEntity extends TileEntity implements Moveable
{
	@Override
	public boolean canUpdate ( )
	{
		return ( false ) ;
	}

	public boolean [ ] SideClosed = new boolean [ Directions . values ( ) . length ] ;

	public void ToggleSide ( int Side , boolean Sneaking )
	{
		if ( Sneaking )
		{
			Side = Directions . values ( ) [ Side ] . Opposite ( ) . ordinal ( ) ;
		}

		SideClosed [ Side ] = ! SideClosed [ Side ] ;

		Propagate ( ) ;
	}

	public net.minecraft.block.Block decorationId ;

	public int DecorationMeta ;

	public int Tier ;

	@Override
	public void Setup ( net . minecraft . entity . player . EntityPlayer Player , net . minecraft . item . ItemStack Item )
	{
		decorationId = CarriageItem . GetDecorationId ( Item ) ;

		DecorationMeta = CarriageItem . GetDecorationMeta ( Item ) ;

		Tier = CarriageItem . GetTier ( Item ) ;
	}

	@Override
	public void EmitDrops ( Block Block , int Meta )
	{
		EmitDrop ( Block , CarriageItem . Stack ( Meta , Tier , decorationId , DecorationMeta ) ) ;
	}

	@Override
	public void ReadCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		for ( int Index = 0 ; Index < SideClosed . length ; Index ++ )
		{
			SideClosed [ Index ] = TagCompound . getBoolean ( "SideClosed" + Index ) ;
		}

		decorationId = (net.minecraft.block.Block) Block.blockRegistry.getObjectById(TagCompound . getInteger ( "DecorationId" )) ;

		DecorationMeta = TagCompound . getInteger ( "DecorationMeta" ) ;

		Tier = TagCompound . getInteger ( "Tier" ) ;
	}

	public void WriteCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		for ( int Index = 0 ; Index < SideClosed . length ; Index ++ )
		{
			TagCompound . setBoolean ( "SideClosed" + Index , SideClosed [ Index ] ) ;
		}

		TagCompound . setInteger ( "DecorationId" , Block.blockRegistry.getIDForObject(decorationId) ) ;

		TagCompound . setInteger ( "DecorationMeta" , DecorationMeta ) ;

		TagCompound . setInteger ( "Tier" , Tier ) ;
	}

	public void fillPackage(CarriagePackage pkg) throws CarriageMotionException{
		FillPackage(pkg);
	}
	
	public abstract void FillPackage ( CarriagePackage Package ) throws CarriageMotionException ;
}
