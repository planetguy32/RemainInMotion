package me.planetguy.remaininmotion.base;

import net.minecraft.block.Block;

public class BlockCamouflageable extends BlockRiM{

	public BlockCamouflageable(Block Template,
			Class<? extends ItemBlockRiM> BlockItemClass,
			Class<? extends TileEntityRiM>[] TileEntityList) {
		super(Template, BlockItemClass, TileEntityList);
		
	}

}
