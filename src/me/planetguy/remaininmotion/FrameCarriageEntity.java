package me.planetguy.remaininmotion ;

public class FrameCarriageEntity extends CarriageEntity
{
	@Override
	public void fillPackage ( CarriagePackage Package ) throws CarriageMotionException
	{
		TEAccessUtil.fillFramePackage(Package, worldObj);
	}
}
