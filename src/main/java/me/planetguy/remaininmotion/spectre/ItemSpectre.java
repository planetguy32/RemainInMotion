package me.planetguy.remaininmotion.spectre;

import java.util.List;

import me.planetguy.remaininmotion.base.ItemBlockRiM;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSpectre extends ItemBlockRiM {
	public ItemSpectre(Block b) {
		super(b);
	}

	@Override
	public void AddTooltip(ItemStack Item, List TooltipLines) {
		TooltipLines.add("NOT VALID FOR DIRECT USAGE");
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
			int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}

}
