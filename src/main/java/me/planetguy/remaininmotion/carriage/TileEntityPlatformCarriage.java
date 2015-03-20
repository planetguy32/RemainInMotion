package me.planetguy.remaininmotion.carriage;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.motion.BlacklistManager;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.position.BlockRecordSet;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityPlatformCarriage extends TileEntityCarriage {

	public void FailBecauseOverburdened() throws CarriageMotionException {
		throw (new CarriageMotionException(Lang.translate(ModRiM.Handle + ".overburdened").replace("##BURDEN##",
				RiMConfiguration.Carriage.MaxPlatformBurden + "")));
	}

	@Override
	public void fillPackage(CarriagePackage pkg) throws CarriageMotionException {
		BlockRecordSet checked = new BlockRecordSet();
		checked.add(pkg.driveRecord);
		BlockRecordSet todo = new BlockRecordSet();
		todo.add(pkg.AnchorRecord);

		int positionsCarried = 0;

		while (!todo.isEmpty()) {
			BlockRecord inProgress = todo.pollFirst();
			if (canAdd(inProgress, positionsCarried)) {
				positionsCarried++;
				// plan neighbours
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
					// special case for side closing
					if (inProgress != pkg.AnchorRecord || !SideClosed[dir.ordinal()]) {
						BlockRecord neighbour = new BlockRecord(inProgress).shift(dir);
						if (!checked.contains(neighbour)) {
							todo.add(neighbour);
						}
					}
				}
				// add to package
				inProgress.Identify(worldObj);
				pkg.AddBlock(inProgress);
			} else {
				pkg.AddPotentialObstruction(inProgress);
			}
			checked.add(inProgress);
		}

	}

	private boolean canAdd(BlockRecord record, int count) {
		return !worldObj.isAirBlock(record.X, record.Y, record.Z)
				&& count < RiMConfiguration.Carriage.MaxPlatformBurden
				&& !BlacklistManager.blacklistSoft.lookup(worldObj, record.X, record.Y, record.Z);
	}

	/*
	 * @Override public void fillPackage ( CarriagePackage Package ) throws
	 * CarriageMotionException { BlockRecordSet CarriagesToCheck = new
	 * BlockRecordSet ( ) ;
	 * 
	 * BlockRecordSet BlocksToCheck = new BlockRecordSet ( ) ;
	 * 
	 * BlockRecordSet BlocksChecked = new BlockRecordSet ( ) ;
	 * 
	 * BlocksChecked . add ( Package . driveRecord ) ;
	 * 
	 * BlocksChecked . add ( Package . AnchorRecord ) ;
	 * 
	 * Package . AddBlock ( Package . AnchorRecord ) ;
	 * 
	 * CarriagesToCheck . add ( Package . AnchorRecord ) ;
	 * 
	 * int BlocksCarried = 0 ;
	 * 
	 * while ( CarriagesToCheck . size ( ) > 0 ) { BlockRecord CarriageRecord =
	 * CarriagesToCheck . pollFirst ( ) ;
	 * 
	 * for ( Directions TargetDirection : Directions . values ( ) ) {
	 * BlockRecord TargetRecord = CarriageRecord . NextInDirection (
	 * TargetDirection ) ;
	 * 
	 * if ( ( ( PlatformCarriageEntity ) CarriageRecord . Entity ) . SideClosed
	 * [ TargetDirection . ordinal ( ) ] ) { if ( TargetDirection == Package .
	 * MotionDirection ) { Package . AddPotentialObstruction ( TargetRecord ) ;
	 * }
	 * 
	 * continue ; }
	 * 
	 * if ( BlocksChecked . add ( TargetRecord ) ) { continue ; }
	 * 
	 * if ( BlacklistManager.blacklistSoft.lookup(worldObj, TargetRecord.X,
	 * TargetRecord.Y, TargetRecord.Z) ||worldObj.isAirBlock(TargetRecord.X,
	 * TargetRecord.Y, TargetRecord.Z)) {
	 * Package.AddPotentialObstruction(TargetRecord); continue ; }
	 * 
	 * TargetRecord . Identify ( worldObj ) ;
	 * 
	 * Package . AddBlock ( TargetRecord ) ;
	 * 
	 * if ( Package . MatchesCarriageType ( TargetRecord ) ) { CarriagesToCheck
	 * . add ( TargetRecord ) ;
	 * 
	 * continue ; }
	 * 
	 * BlocksToCheck . add ( TargetRecord ) ;
	 * 
	 * BlocksCarried ++ ;
	 * 
	 * if ( BlocksCarried > Configuration . Carriage . MaxPlatformBurden ) {
	 * FailBecauseOverburdened ( ) ; } } }
	 * 
	 * while ( BlocksToCheck . size ( ) > 0 ) { BlockRecord BlockRecord =
	 * BlocksToCheck . pollFirst ( ) ;
	 * 
	 * for ( Directions TargetDirection : Directions . values ( ) ) {
	 * BlockRecord TargetRecord = BlockRecord . NextInDirection (
	 * TargetDirection ) ;
	 * 
	 * if ( ! BlocksChecked . add ( TargetRecord ) ) { continue ; }
	 * 
	 * if ( worldObj . isAirBlock ( TargetRecord . X , TargetRecord . Y ,
	 * TargetRecord . Z ) ) { continue ; }
	 * 
	 * TargetRecord . Identify ( worldObj ) ;
	 * 
	 * Package . AddBlock ( TargetRecord ) ;
	 * 
	 * BlocksToCheck . add ( TargetRecord ) ;
	 * 
	 * BlocksCarried ++ ;
	 * 
	 * if ( BlocksCarried > 10)// Configuration . Carriage . MaxPlatformBurden )
	 * { FailBecauseOverburdened ( ) ; } } } }
	 */
}
