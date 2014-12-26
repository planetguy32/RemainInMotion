package me.planetguy.remaininmotion.drive;

import java.util.Arrays;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.Stack;
import me.planetguy.remaininmotion.Vanilla;
import me.planetguy.remaininmotion.base.BlockItem;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.Mod;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;

public class CarriageDriveItem extends BlockItem {
	public CarriageDriveItem(Block Id) {
		super(Id);
	}

	public static boolean GetPrivateFlag(net.minecraft.item.ItemStack Item) {
		if (Item.stackTagCompound != null) { return (Item.stackTagCompound.getBoolean("Private")); }

		return (((Item.getItemDamage() >>> 4) & 0x1) == 1);
	}

	public static int GetLabel(net.minecraft.item.ItemStack Item) {
		if (Item.stackTagCompound != null) { return (Item.stackTagCompound.getInteger("Label")); }

		return (Item.getItemDamage() >>> 5);
	}

	public static int GetTier(net.minecraft.item.ItemStack Item) {
		if (Item.stackTagCompound != null) { return (Item.stackTagCompound.getInteger("Tier")); }

		return (0);
	}

	public static int AddDyeToLabel(int Label, Vanilla.DyeTypes DyeType) {
		return (Label | (1 << DyeType.ordinal()));
	}

	public static boolean LabelHasDye(int Label, Vanilla.DyeTypes DyeType) {
		return (((Label >>> DyeType.ordinal()) & 0x1) == 1);
	}

	public static net.minecraft.item.ItemStack Stack(int Type, int Tier) {
		return (Stack(Type, Tier, false, 0));
	}

	public static net.minecraft.item.ItemStack Stack(int Type, int Tier, boolean Private, int Label) {
		net.minecraft.item.ItemStack Item = Stack.Tag(Stack.New(RIMBlocks.CarriageDrive, Type));

		Item.stackTagCompound.setBoolean("Private", Private);

		Item.stackTagCompound.setInteger("Label", Label);

		Item.stackTagCompound.setInteger("Tier", Tier);

		return (Item);
	}

	/*
	 * @Override public String getItemDisplayName ( net . minecraft . item .
	 * ItemStack Item ) {
	 * 
	 * try { switch ( CarriageDrive . Types . values ( ) [ GetBlockType ( Item )
	 * ] ) { case Engine :
	 * 
	 * return ( "Carriage Engine" ) ;
	 * 
	 * case Motor :
	 * 
	 * return ( "Carriage Motor" ) ;
	 * 
	 * case Translocator :
	 * 
	 * return ( "Carriage Translocator" ) ;
	 * 
	 * case Controller :
	 * 
	 * return ( "Carriage Controller" ) ;
	 * 
	 * case Transduplicator: return ("Carriage Transduplicator");
	 * 
	 * case Adapter: return "Carriage Adapter"; } } catch ( Throwable Throwable
	 * ) { Throwable . printStackTrace ( ) ; }
	 * 
	 * return ( "INVALID CARRIAGE DRIVE" ) ;
	 * 
	 * }
	 */

	@Override
	public void AddTooltip(net.minecraft.item.ItemStack Item, java.util.List TooltipLines) {
		int Type = GetBlockType(Item);

		if (Configuration.Cosmetic.ShowHelpInTooltips) {
			for (String s : Lang.translate(Mod.Handle + ".drive.tooltip." + GetBlockType(Item)).split("##/##")) {
				TooltipLines.add(s);
			}
		}

		if (Type == CarriageDrive.Types.Translocator.ordinal()) {
			if (Item.stackTagCompound == null) {
				TooltipLines.add(Arrays.asList(Lang.translate(Mod.Handle + ".pleaseUpdateCarriage").split("##/##")));
			}

			boolean Private = GetPrivateFlag(Item);

			int Label = GetLabel(Item);

			if (Private) {
				TooltipLines.add(Lang.translate(Mod.Handle + ".labelPrivate"));
			} else {
				TooltipLines.add(Lang.translate(Mod.Handle + ".label"));
			}

			if (Label == 0) {
				TooltipLines.add(Lang.translate(Mod.Handle + ".blank"));
			} else {
				for (Vanilla.DyeTypes DyeType : Vanilla.DyeTypes.values()) {
					if (LabelHasDye(Label, DyeType)) {
						TooltipLines.add(" - " + Lang.translate(Mod.Handle + DyeType.Handle));
					}
				}
			}
		}
	}
}
