package me.planetguy.remaininmotion.base;

import me.planetguy.remaininmotion.core.RiMConfiguration;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockCamouflageable extends BlockRiM implements ICamouflageable {

    public BlockCamouflageable(Block Template, Class<? extends ItemBlockRiM> BlockItemClass,
                               Class<? extends TileEntityRiM>... TileEntityList) {
        super(Template, BlockItemClass, TileEntityList);

    }

    @Override
    public IIcon getIconCamouflaged(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityCamouflageable) {
        	try {
        		// Try/catch this - it's not unlikely that mods will do an unsafe cast to
        		// their mod's tile entity here.
        		//
        		// included for compatibility with blocks that use position-sensitive but
        		// not TE-sensitive icon logic, like applying biome colour
                if(((TileEntityCamouflageable) te).Decoration instanceof BlockCamouflageable) throw new Exception("Making a frame look like another? Interesting....");

                // This line causes White Wool to have issues (and other metadata 0 blocks), we need to find a better way
                if(true || ((TileEntityCamouflageable) te).DecorationMeta != 0) throw new Exception("This method will almost always attempt to get metadata from world, resulting in the wrong texture.");
                return ((TileEntityCamouflageable) te).Decoration.getIcon
                		(world, x, y, z, side);
        	} catch(Exception e) {
                return ((TileEntityCamouflageable) te).Decoration.getIcon(side,
                        ((TileEntityCamouflageable) te).DecorationMeta);
        	}

        } else {
            return null;
        }
    }

    /**
     * Gets the light value of the specified block coords. Args: x, y, z
     */
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityCamouflageable) {
            Block deco = ((TileEntityCamouflageable) te).Decoration;
            if(deco != null)
            {
                try {
                    if (deco instanceof BlockCamouflageable)
                        throw new Exception("Making a frame look like another? Interesting....");
                    return ((Block) deco).getLightValue();
                }catch(Exception e) {}
            }
        }
        return super.getLightValue();

    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityCamouflageable) {
            Block deco = ((TileEntityCamouflageable) te).Decoration;
            if(deco != null)
            {
                try {
                    if(deco instanceof BlockCamouflageable) throw new Exception("Making a frame look like another? Interesting....");
                    return ((Block) deco).getLightOpacity();
                }catch(Exception e) {}
            }
        }
        return super.getLightOpacity();
    }

    // Make grass and stuff work.
    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        if(!RiMConfiguration.DirtyHacks.experimentalColor) return super.colorMultiplier(world, x, y, z);
        TileEntity te = (TileEntity) world.getTileEntity(x,y,z);

        if (te != null && te instanceof TileEntityCamouflageable) {

            Block deco = ((TileEntityCamouflageable) te).Decoration;
            if(deco != null)
            {
                try {
                    if(deco instanceof BlockCamouflageable) throw new Exception("Making a frame look like another? Interesting....");
                    return ((Block) deco).colorMultiplier(world, x, y, z);
                }catch(Exception e){
                    return 0xffffffff;
                }
            }
        }
        return super.colorMultiplier(world, x, y, z);
    }
}
