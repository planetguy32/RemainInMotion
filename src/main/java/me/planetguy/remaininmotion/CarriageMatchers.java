package me.planetguy.remaininmotion;

import java.util.ArrayList;
import java.util.List;

import me.planetguy.remaininmotion.api.CarriageMatcher;
import me.planetguy.remaininmotion.api.Moveable;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class CarriageMatchers {

	public static List<CarriageMatcher>	matchers	= new ArrayList<CarriageMatcher>();

	public static void register(CarriageMatcher matcher) {
		matchers.add(matcher);
	}

	public static boolean matches(Block b, int meta, TileEntity te, CarriagePackage pkg) {
		for (CarriageMatcher m : matchers) {
			if (m.matches(b, meta, te, pkg.AnchorRecord.block, pkg.AnchorRecord.Meta, pkg.AnchorRecord.entity)) { return true; }
		}
		return false;
	}

	public static Moveable getMover(Block blockslist, int meta, TileEntity te) {
		for (CarriageMatcher m : matchers) {
			Moveable mv = m.getCarriage(blockslist, meta, te);
			if (mv != null) { return mv; }
		}
		return null;
	}

}
