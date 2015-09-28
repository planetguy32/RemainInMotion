package me.planetguy.remaininmotion.base;

import java.util.List;

import me.planetguy.lib.prefab.ItemBase;
import me.planetguy.remaininmotion.util.Stack;
import me.planetguy.remaininmotion.core.CreativeTab;
import me.planetguy.remaininmotion.core.ItemTypes;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.util.Registry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class SimpleItemSet extends ItemBase {

	public SimpleItemSet() {
		//for legacy compatibility...
		super("item."+ModRiM.Handle + "_SimpleItemSet");
		this.setHasSubtypes(true);
		setCreativeTab(CreativeTab.Instance);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + stack.getItemDamage();
	}

	@Override
	public void getSubItems(Item i, CreativeTabs CreativeTab, List Showcase) {
		for (ItemTypes Type : ItemTypes.values()) {
			Showcase.add(Stack.New(this, Type));
		}
	}

	@Override
	public void registerIcons(IIconRegister IconRegister) {
		for (ItemTypes Type : ItemTypes.values()) {
			Type.Icon = Registry.RegisterIcon(IconRegister, Type.name());
		}
	}

	@Override
	public IIcon getIconFromDamage(int Damage) {
		try {
			return (ItemTypes.values()[Damage].Icon);
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();

			return (RIMBlocks.Spectre.getIcon(0, 0));
		}
	}
	
	@Override
	public int countTooltipLines() {
		return 1;
	}
}
