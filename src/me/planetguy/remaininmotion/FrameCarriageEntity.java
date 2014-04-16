package me.planetguy.remaininmotion ;

public class FrameCarriageEntity extends CarriageEntity
{
	@Override
	public void FillPackage ( CarriagePackage Package ) throws CarriageMotionException
	{
		TEAccessUtil.fillFramePackage(Package, worldObj);
	}
}
