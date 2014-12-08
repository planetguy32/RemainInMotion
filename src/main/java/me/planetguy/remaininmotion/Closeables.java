package me.planetguy.remaininmotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;
import net.minecraft.tileentity.TileEntity;

public class Closeables {
	
	static HashMap<Class<? extends TileEntity>, ICloseableFactory> closeableMap=new HashMap<Class<? extends TileEntity>, ICloseableFactory>();
	
	public static void register(ICloseableFactory closeable) {
		for(Class c:closeable.validClasses())
			closeableMap.put(c, closeable);
	}
	
	public static ICloseable getCloseable(TileEntity entity) {
		if(entity != null) {
			ICloseableFactory factory=closeableMap.get(entity.getClass());
			if(factory != null)
				return factory.retrieve(entity);
		}
		return null;
	}
	
}
