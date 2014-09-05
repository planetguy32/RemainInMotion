package me.planetguy.remaininmotion.drive;

import me.planetguy.remaininmotion.BlacklistManager;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriageObstructionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.api.ISpecialMoveBehavior;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.Core;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import me.planetguy.util.Debug;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;

public class CarriageAdapterEntity extends CarriageEngineEntity implements ISpecialMoveBehavior{
	
	public boolean alreadyMoving;
	
	static{
		BlacklistManager.blacklistSoft.blacklist(RIMBlocks.CarriageDrive,5);
	}
	
	public void HandleToolUsage ( int Side , boolean Sneaking ){
		super.HandleToolUsage(Side, true); //Don't allow toggling continuous - it's meaningless
	}
	
	@Override
	public void updateEntity ( )
	{
		if ( worldObj . isRemote )
		{
			return ;
		}

		if ( Stale )
		{
			HandleNeighbourBlockChange ( ) ;
		}

		if ( CooldownRemaining > 0 )
		{
			CooldownRemaining -- ;

			MarkServerRecordDirty ( ) ;

			return ;
		}

		if ( Active )
		{
			return ;
		}

		if ( SignalDirection == null )
		{
			if ( Signalled )
			{
				Signalled = false ;

				MarkServerRecordDirty ( ) ;
			}

			return ;
		}

		if ( CarriageDirection == null )
		{
			return ;
		}

		if ( Signalled )
		{
			if ( ! Continuous )
			{
				return ;
			}
		}
		else
		{
			Signalled = true ;

			MarkServerRecordDirty ( ) ;
		}
	}
	
	
	@Override
	public void onAdded(CarriagePackage pkg, NBTTagCompound tag) throws CarriageMotionException{
		this.HandleNeighbourBlockChange();
		if(!alreadyMoving){
			alreadyMoving=true;
			if(CarriageDirection!=null){
				BlockRecord oldAnchor=pkg.AnchorRecord;
				pkg.AnchorRecord=new BlockRecord(xCoord+CarriageDirection.DeltaX,yCoord+CarriageDirection.DeltaY,zCoord+CarriageDirection.DeltaZ);
				pkg.AnchorRecord.Identify(worldObj);
				MultiTypeCarriageUtil.fillPackage(pkg, worldObj.getTileEntity(
					xCoord+CarriageDirection.DeltaX,
					yCoord+CarriageDirection.DeltaY,
					zCoord+CarriageDirection.DeltaZ) ) ;
				pkg.AnchorRecord=oldAnchor;
				
			}
		}
		this.writeToNBT(tag);
	}
	
	public void fillPackage (CarriagePackage Package, TileEntity carriage) throws CarriageMotionException
	{
		MultiTypeCarriageUtil.fillPackage(Package, carriage ) ;

	}
	
	public String toString(){
		return Debug.dump(this);
	}

}
