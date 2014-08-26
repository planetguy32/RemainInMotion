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
	
	static{
		BlacklistManager.blacklistSoft.blacklist(RIMBlocks.CarriageDrive,5);
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
	
	
	public void startMoving(Directions dir){
		try
		{
			InitiateMotion ( PreparePackage ( dir ) ) ;
		}
		catch ( CarriageMotionException Exception )
		{
			String Message = "Drive at (" + xCoord + "," + yCoord + "," + zCoord + ") in dimension " + worldObj . provider . dimensionId + " failed to move carriage: " + Exception . getMessage ( ) ;

			if ( Exception instanceof CarriageObstructionException )
			{
				CarriageObstructionException ObstructionException = ( CarriageObstructionException ) Exception ;

				Message += " - (" + ObstructionException . X + "," + ObstructionException . Y + "," + ObstructionException . Z + ")" ;
			}

			if ( Configuration . Debug . LogMotionExceptions ){
				Debug . dbg ( Message ) ;
			}

			if(this.lastUsingPlayer!=null){
				this.lastUsingPlayer.addChatComponentMessage(new ChatComponentText(Message));
			}
		}
	}

	@Override
	public void onAdded(CarriagePackage pkg, NBTTagCompound tag) {
		if(
				!pkg.Carriages.contains(new BlockRecord(this))
		   &&   !pkg.Body.contains(new BlockRecord(this))){
			startMoving(this.CarriageDirection);
		}
		this.writeToNBT(tag);
	}
	
	public CarriagePackage GeneratePackage ( CarriagePackage Package, TileEntity carriage , Directions CarriageDirection , Directions MotionDirection ) throws CarriageMotionException
	{
		Package . AddBlock ( Package . DriveRecord ) ;

		if ( MotionDirection != CarriageDirection )
		{
			Package . AddPotentialObstruction ( Package . DriveRecord . NextInDirection ( MotionDirection ) ) ;
		}

		MultiTypeCarriageUtil.fillPackage(Package, carriage ) ;
		
		Package . Finalize ( ) ;

		return ( Package ) ;
	}

}
