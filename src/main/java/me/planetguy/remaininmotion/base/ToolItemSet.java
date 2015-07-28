package me.planetguy.remaininmotion.base;

import java.util.List;

import me.planetguy.lib.prefab.ItemBase;
import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.util.Stack;
import me.planetguy.remaininmotion.api.ISpecialScrewdriverPunchBehavior;
import me.planetguy.remaininmotion.core.interop.ModInteraction.Wrenches;
import me.planetguy.remaininmotion.core.CreativeTab;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMItems;
import me.planetguy.remaininmotion.util.Registry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ToolItemSet extends ItemBase {
	public static int	Id;

	public enum Types {
		Screwdriver;

		public IIcon	Icon;

		public ItemStack Stack() {
			return (Stack.New(RiMItems.ToolItemSet, this));
		}
	}

	public ToolItemSet() {
		super("item."+ModRiM.Handle + "_ToolItemSet");
		setMaxStackSize(1);
		setCreativeTab(CreativeTab.Instance);
	}

	@Override
	public boolean hasContainerItem(ItemStack itemStack) {
		return (true);
	}

	@Override
	public ItemStack getContainerItem(ItemStack Item) {
		return (Stack.New(this, Item.getItemDamage()));
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack Item) {
		return (false);
	}

	@Override
	public String getItemStackDisplayName(ItemStack Item) {
		try {
			switch (Types.values()[Item.getItemDamage()]) {
				case Screwdriver:

					return (Lang.translate(ModRiM.Handle + ".screwdriver"));
			}
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();
		}

		return ("INVALID ITEM");
	}

	@Override
	public void getSubItems(Item i, CreativeTabs CreativeTab, List Showcase) {
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

	public static boolean IsScrewdriverOrEquivalent(ItemStack Item) {
		if (Item == null) { return (false); }

		if (Item.getItem() == RiMItems.ToolItemSet) {
			if (Item.getItemDamage() == Types.Screwdriver.ordinal()) { return (true); }
		}

		if (Wrenches.isAWrench(Item)) { return true; }

		return (false);
	}
	
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player) {
    	Block b=player.worldObj.getBlock(x, y, z);
    	if(b instanceof ISpecialScrewdriverPunchBehavior) {
    		return ((ISpecialScrewdriverPunchBehavior) b).onPunched(player.worldObj, x, y, z);
    	} else {
    		return false;
    	}
    }
    
	@Override
	public boolean doesSneakBypassUse(World w, int X, int Y, int Z, EntityPlayer player) {
		return (true);
	}
	
}
