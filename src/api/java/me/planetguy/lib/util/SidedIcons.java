package me.planetguy.lib.util;

import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class SidedIcons {
	
	public SidedIcons(IIcon front, IIcon sideUp, IIcon sideDown, IIcon sideFacingLeft, IIcon back ) {
		IIcon sideFacingRight=new IconFlipped(sideFacingLeft, true, false);
		
		Debug.mark();
		
		//TODO fix this icon matrix
		iconMatrix=new IIcon[][] {
				{back, front, sideUp, sideUp, sideUp, sideUp},
				{front, back, sideDown, sideDown, sideDown, sideDown},
				{sideDown, sideDown, back, front, sideFacingRight, sideFacingLeft},
				{sideUp, sideUp, front, back, sideFacingLeft, sideFacingRight},
				{sideFacingRight, sideFacingRight, sideFacingLeft, sideFacingRight, back, front},
				{sideFacingLeft, sideFacingLeft, sideFacingRight, sideFacingLeft, front, back}
		};
	}
	
	private IIcon[][] iconMatrix;
	
	public IIcon getIcon(ForgeDirection facing, int side) {
		return iconMatrix[facing.ordinal()][side];
	}

}
