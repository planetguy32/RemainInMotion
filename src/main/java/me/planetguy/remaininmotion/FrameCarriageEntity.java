package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.util.CarriageMotionException;

public class FrameCarriageEntity extends CarriageEntity
{
	@Override
	public void FillPackage ( CarriagePackage Package ) throws CarriageMotionException
	{
		TEAccessUtil.fillFramePackage(Package, worldObj);
	}
}
