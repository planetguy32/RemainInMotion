package me.planetguy.remaininmotion.fmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import net.minecraft.tileentity.TileEntity;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;

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
