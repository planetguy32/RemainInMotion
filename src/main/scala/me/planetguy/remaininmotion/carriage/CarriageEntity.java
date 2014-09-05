package me.planetguy.remaininmotion.carriage ;

import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.base.RIMBlock;
import me.planetguy.remaininmotion.base.TileEntity;

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

	public int DecorationId ;

	public int DecorationMeta ;

	public int Tier ;

	@Override
	public void Setup ( net . minecraft . entity . player . EntityPlayer Player , net . minecraft . item . ItemStack Item )
	{
		DecorationId = CarriageItem . GetDecorationId ( Item ) ;

		DecorationMeta = CarriageItem . GetDecorationMeta ( Item ) ;

		Tier = CarriageItem . GetTier ( Item ) ;
	}

	@Override
	public void EmitDrops ( RIMBlock Block , int Meta )
	{
		EmitDrop ( Block , CarriageItem . Stack ( Meta , Tier , DecorationId , DecorationMeta ) ) ;
	}

	@Override
	public void ReadCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		for ( int Index = 0 ; Index < SideClosed . length ; Index ++ )
		{
			SideClosed [ Index ] = TagCompound . getBoolean ( "SideClosed" + Index ) ;
		}

		DecorationId = TagCompound . getInteger ( "DecorationId" ) ;

		DecorationMeta = TagCompound . getInteger ( "DecorationMeta" ) ;

		Tier = TagCompound . getInteger ( "Tier" ) ;
	}

	public void WriteCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		for ( int Index = 0 ; Index < SideClosed . length ; Index ++ )
		{
			TagCompound . setBoolean ( "SideClosed" + Index , SideClosed [ Index ] ) ;
		}

		TagCompound . setInteger ( "DecorationId" , DecorationId ) ;

		TagCompound . setInteger ( "DecorationMeta" , DecorationMeta ) ;

		TagCompound . setInteger ( "Tier" , Tier ) ;
	}

	public abstract void fillPackage ( CarriagePackage Package ) throws CarriageMotionException ;
}
