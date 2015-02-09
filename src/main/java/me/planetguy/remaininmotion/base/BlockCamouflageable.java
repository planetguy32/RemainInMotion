package me.planetguy.remaininmotion.base;

import me.planetguy.remaininmotion.carriage.ItemCarriage;
import me.planetguy.remaininmotion.carriage.TileEntityFrameCarriage;
import me.planetguy.remaininmotion.carriage.TileEntityMemoryCarriage;
import me.planetguy.remaininmotion.carriage.TileEntityPlatformCarriage;
import me.planetguy.remaininmotion.carriage.TileEntityStructureCarriage;
import me.planetguy.remaininmotion.carriage.TileEntitySupportCarriage;
import me.planetguy.remaininmotion.carriage.TileEntityTemplateCarriage;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCamouflageable extends BlockRiM{

	public BlockCamouflageable(Block Template,
			Class<? extends ItemBlockRiM> BlockItemClass,
			Class<? extends TileEntityRiM>... TileEntityList) {
		super(Template, BlockItemClass, TileEntityList);
		
	}
	
	public IIcon getIconCamouflaged(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te=world.getTileEntity(x, y, z);
		if(te instanceof TileEntityCamouflageable) 
			return ((TileEntityCamouflageable) te).Decoration.getIcon(side, ((TileEntityCamouflageable) te).DecorationMeta);
		else
			return null;
	}

}
