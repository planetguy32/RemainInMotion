package me.planetguy.remaininmotion.drive;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.spectre.MotiveSpectreEntity;
import me.planetguy.remaininmotion.spectre.RotativeSpectreEntity;
import me.planetguy.remaininmotion.spectre.Spectre;
import me.planetguy.remaininmotion.spectre.TeleportativeSpectreEntity;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.WorldUtil;
import net.minecraft.tileentity.TileEntity;

public class CarriageRotatorEntity extends CarriageDriveEntity{
	
	@Override
	public CarriagePackage GeneratePackage ( TileEntity carriage , Directions CarriageDirection , Directions MotionDirection ) throws CarriageMotionException
	{		
		CarriagePackage Package = new CarriagePackage ( this , carriage , MotionDirection.Null ) ;

		Package . AddBlock ( Package . DriveRecord ) ;
		
		MultiTypeCarriageUtil.fillPackage(Package, carriage ) ;
		
		Package . Finalize ( ) ;
		
		this.removeUsedEnergy(Package);

		return ( Package ) ;
	}
	@Override
	public boolean Anchored() {
		return false;
	}
	
	//don't establish placeholders yet - it's very hard to predict where things will go
	@Override
	public void EstablishPlaceholders(CarriagePackage pkg) {
		for ( BlockRecord Record : pkg . Body )
		{
			{
				SneakyWorldUtil . SetBlock ( worldObj , Record . X , Record . Y , Record . Z , RIMBlocks.air , 0 ) ;
			}
		}
	}
	
	@Override
	public void EstablishSpectre ( CarriagePackage Package )
	{
		int CarriageX = Package . AnchorRecord . X ;
		int CarriageY = Package . AnchorRecord . Y ;
		int CarriageZ = Package . AnchorRecord . Z ;

		WorldUtil . SetBlock ( worldObj , CarriageX , CarriageY , CarriageZ ,
				RIMBlocks . Spectre , Spectre . Types . Rotative . ordinal ( ) ) ;
		
		RotativeSpectreEntity theEntity=new RotativeSpectreEntity();
		
		worldObj.setTileEntity(CarriageX, CarriageY, CarriageZ, theEntity);
		
		theEntity . Absorb ( Package ) ;
	}

}
