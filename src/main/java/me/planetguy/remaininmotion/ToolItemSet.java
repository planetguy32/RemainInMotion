package me.planetguy.remaininmotion;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.base.Item;
import me.planetguy.remaininmotion.core.Mod;
import me.planetguy.remaininmotion.core.RiMItems;
import me.planetguy.remaininmotion.core.ModInteraction;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.ModInteraction.Wrenches;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ToolItemSet extends Item {
	public static int	Id;

	public enum Types {
		Screwdriver;

		public IIcon	Icon;

		public net.minecraft.item.ItemStack Stack() {
			return (Stack.New(RiMItems.ToolItemSet, this));
		}
	}

	public ToolItemSet() {
		super();

		setMaxStackSize(1);
	}

	@Override
	public boolean hasContainerItem() {
		return (true);
	}

	@Override
	public ItemStack getContainerItem(ItemStack Item) {
		return (Stack.New(this, Item.getItemDamage()));
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(net.minecraft.item.ItemStack Item) {
		return (false);
	}

	@Override
	public String getItemStackDisplayName(ItemStack Item) {
		try {
			switch (Types.values()[Item.getItemDamage()]) {
				case Screwdriver:

					return (Lang.translate(Mod.Handle + ".screwdriver"));
			}
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();
		}

		return ("INVALID ITEM");
	}

	@Override
	public void AddShowcaseStacks(java.util.List Showcase) {
		for (Types Type : Types.values()) {
			Showcase.add(Stack.New(this, Type));
		}
	}

	@Override
	public void registerIcons(IIconRegister IconRegister) {
		for (Types Type : Types.values()) {
			Type.Icon = Registry.RegisterIcon(IconRegister, Type.name());
		}
	}

	@Override
	public IIcon getIconFromDamage(int Damage) {
		try {
			return (Types.values()[Damage].Icon);
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();

			return (RIMBlocks.Spectre.getIcon(0, 0));
		}
	}

	public static boolean IsScrewdriverOrEquivalent(net.minecraft.item.ItemStack Item) {
		if (Item == null) { return (false); }

		if (Item.getItem() == RiMItems.ToolItemSet) {
			if (Item.getItemDamage() == Types.Screwdriver.ordinal()) { return (true); }
		}

		if (Wrenches.isAWrench(Item)) { return true; }

		return (false);
	}
}
