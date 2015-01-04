package me.planetguy.remaininmotion.base;

import java.util.LinkedHashMap;
import java.util.List;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.carriage.TileEntityFrameCarriage;
import me.planetguy.remaininmotion.carriage.TileEntityMemoryCarriage;
import me.planetguy.remaininmotion.carriage.TileEntityPlatformCarriage;
import me.planetguy.remaininmotion.carriage.TileEntityStructureCarriage;
import me.planetguy.remaininmotion.carriage.TileEntitySupportCarriage;
import me.planetguy.remaininmotion.carriage.TileEntityTemplateCarriage;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.drive.TileEntityCarriageAdapter;
import me.planetguy.remaininmotion.drive.TileEntityCarriageController;
import me.planetguy.remaininmotion.drive.TileEntityCarriageEngine;
import me.planetguy.remaininmotion.drive.TileEntityCarriageMotor;
import me.planetguy.remaininmotion.drive.TileEntityCarriageRotator;
import me.planetguy.remaininmotion.drive.TileEntityCarriageTransduplicator;
import me.planetguy.remaininmotion.drive.TileEntityCarriageTranslocator;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import me.planetguy.remaininmotion.spectre.TileEntityRotativeSpectre;
import me.planetguy.remaininmotion.spectre.TileEntityTeleportativeSpectre;
import me.planetguy.remaininmotion.spectre.TileEntityTransduplicativeSpectre;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class BlockRiM extends BlockContainer {
	public int	RenderId;

	@Override
	public int getRenderType() {
		return (RenderId);
	}

	public Class<? extends TileEntityRiM>[]								TileEntityClasses;
	public static LinkedHashMap<Class<? extends TileEntityRiM>, String>	legacyClassToNameMap;

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

	public static void initLegacyClassMap() {
		legacyClassToNameMap = new LinkedHashMap();
		
		// Frames
		legacyClassToNameMap.put(TileEntityFrameCarriage.class, "JAKJ_RedstoneInMotion_FrameCarriageEntity");
		legacyClassToNameMap.put(TileEntitySupportCarriage.class, "JAKJ_RedstoneInMotion_SupportCarriageEntity");
		legacyClassToNameMap.put(TileEntityStructureCarriage.class, "JAKJ_RedstoneInMotion_StructureCarriageEntity");
		legacyClassToNameMap.put(TileEntityPlatformCarriage.class, "JAKJ_RedstoneInMotion_PlatformCarriageEntity");
		legacyClassToNameMap.put(TileEntityTemplateCarriage.class, "JAKJ_RedstoneInMotion_TemplateCarriageEntity");
		legacyClassToNameMap.put(TileEntityMemoryCarriage.class, "JAKJ_RedstoneInMotion_MemoryCarriageEntity");
		
		// Drives
		legacyClassToNameMap.put(TileEntityCarriageEngine.class, "JAKJ_RedstoneInMotion_CarriageEngineEntity");
		legacyClassToNameMap.put(TileEntityCarriageMotor.class, "JAKJ_RedstoneInMotion_CarriageMotorEntity");
		legacyClassToNameMap.put(TileEntityCarriageController.class, "JAKJ_RedstoneInMotion_CarriageControllerEntity");
		legacyClassToNameMap.put(TileEntityCarriageTranslocator.class,
				"JAKJ_RedstoneInMotion_CarriageTranslocatorEntity");
		legacyClassToNameMap.put(TileEntityCarriageTransduplicator.class,
				"JAKJ_RedstoneInMotion_CarriageTransduplicatorEntity");
		legacyClassToNameMap.put(TileEntityCarriageAdapter.class, "JAKJ_RedstoneInMotion_CarriageAdapterEntity");
		legacyClassToNameMap.put(TileEntityCarriageRotator.class, "JAKJ_RedstoneInMotion_CarriageRotatorEntity");
		
		// Specters
		legacyClassToNameMap.put(TileEntityMotiveSpectre.class, "JAKJ_RedstoneInMotion_MotiveSpectreEntity");
		legacyClassToNameMap.put(TileEntityTeleportativeSpectre.class,
				"JAKJ_RedstoneInMotion_TeleportativeSpectreEntity");
		legacyClassToNameMap.put(TileEntityTransduplicativeSpectre.class,
				"JAKJ_RedstoneInMotion_TransduplicativeSpectreEntity");
		legacyClassToNameMap.put(TileEntityRotativeSpectre.class, "JAKJ_RedstoneInMotion_RotativeSpectreEntity");

		for (Class<? extends TileEntityRiM> TileEntityClass : legacyClassToNameMap.keySet()) {
			if (TileEntityClass != null) {
				if (legacyClassToNameMap.get(TileEntityClass) != null) {
					GameRegistry.registerTileEntity(TileEntityClass, legacyClassToNameMap.get(TileEntityClass));
				}
			}
		}
	}

	public BlockRiM(Block Template, Class<? extends ItemBlockRiM> BlockItemClass,
			Class<? extends TileEntityRiM>... TileEntityList) {
		super(Template.getMaterial());

		TileEntityClasses = TileEntityList;

		setBlockName(ModRiM.Handle + "_" + getClass().getSimpleName().substring(5, getClass().getSimpleName().length()));

		setHardness(Template.getBlockHardness(null, 0, 0, 0));

		setStepSound(Template.stepSound);

		GameRegistry.registerBlock(this, BlockItemClass, getUnlocalizedName());

		this.setCreativeTab(CreativeTab.Instance);
	}

	public abstract static class HarvestToolTypes {
		public static String	Pickaxe	= "pickaxe";

		public static String	Hatchet	= "axe";
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

	@SuppressWarnings("deprecation")
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
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
		try {
			TileEntityRiM tile = (TileEntityRiM) worldObj.getTileEntity(x, y, z);
			tile.rotateSpecial(axis);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
