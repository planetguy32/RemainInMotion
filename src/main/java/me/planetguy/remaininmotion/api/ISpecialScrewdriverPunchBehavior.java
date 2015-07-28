package me.planetguy.remaininmotion.api;

import net.minecraft.world.World;

public interface ISpecialScrewdriverPunchBehavior {
	
	public boolean onPunched(World w, int x, int y, int z);

}
