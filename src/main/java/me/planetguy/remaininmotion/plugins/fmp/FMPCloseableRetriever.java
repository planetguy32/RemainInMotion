package me.planetguy.remaininmotion.plugins.fmp;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

public class FMPCloseableRetriever implements ICloseableFactory {

	@Override
	public ICloseable retrieve(TileEntity te) {
		if (te instanceof TileMultipart) {
			TileMultipart tm = (TileMultipart) te;
			for (TMultiPart part : tm.jPartList()) {
				if (RiMConfiguration.Debug.verbose) {
					Debug.dbg(part);
				}
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
