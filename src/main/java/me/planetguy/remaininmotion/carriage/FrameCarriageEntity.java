package me.planetguy.remaininmotion.carriage ;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import scala.actors.threadpool.Arrays;
import me.planetguy.remaininmotion.CarriageMotionException;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;

public class FrameCarriageEntity extends CarriageEntity
{
	public FrameCarriageEntity() {
		RiMRegistry.registerCloseableFactory(new ICloseableFactory() {
			@Override
			public ICloseable retrieve(TileEntity te) {
				return (ICloseable) te;
			}

			@Override
			public List<Class<? extends TileEntity>> validClasses() {
				return Arrays.asList(new Class[] {FrameCarriageEntity.class});
			}
		});
	}
	
	@Override
	public void fillPackage ( CarriagePackage Package ) throws CarriageMotionException
	{
		MultiTypeCarriageUtil.fillFramePackage(Package, worldObj);
	}

}
