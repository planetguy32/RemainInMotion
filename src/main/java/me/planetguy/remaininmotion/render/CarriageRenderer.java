package me.planetguy.remaininmotion.render;

import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.base.ItemBlockRiM;
import me.planetguy.remaininmotion.carriage.BlockCarriage;
import me.planetguy.remaininmotion.carriage.ItemCarriage;
import me.planetguy.remaininmotion.carriage.TileEntityTemplateCarriage;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class CarriageRenderer extends BlockRenderer {
	public CarriageRenderer() {
		RIMBlocks.Carriage.RenderId = Initialize(RIMBlocks.Carriage);
	}

	@Override
	public void Render(net.minecraft.tileentity.TileEntity TileEntity) {
		if (TileEntity instanceof TileEntityTemplateCarriage) {
			TileEntityTemplateCarriage Carriage = (TileEntityTemplateCarriage) TileEntity;

			if (Carriage.Pattern == null) { return; }

			if (Carriage.RenderPattern == false) { return; }

			SetSideSpan(0, 0, 1, 1);

			SetTextureSpan(BlockCarriage.PlaceholderIcon);

			for (BlockRecord Record : Carriage.Pattern) {
				RenderGhost(Record.X, Record.Y, Record.Z);
			}
		}
	}

	@Override
	public IIcon GetIcon(ItemStack Item, Directions Side) {
		if (Side != Directions.PosY) {
			Block decorationBlock = ItemCarriage.GetDecorationBlock(Item);
			if (decorationBlock != null && decorationBlock != Blocks.air) {
				int DecorationMeta = ItemCarriage.GetDecorationMeta(Item);

				try {
					return (decorationBlock.getIcon(Side.ordinal(), DecorationMeta));
				} catch (Throwable Throwable) {
					Throwable.printStackTrace();
					return (RIMBlocks.Carriage.getIcon(0, ItemBlockRiM.GetBlockType(Item)));
				}
			}
		}

		return (RIMBlocks.Carriage.getIcon(0, ItemBlockRiM.GetBlockType(Item)));
	}
}
