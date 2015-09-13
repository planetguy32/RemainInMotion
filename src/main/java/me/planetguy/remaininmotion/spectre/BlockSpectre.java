package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.base.ToolItemSet;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockSpectre extends BlockRiM {
	public BlockSpectre() {
		super(Blocks.bedrock, ItemSpectre.class, TileEntityMotiveSpectre.class, TileEntitySupportiveSpectre.class,
				TileEntityTeleportativeSpectre.class, TileEntityTransduplicativeSpectre.class,
				TileEntityRotativeSpectre.class, TileEntitySupportiveSpectre.class);
		RenderId = -1;
	}

	public enum Types {
		Motive, Supportive, Teleportative, Transduplicative, Rotative, SupportiveNoCollide;
	}
    @Override
    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB bb, List list, Entity entity) {
        if(world.getBlockMetadata(i,j,k) == Types.SupportiveNoCollide.ordinal()){
            //TileEntitySupportiveSpectre tile = (TileEntitySupportiveSpectre) world.getTileEntity(i,j,k);
            //if(tile.BB.isEmpty()) return;
            //for(AxisAlignedBB aabb : tile.BB) {
            //    AxisAlignedBB aabb1 = aabb.getOffsetBoundingBox(i,j,k);
            //    if(bb.intersectsWith(aabb1)) list.add(aabb1);
            //}
            return;
        }
        super.addCollisionBoxesToList(world, i, j, k, bb, list, entity);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        if(world.getBlockMetadata(i,j,k) == Types.SupportiveNoCollide.ordinal()){
            //TileEntitySupportiveSpectre tile = (TileEntitySupportiveSpectre) world.getTileEntity(i,j,k);
            //if(tile.BB.size() > 1) {
            //    return super.getCollisionBoundingBoxFromPool(world, i, j, k);
            //} else if(tile.BB.size() == 1) {
            //    return tile.BB.get(0);
            //} else {
                return null;
            //}
        }
        return super.getCollisionBoundingBoxFromPool(world, i, j, k);
    }

    @Override
	public boolean onBlockActivated(World World, int X, int Y, int Z, EntityPlayer Player, int Side, float HitX,
			float HitY, float HitZ) {
		if (World.isRemote) { return (false); }

		if (World.getBlockMetadata(X, Y, Z) != Types.Supportive.ordinal() && World.getBlockMetadata(X, Y, Z) != Types.SupportiveNoCollide.ordinal()) { return (false); }

		if (!ToolItemSet.IsScrewdriverOrEquivalent(Player.inventory.getCurrentItem())) { return (false); }

		WorldUtil.ClearBlock(World, X, Y, Z);

		return (true);
	}

	@Override
	public IIcon getIcon(int a, int b) {
		return Blocks.planks.getIcon(0, 0);
	}

	@Override
	public IIcon getIcon(IBlockAccess iblockaccess, int x, int y, int z, int meta) {
		return Blocks.planks.getIcon(iblockaccess, x, y, z, meta);
	}

	@Override
	public boolean isOpaqueCube() {
		// System.out.println("Render fallback (IOC): "+Configuration.Cosmetic.renderFallback);
		return (RiMConfiguration.Cosmetic.renderFallback);
	}

	@Override
	public boolean renderAsNormalBlock() {
		// System.out.println("Render fallback (RANB): "+Configuration.Cosmetic.renderFallback);
		return (RiMConfiguration.Cosmetic.renderFallback);
	}

    /**
     * Gets the light value of the specified block coords. Args: x, y, z
     */
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
    	/*
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityMotiveSpectre) {
            return ((TileEntityMotiveSpectre) te).getLightValue();
        } else if(te instanceof TileEntitySupportiveSpectre)
        {
            return ((TileEntitySupportiveSpectre) te).getLightValue();
        }
        */
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
