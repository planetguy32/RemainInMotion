package me.planetguy.remaininmotion.api.event;

import net.minecraft.world.World;

public interface IBlockPos {
	
	public World world();
	public int x();
	public int y(); 
	public int z();

}
