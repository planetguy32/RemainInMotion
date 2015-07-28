package me.planetguy.remaininmotion.carriage;

import java.util.Arrays;
import java.util.List;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.util.Stack;
import me.planetguy.remaininmotion.base.ItemBlockRiM;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ItemCarriage extends ItemBlockRiM {
	public ItemCarriage(Block b) {
		super(b);
	}

	//Helpers for accessing data in carriage items
	//Current system uses NBT data
	//Really old system used itemstack metadata
	public static Block GetDecorationBlock(ItemStack Item) {
		if (Item.stackTagCompound != null) {
			return Block.getBlockById(Item.getTagCompound().getInteger("DecorationId")); 
		} else {
			return Block.getBlockFromItem(Item.getItem());
		}
	}

	public static int GetDecorationMeta(ItemStack Item) {
		if (Item.stackTagCompound != null) {
			return (Item.stackTagCompound.getInteger("DecorationMeta"));
		} else {
			return Item.getItemDamage();
		}
	}

	public static int GetTier(ItemStack Item) {
		if (Item.stackTagCompound != null) {
			return (Item.stackTagCompound.getInteger("Tier"));
		} else {
			return (0);
		}
	}

	public static ItemStack Stack(int Type) {
		return (Stack(Type, 0, 0));
	}

	public static ItemStack Stack(int Type, int DecorationId, int DecorationMeta) {
		ItemStack Item = Stack.Tag(Stack.New(RIMBlocks.Carriage, Type));

		Item.stackTagCompound.setInteger("DecorationId", DecorationId);

		Item.stackTagCompound.setInteger("DecorationMeta", DecorationMeta);

		return (Item);
	}

	@Override
	public void AddTooltip(ItemStack Item, List TooltipLines) {
		if (RiMConfiguration.Cosmetic.ShowHelpInTooltips) {
			try {
				for (String s : Lang.translate(ModRiM.Handle + ".carriage.tooltip." + GetBlockType(Item))
						.split("##/##")) {
					TooltipLines.add(s);
				}
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();

				return;
			}
		}

		Block DecorationId = GetDecorationBlock(Item);

		if (DecorationId == Blocks.air) { return; }

		if (Item.stackTagCompound == null) {
			TooltipLines.addAll(Arrays.asList(Lang.translate(ModRiM.Handle + ".pleaseUpdateCarriage").split("##/##")));
		}

		ItemStack Decoration = Stack.New(DecorationId, GetDecorationMeta(Item));

		try {
			TooltipLines.add(Lang.translate(ModRiM.Handle + ".decoration")
					+ Decoration.getItem().getItemStackDisplayName(Decoration));
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();

			TooltipLines.add(Lang.translate(ModRiM.Handle + ".decoration") + "!!ERR!!");
		}
	}
}
