package me.planetguy.remaininmotion.carriage;

import java.util.List;

import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.base.BlockCamouflageable;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarriage extends BlockCamouflageable {
	public BlockCarriage() {
		super(Blocks.planks, ItemCarriage.class, TileEntityFrameCarriage.class, TileEntitySupportCarriage.class,
				TileEntityStructureCarriage.class, TileEntityPlatformCarriage.class, TileEntityTemplateCarriage.class,
				TileEntityMemoryCarriage.class);

		this.setHarvestLevel(HarvestToolTypes.Hatchet, 0);
	}

	public enum Types {
		Frame, Support, Structure, Platform, Template, Memory;

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
	public void AddShowcaseStacks(List Showcase) {
		//Exclude frame carriage - use the plain carriage where possible
		for (int i=0; i<Types.values().length; i++) {
			Showcase.add(ItemCarriage.Stack(i));
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
	public IIcon getIcon(IBlockAccess world, int X, int Y, int Z, int Side) {
		try {
			TileEntityCarriage carriage = (TileEntityCarriage) world.getTileEntity(X, Y, Z);

			Types type = Types.values()[world.getBlockMetadata(X, Y, Z)];

			if ((carriage.SideClosed[Side])) {
				IIcon ico = getIconCamouflaged(world, X, Y, Z, Side);
				if (ico != null) {
					return ico;
				} else {
					return type.ClosedIcon;
				}
			}

			return (type.OpenIcon);
		} catch (Throwable t) {
			t.printStackTrace();

			return (RIMBlocks.Spectre.getIcon(world, X, Y, Z, Side));
		}
	}

	@Override
	public boolean onBlockActivated(World World, int X, int Y, int Z, EntityPlayer Player, int Side, float HitX,
			float HitY, float HitZ) {
		if (World.isRemote) { return (false); }

		if (!ToolItemSet.IsScrewdriverOrEquivalent(Player.inventory.getCurrentItem())) { return (false); }

		try {
			((TileEntityCarriage) World.getTileEntity(X, Y, Z)).ToggleSide(Side, Player.isSneaking());
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();

			return (false);
		}

		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return (false);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		TileEntityCarriage tile = (TileEntityCarriage) world.getTileEntity(x, y, z);
		if (tile != null) {
			int decoID = Block.getIdFromBlock(tile.Decoration);
			int decoMeta = tile.DecorationMeta;
			ItemStack stack = ItemCarriage.Stack(world.getBlockMetadata(x, y, z), decoID, decoMeta);
			if (stack != null) { return stack; }
		}
		return super.getPickBlock(target, world, x, y, z);
	}

}
