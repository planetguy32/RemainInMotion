package me.planetguy.remaininmotion;

import me.planetguy.remaininmotion.core.ModRiM;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTab extends CreativeTabs {
	public CreativeTab() {
		super(ModRiM.Handle);
	}

	public static CreativeTab	Instance;

	public static void Prepare() {
		Instance = new CreativeTab();
	}

	@Override
	public String getTranslatedTabLabel() {
		return (ModRiM.Title);
	}

	public Item	TabIconItemIndex;

	public static void Initialize(Item TabIconItemIndex) {
		Instance.TabIconItemIndex = TabIconItemIndex;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return TabIconItemIndex;
	}
}
