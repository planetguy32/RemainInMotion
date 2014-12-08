package me.planetguy.remaininmotion.fmp;

import java.util.List;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import scala.actors.threadpool.Arrays;
import scala.collection.Seq;
import net.minecraft.tileentity.TileEntity;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;

public class FMPCloseableRetriever implements ICloseableFactory {

	@Override
	public ICloseable retrieve(TileEntity te) {
		if(te instanceof TileMultipart){
			TileMultipart tm=(TileMultipart) te;
			for(TMultiPart part:tm.jPartList()){
				if(part instanceof FMPCarriage){
					return (ICloseable) part;
				}
			}
		}
		return null;
	}

	@Override
	public List<Class<? extends TileEntity>> validClasses() {
		return Arrays.asList(new Class[] {TileMultipart.class});
	}

}
