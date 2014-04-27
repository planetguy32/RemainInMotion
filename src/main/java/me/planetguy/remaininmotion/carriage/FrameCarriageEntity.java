package me.planetguy.remaininmotion.carriage ;

import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.TEAccessUtil;
import me.planetguy.remaininmotion.util.CarriageMotionException;

public class FrameCarriageEntity extends CarriageEntity
{
	@Override
	public void FillPackage ( CarriagePackage Package ) throws CarriageMotionException
	{
		TEAccessUtil.fillFramePackage(Package, worldObj);
	}
}
