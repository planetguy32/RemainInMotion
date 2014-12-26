package me.planetguy.remaininmotion.base;

import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.core.Mod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemRiM extends net.minecraft.item.Item {
	public ItemRiM() {
		super();

		setUnlocalizedName(Mod.Handle + "_" + getClass().getSimpleName());

		setHasSubtypes(true);

		setCreativeTab(CreativeTab.Instance);

		cpw.mods.fml.common.registry.GameRegistry.registerItem(this, getUnlocalizedName(), Mod.Handle);
	}

	public void AddShowcaseStacks(java.util.List Showcase) {}

	@Override
	public void getSubItems(net.minecraft.item.Item i, CreativeTabs CreativeTab, java.util.List Showcase) {
		AddShowcaseStacks(Showcase);
	}

	public void AddTooltip(ItemStack Item, java.util.List TooltipLines) {}

	@Override
	public void addInformation(ItemStack Item, net.minecraft.entity.player.EntityPlayer Player,
			java.util.List TooltipLines, boolean Advanced) {
		AddTooltip(Item, TooltipLines);
	}

	@Override
	public boolean doesSneakBypassUse(World w, int X, int Y, int Z, EntityPlayer player) {
		return (true);
	}
}
