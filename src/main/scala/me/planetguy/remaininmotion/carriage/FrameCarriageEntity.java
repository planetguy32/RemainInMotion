package me.planetguy.remaininmotion.carriage ;

import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;

public class FrameCarriageEntity extends CarriageEntity
{
	@Override
	public void fillPackage ( CarriagePackage Package ) throws CarriageMotionException
	{
		MultiTypeCarriageUtil.fillFramePackage(Package, worldObj);
	}
}
