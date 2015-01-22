package me.planetguy.remaininmotion;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import me.planetguy.remaininmotion.api.CarriageMatcher;
import me.planetguy.remaininmotion.api.FrameCarriageMatcher;
import me.planetguy.remaininmotion.api.Moveable;

public class FrameCarriageMatchers {

	private static boolean hasRegisteredMatcher=false;
	
	public static List<FrameCarriageMatcher>	matchers	= new ArrayList<FrameCarriageMatcher>();

	public static void register(FrameCarriageMatcher matcher) {
		matchers.add(matcher);
	}

	public static boolean matches(Block b, int meta, TileEntity te, CarriagePackage pkg) {
		for (FrameCarriageMatcher m : matchers) {
			if (m.isFrameCarriage(b, meta, te, pkg.AnchorRecord.block, pkg.AnchorRecord.Meta, pkg.AnchorRecord.Entity)) { return true; }
		}
		return false;
	}

	public static Moveable getMover(Block blockslist, int meta, TileEntity te) {
		for (FrameCarriageMatcher m : matchers) {
			
		}
		return null;
	}

}
