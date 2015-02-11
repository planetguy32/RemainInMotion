package me.planetguy.remaininmotion;

import java.util.ArrayList;
import java.util.List;

import me.planetguy.remaininmotion.api.CarriageMatcher;
import me.planetguy.remaininmotion.api.FrameCarriageMatcher;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class FrameCarriageMatchers {

	private static boolean						hasRegisteredMatcher	= false;

	private static List<FrameCarriageMatcher>	matchers				= new ArrayList<FrameCarriageMatcher>();

	public static void register(FrameCarriageMatcher matcher) {
		matchers.add(matcher);
		if (!hasRegisteredMatcher) {
			hasRegisteredMatcher = true;
			RiMRegistry.registerMatcher(new CarriageMatcher() {

				@Override
				public boolean matches(Block block1, int meta1, TileEntity entity1, Block bloc2k, int meta2,
						TileEntity entity2) {
					return FrameCarriageMatchers.matches(block1, meta1, entity1)
							&& FrameCarriageMatchers.matches(bloc2k, meta2, entity2);
				}

				@Override
				public Moveable getCarriage(Block block, int meta, final TileEntity te) {
					if (FrameCarriageMatchers.matches(block, meta, te)) {
						return new Moveable() {
							@Override
							public void fillPackage(CarriagePackage _package) throws CarriageMotionException {
								MultiTypeCarriageUtil.fillFramePackage(_package, te.getWorldObj());
							}
						};
					} else {
						return null;
					}
				}

			});
		}
	}

	public static boolean matches(Block b, int meta, TileEntity te) {
		for (FrameCarriageMatcher m : matchers) {
			if (m.isFrameCarriage(b, meta, te)) { return true; }
		}
		return false;
	}

}
