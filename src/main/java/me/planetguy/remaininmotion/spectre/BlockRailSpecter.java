package me.planetguy.remaininmotion.spectre;

import cpw.mods.fml.common.registry.GameRegistry;
import me.planetguy.remaininmotion.core.ModRiM;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailSpecter extends BlockRailBase implements ITileEntityProvider {

    public BlockRailSpecter() {
        super(false);
        setCreativeTab(null);

        setBlockName(ModRiM.Handle + "_" + getClass().getSimpleName().substring(5, getClass().getSimpleName().length()));

        setHardness(-1);

        setStepSound(Block.soundTypeMetal);

        GameRegistry.registerBlock(this, ItemSpectre.class, getUnlocalizedName());
    }

    @Override
    public int quantityDropped(int Meta, int Fortune, java.util.Random Random) {
        return (0);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int meta) {
        try {
            return TileEntitySupportiveSpectre.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World World, int Meta) {
        try {
            return TileEntitySupportiveSpectre.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getRenderType() {
        return (-1);
    }

    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return true;
    }

    public int getMobilityFlag()
    {
        return 2;
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {}

    @Override
    public IIcon getIcon(int a, int b) {
        return Blocks.planks.getIcon(0, 0);
    }

    @Override
    public IIcon getIcon(IBlockAccess iblockaccess, int x, int y, int z, int meta) {
        return Blocks.planks.getIcon(iblockaccess, x, y, z, meta);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityMotiveSpectre) {
            return ((TileEntityMotiveSpectre) te).getLightValue();
        } else if(te instanceof TileEntitySupportiveSpectre)
        {
            return ((TileEntitySupportiveSpectre) te).getLightValue();
        }
        return super.getLightValue(world, x, y, z);

    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityMotiveSpectre) {
            return ((TileEntityMotiveSpectre) te).getLightOpacity();
        } else if(te instanceof TileEntitySupportiveSpectre)
        {
            return ((TileEntitySupportiveSpectre) te).getLightOpacity();
        }
        return super.getLightOpacity(world, x, y, z);
    }

}
