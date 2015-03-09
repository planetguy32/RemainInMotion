package me.planetguy.remaininmotion.core.interop.chickenchunks;

import me.planetguy.remaininmotion.BlockRecord;
import net.minecraft.world.World;

public class DummyCCHandler implements IChickenChunksHandler {

	@Override
	public void performChickenChunksPostInit(World worldObj, BlockRecord record) {
	}

	@Override
	public void handleChickenChunks(World worldObj, BlockRecord record,
			BlockRecord newPosition) {
	}

}
