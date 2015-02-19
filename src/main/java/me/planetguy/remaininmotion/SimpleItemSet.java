package me.planetguy.remaininmotion;

import java.util.List;

import me.planetguy.remaininmotion.base.ItemRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class SimpleItemSet extends ItemRiM {

	public SimpleItemSet() {
		super();
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + stack.getItemDamage();
	}

	@Override
	public void AddShowcaseStacks(List Showcase) {
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
}
