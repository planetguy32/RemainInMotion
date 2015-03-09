package me.planetguy.remaininmotion.core.interop.chickenchunks;

import me.planetguy.remaininmotion.BlockRecord;
import net.minecraft.world.World;

public interface IChickenChunksHandler {

	public abstract void performChickenChunksPostInit(World worldObj,
			BlockRecord record);

	public abstract void handleChickenChunks(World worldObj,
			BlockRecord record, BlockRecord newPosition);

}