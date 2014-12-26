package me.planetguy.remaininmotion.base;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public abstract class ItemBlockRiM extends ItemBlockWithMetadata {
	public ItemBlockRiM(Block id) {
		super(id, id);

		setHasSubtypes(true);
	}

	public static int GetBlockType(ItemStack Item) {
		return (Item.getItemDamage());
	}

	public void AddTooltip(ItemStack Item, java.util.List TooltipLines) {}

	@Override
	public void addInformation(ItemStack Item, net.minecraft.entity.player.EntityPlayer Player,
			java.util.List TooltipLines, boolean AdvancedTooltipsActive) {
		AddTooltip(Item, TooltipLines);
	}

	@Override
	public String getUnlocalizedName(ItemStack s) {
		return super.getUnlocalizedName(s) + "." + s.getItemDamage();
	}

}
