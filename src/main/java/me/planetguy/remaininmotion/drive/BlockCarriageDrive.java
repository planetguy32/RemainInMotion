package me.planetguy.remaininmotion.drive;

import java.util.List;

import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.core.Core;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCarriageDrive extends BlockRiM {
	public BlockCarriageDrive() {
		super(Blocks.iron_block, ItemCarriageDrive.class, HarvestToolTypes.Pickaxe,

		TileEntityCarriageEngine.class, TileEntityCarriageMotor.class, TileEntityCarriageController.class,
				TileEntityCarriageTranslocator.class, TileEntityCarriageTransduplicator.class,
				TileEntityCarriageAdapter.class, TileEntityCarriageRotator.class);
	}

	public enum Types {
		Engine(1.0), Motor(1.01), Controller(1.1), Translocator(4.0), Transduplicator(0.0), Adapter(1.0), Rotator(1.0);

		public IIcon	NormalIcon;

		public IIcon	ContinuousIcon;

		public IIcon	NormalActiveIcon;

		public IIcon	ContinuousActiveIcon;

		public double	MaxBurden	= 1000.0;

		public double	EnergyConsumption;

		private Types(double energy) {
			EnergyConsumption = energy;
		}
	}

	public enum Tiers {
		wood(1.0, 1.0);

		private Tiers(double burden, double power) {
			EnergyConsumptionFactor = power;
			MaxBurdenFactor = burden;
		}

		public double	MaxBurdenFactor;

		public double	EnergyConsumptionFactor;
	}

	public static IIcon	InactiveIcon;

	public static IIcon	DyeIconSet;

	public static IIcon	PublicIcon;

	public static IIcon	PrivateToSelfIcon;

	public static IIcon	PrivateToOtherIcon;

	@Override
	public void AddShowcaseStacks(List Showcase) {
		for (Types Type : Types.values()) {
			if ((Type == Types.Controller) && (Core.CarriageControllerEntity == null)) {
				continue;
			}

			if (Type == Types.Translocator) {
				Showcase.add(ItemCarriageDrive.Stack(Type.ordinal(), 0, false, 0));
			} else {
				Showcase.add(ItemCarriageDrive.Stack(Type.ordinal(), 0));
			}
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister IconRegister) {
		for (Types Type : Types.values()) {
			if (Type == Types.Controller) {
				if (Core.CarriageControllerEntity == null) {
					continue;
				}
			} else {
				Type.ContinuousIcon = Registry.RegisterIcon(IconRegister, "Carriage" + Type.name() + "_Continuous");

				Type.ContinuousActiveIcon = Registry.RegisterIcon(IconRegister, "Carriage" + Type.name()
						+ "_Continuous_Active");
			}

			Type.NormalIcon = Registry.RegisterIcon(IconRegister, "Carriage" + Type.name());

			Type.NormalActiveIcon = Registry.RegisterIcon(IconRegister, "Carriage" + Type.name() + "_Active");
		}

		InactiveIcon = Registry.RegisterIcon(IconRegister, "CarriageDriveInactive");

		DyeIconSet = Registry.RegisterIcon(IconRegister, "CarriageTranslocator_LabelDyes");

		PublicIcon = Registry.RegisterIcon(IconRegister, "CarriageTranslocator_LabelPublic");

		PrivateToSelfIcon = Registry.RegisterIcon(IconRegister, "CarriageTranslocator_LabelPrivateToSelf");

		PrivateToOtherIcon = Registry.RegisterIcon(IconRegister, "CarriageTranslocator_LabelPrivateToOther");

		TileEntityCarriageRotator.onRegisterIcons(IconRegister);
	}

	@Override
	public IIcon getIcon(int Side, int Meta) {
		try {
			if (Meta == Types.Rotator.ordinal()) { return TileEntityCarriageRotator.icons[0][Side]; }
			return (Types.values()[Meta].NormalIcon);
		} catch (Throwable Throwable) {
			// Throwable . printStackTrace ( ) ; //Fix log spam with MapWriter

			return (RIMBlocks.Spectre.getIcon(0, 0));
		}
	}

	@Override
	public IIcon getIcon(IBlockAccess World, int X, int Y, int Z, int Side) {

		try {

			TileEntityCarriageDrive Drive = (TileEntityCarriageDrive) World.getTileEntity(X, Y, Z);

			int meta = World.getBlockMetadata(X, Y, Z);

			return Drive.getIcon(Side, meta);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public boolean onBlockActivated(World World, int X, int Y, int Z, EntityPlayer Player, int Side, float HitX,
			float HitY, float HitZ) {

		if (World.isRemote) { return (false); }

		if (!ToolItemSet.IsScrewdriverOrEquivalent(Player.inventory.getCurrentItem())) { return (false); }

		try {
			TileEntityCarriageDrive cde = (TileEntityCarriageDrive) World.getTileEntity(X, Y, Z);
			cde.lastUsingPlayer = Player;
			cde.HandleToolUsage(Side, Player.isSneaking());

		} catch (Throwable Throwable) {
			Throwable.printStackTrace();

			return (false);
		}

		return (true);
	}

	@Override
	public void onNeighborBlockChange(World World, int X, int Y, int Z, Block Id) {
		try {
			((TileEntityCarriageDrive) World.getTileEntity(X, Y, Z)).HandleNeighbourBlockChange();
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();
		}
	}

	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
		try {
			TileEntityCarriageDrive entity = ((TileEntityCarriageDrive) worldObj.getTileEntity(x, y, z));
			entity.rotate(axis);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
