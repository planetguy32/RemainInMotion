package me.planetguy.remaininmotion;

import me.planetguy.remaininmotion.base.ItemRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.item.ItemStack;

public class SimpleItemSet extends ItemRiM {

	public SimpleItemSet() {
		super();
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + stack.getItemDamage();
	}

	@Override
	public void AddShowcaseStacks(java.util.List Showcase) {
		for (Types Type : Types.values()) {
			Showcase.add(Stack.New(this, Type));
		}
	}

	@Override
	public void registerIcons(net.minecraft.client.renderer.texture.IIconRegister IconRegister) {
		for (Types Type : Types.values()) {
			Type.Icon = Registry.RegisterIcon(IconRegister, Type.name());
		}
	}

	@Override
	public net.minecraft.util.IIcon getIconFromDamage(int Damage) {
		try {
			return (Types.values()[Damage].Icon);
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();

			return (RIMBlocks.Spectre.getIcon(0, 0));
		}
	}
}
