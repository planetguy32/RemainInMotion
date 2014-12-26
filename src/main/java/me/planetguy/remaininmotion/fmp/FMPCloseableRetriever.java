package me.planetguy.remaininmotion.fmp;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;
import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

public class FMPCloseableRetriever implements ICloseableFactory {

	@Override
	public ICloseable retrieve(TileEntity te) {
		if (te instanceof TileMultipart) {
			TileMultipart tm = (TileMultipart) te;
			for (TMultiPart part : tm.jPartList()) {
				Debug.dbg(part);
				if (part instanceof ICloseable) { return (ICloseable) part; }
			}
		}
		return null;
	}

	@Override
	public Class validClass() {
		return TileMultipart.class;
	}

}
