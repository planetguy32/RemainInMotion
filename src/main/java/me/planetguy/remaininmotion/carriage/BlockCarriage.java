package me.planetguy.remaininmotion.carriage;

import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.util.transformations.ArrayRotator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCarriage extends BlockRiM {
	public BlockCarriage() {
		super(Blocks.planks, ItemCarriage.class, HarvestToolTypes.Hatchet, TileEntityFrameCarriage.class,
				TileEntitySupportCarriage.class, TileEntityStructureCarriage.class, TileEntityPlatformCarriage.class,
				TileEntityTemplateCarriage.class);
	}

	public enum Types {
		Frame, Support, Structure, Platform, Template;

		public double	Burden	= 1.0;

		public IIcon	OpenIcon;
		public IIcon	ClosedIcon;
	}

	public enum Tiers {
		foo;

		public double	CarriageBurdenFactor	= 1.0;

		public double	CargoBurdenFactor		= 1.0;
	}

	public static IIcon	PlaceholderIcon;

	@Override
	public void AddShowcaseStacks(java.util.List Showcase) {
		for (Types Type : Types.values()) {
			Showcase.add(ItemCarriage.Stack(Type.ordinal(), 0));
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister IconRegister) {
		for (Types Type : Types.values()) {
			Type.ClosedIcon = Registry.RegisterIcon(IconRegister, Type.name() + "Carriage_Closed");

			Type.OpenIcon = Registry.RegisterIcon(IconRegister, Type.name() + "Carriage_Open");
		}

		PlaceholderIcon = Registry.RegisterIcon(IconRegister, "CarriagePlaceholder");
	}

	@Override
	public IIcon getIcon(int Side, int Meta) {
		try {
			if (Meta < Types.values().length && Meta >= 0) { return (Types.values()[Meta].OpenIcon); }
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();
		}

		return (RIMBlocks.Spectre.getIcon(0, 0));
	}

	@Override
	public IIcon getIcon(net.minecraft.world.IBlockAccess World, int X, int Y, int Z, int Side) {
		try {
			TileEntityCarriage Carriage = (TileEntityCarriage) World.getTileEntity(X, Y, Z);

			if ((Carriage.DecorationId != 0) && (Carriage.SideClosed[Side])) { return (net.minecraft.block.Block
					.getBlockById(Carriage.DecorationId).getIcon(Side, Carriage.DecorationMeta)); }

			Types Type = Types.values()[World.getBlockMetadata(X, Y, Z)];

			return (Carriage.SideClosed[Side] ? Type.ClosedIcon : Type.OpenIcon);
		} catch (Throwable t) {
			t.printStackTrace();

			return (RIMBlocks.Spectre.getIcon(0, 0));
		}
	}

	@Override
	public boolean onBlockActivated(net.minecraft.world.World World, int X, int Y, int Z,
			net.minecraft.entity.player.EntityPlayer Player, int Side, float HitX, float HitY, float HitZ) {
		if (World.isRemote) { return (false); }

		if (!ToolItemSet.IsScrewdriverOrEquivalent(Player.inventory.getCurrentItem())) { return (false); }

		try {
			((TileEntityCarriage) World.getTileEntity(X, Y, Z)).ToggleSide(Side, Player.isSneaking());
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();

			return (false);
		}

		return (true);
	}

	@Override
	public boolean isOpaqueCube() {
		return (false);
	}

	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
		try {
			ArrayRotator.rotate(((TileEntityCarriage) worldObj.getTileEntity(x, y, z)).SideClosed, axis);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
