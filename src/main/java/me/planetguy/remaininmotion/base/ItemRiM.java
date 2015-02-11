package me.planetguy.remaininmotion.base;

import java.util.List;

import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.core.ModRiM;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class ItemRiM extends Item {
	public ItemRiM() {
		super();

		setUnlocalizedName(ModRiM.Handle + "_" + getClass().getSimpleName());

		setHasSubtypes(true);

		setCreativeTab(CreativeTab.Instance);

		GameRegistry.registerItem(this, getUnlocalizedName(), ModRiM.Handle);
	}

	public void AddShowcaseStacks(List Showcase) {}

	@Override
	public void getSubItems(Item i, CreativeTabs CreativeTab, List Showcase) {
		AddShowcaseStacks(Showcase);
	}

	public void AddTooltip(ItemStack Item, java.util.List TooltipLines) {}

	@Override
	public void addInformation(ItemStack Item, EntityPlayer Player, List TooltipLines, boolean Advanced) {
		AddTooltip(Item, TooltipLines);
	}

	@Override
	public boolean doesSneakBypassUse(World w, int X, int Y, int Z, EntityPlayer player) {
		return (true);
	}
}
