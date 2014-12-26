package me.planetguy.remaininmotion.base;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.core.ModRiM;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class BlockRiM extends BlockContainer {
	public int	RenderId;

	@Override
	public int getRenderType() {
		return (RenderId);
	}

	public Class<? extends TileEntityRiM>[]	TileEntityClasses;

	@Override
	public boolean hasTileEntity(int meta) {
		try {
			if (meta >= 0 && meta < TileEntityClasses.length) { return (TileEntityClasses[meta] != null); }
		} catch (Throwable t) {
			Debug.exception(t);
		}

		return (false);
	}

	@Override
	public TileEntity createTileEntity(World World, int Meta) {
		try {
			TileEntityRiM te = TileEntityClasses[Meta].newInstance();
			return te;
		} catch (Throwable e) {
			e.printStackTrace();
			return (null);
		}
	}

	public BlockRiM(Block Template, Class<? extends ItemBlockRiM> BlockItemClass,
			Class<? extends TileEntityRiM>... TileEntityClasses) {
		super(Template.getMaterial());

		setBlockName(ModRiM.Handle + "_" + getClass().getSimpleName());

		setHardness(Template.getBlockHardness(null, 0, 0, 0));

		setStepSound(Template.stepSound);

		GameRegistry.registerBlock(this, BlockItemClass, getUnlocalizedName());

		this.TileEntityClasses = TileEntityClasses;

		for (Class<? extends TileEntityRiM> TileEntityClass : TileEntityClasses) {
			if (TileEntityClass != null) {
				GameRegistry.registerTileEntity(TileEntityClass, ModRiM.Handle + "_" + TileEntityClass.getSimpleName());
			}
		}
	}

	public abstract static class HarvestToolTypes {
		public static String	Pickaxe	= "pickaxe";

		public static String	Hatchet	= "axe";
	}

	public BlockRiM(Block Template, Class<? extends ItemBlockRiM> BlockItemClass, String HarvestToolType,
			Class<? extends TileEntityRiM>... TileEntityClasses) {
		this(Template, BlockItemClass, TileEntityClasses);

		// TODO net . minecraftforge . common . MinecraftForge .
		// setBlockHarvestLevel ( this , HarvestToolType , 0 ) ;

		setCreativeTab(CreativeTab.Instance);
	}

	public void AddShowcaseStacks(java.util.List Showcase) {}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List items) {
		AddShowcaseStacks(items);
	}

	@Override
	public int quantityDropped(int Meta, int Fortune, java.util.Random Random) {
		return (0);
	}

	@Override
	public void onBlockPlacedBy(World World, int X, int Y, int Z, EntityLivingBase Entity, ItemStack Item) {
		super.onBlockPlacedBy(World, X, Y, Z, Entity, Item);

		try {
			((TileEntityRiM) World.getTileEntity(X, Y, Z)).Setup((EntityPlayer) Entity, Item);
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();
		}
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if (!world.isRemote) {
			if (!player.capabilities.isCreativeMode) {
				try {
					((TileEntityRiM) world.getTileEntity(x, y, z)).EmitDrops(this, world.getBlockMetadata(x, y, z));
				} catch (Throwable Throwable) {
					Throwable.printStackTrace();
				}
			}
		}

		return (super.removedByPlayer(world, player, x, y, z));
	}

	@Override
	public void dropBlockAsItem(World w, int x, int y, int z, ItemStack s) {
		super.dropBlockAsItem(w, x, y, z, s);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int meta) {
		try {
			return TileEntityClasses[meta].newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return super.getPickBlock(target, world, x, y, z);
	}

}
