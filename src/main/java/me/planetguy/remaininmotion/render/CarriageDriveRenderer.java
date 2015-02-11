package me.planetguy.remaininmotion.render;

import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.Vanilla;
import me.planetguy.remaininmotion.base.ItemBlockRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive;
import me.planetguy.remaininmotion.drive.ItemCarriageDrive;
import me.planetguy.remaininmotion.drive.TileEntityCarriageTranslocator;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class CarriageDriveRenderer extends BlockRenderer {
	public CarriageDriveRenderer() {
		RIMBlocks.CarriageDrive.RenderId = Initialize(RIMBlocks.CarriageDrive);
	}

	public void UseFullLabel() {
		RiMConfiguration.TextureSets TextureSet = RiMConfiguration.TextureSets.values()[RiMConfiguration.TextureSets.TextureSet];

		SetSideSpan(TextureSet.LabelMinH, TextureSet.LabelMinV, TextureSet.LabelMaxH, TextureSet.LabelMaxV);
	}

	public void UsePartialLabel(double MinH, double MinV, double MaxH, double MaxV) {
		UseFullLabel();

		SubsectSideSpan(MinH, MinV, MaxH, MaxV);
	}

	public void UseFullIcon(IIcon Icon) {
		SetTextureSpan(Icon);
	}

	public void UsePartialIcon(IIcon Icon, double MinU, double MinV, double MaxU, double MaxV) {
		UseFullIcon(Icon);

		SubsectTextureSpan(MinU, MinV, MaxU, MaxV);
	}

	@Override
	public void Render(TileEntity TileEntity) {
		if (TileEntity instanceof TileEntityCarriageTranslocator) {
			TileEntityCarriageTranslocator Translocator = (TileEntityCarriageTranslocator) TileEntity;

			if (Translocator.Player == null) { return; }

			UseFullLabel();

			if (Translocator.Player.equals("")) {
				UseFullIcon(BlockCarriageDrive.PublicIcon);
			} else {
				if (Translocator.Player.equals(Minecraft.getMinecraft().thePlayer.getDisplayName())) {
					UseFullIcon(BlockCarriageDrive.PrivateToSelfIcon);
				} else {
					UseFullIcon(BlockCarriageDrive.PrivateToOtherIcon);
				}
			}

			RenderOverlay(0.001, Translocator.SideClosed, false);

			for (Vanilla.DyeTypes DyeType : Vanilla.DyeTypes.values()) {
				if (ItemCarriageDrive.LabelHasDye(Translocator.Label, DyeType)) {
					double GridH = ((double) (DyeType.ordinal() % 4)) / 4;
					double GridV = ((double) (DyeType.ordinal() / 4)) / 4;

					UsePartialLabel(GridH, GridV, GridH + 0.25, GridV + 0.25);

					UsePartialIcon(BlockCarriageDrive.DyeIconSet, GridH, GridV, GridH + 0.25, GridV + 0.25);

					RenderOverlay(0.002, Translocator.SideClosed, false);
				}
			}
		}
	}

	@Override
	public IIcon GetIcon(ItemStack Item, Directions Side) {

		IIcon ico = RIMBlocks.CarriageDrive.getIcon(Side.ordinal(), ItemBlockRiM.GetBlockType(Item));
		if (ico == null) {
			return Blocks.stone.getIcon(0, 0);
		} else {
			return (ico);
		}
	}
}
