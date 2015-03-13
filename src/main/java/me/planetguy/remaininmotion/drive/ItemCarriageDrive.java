package me.planetguy.remaininmotion.drive;

import java.util.Arrays;
import java.util.List;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.util.Stack;
import me.planetguy.remaininmotion.util.Vanilla;
import me.planetguy.remaininmotion.base.ItemBlockRiM;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemCarriageDrive extends ItemBlockRiM {
	public ItemCarriageDrive(Block Id) {
		super(Id);
	}

	public static boolean GetPrivateFlag(ItemStack Item) {
		if (Item.stackTagCompound != null) { return (Item.stackTagCompound.getBoolean("Private")); }

		return (((Item.getItemDamage() >>> 4) & 0x1) == 1);
	}

	public static int GetLabel(ItemStack Item) {
		if (Item.stackTagCompound != null) { return (Item.stackTagCompound.getInteger("Label")); }

		return (Item.getItemDamage() >>> 5);
	}

	public static int GetTier(ItemStack Item) {
		if (Item.stackTagCompound != null) { return (Item.stackTagCompound.getInteger("Tier")); }

		return (0);
	}

	public static int AddDyeToLabel(int Label, Vanilla.DyeTypes DyeType) {
		return (Label | (1 << DyeType.ordinal()));
	}

	public static boolean LabelHasDye(int Label, Vanilla.DyeTypes DyeType) {
		return (((Label >>> DyeType.ordinal()) & 0x1) == 1);
	}

	public static ItemStack Stack(int Type, int Tier) {
		return (Stack(Type, Tier, false, 0));
	}

	public static ItemStack Stack(int Type, int Tier, boolean Private, int Label) {
		ItemStack Item = Stack.Tag(Stack.New(RIMBlocks.CarriageDrive, Type));

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
	public void AddTooltip(ItemStack Item, List TooltipLines) {
		int Type = GetBlockType(Item);

		if (RiMConfiguration.Cosmetic.ShowHelpInTooltips) {
			for (String s : Lang.translate(ModRiM.Handle + ".drive.tooltip." + GetBlockType(Item)).split("##/##")) {
				TooltipLines.add(s);
			}
		}

		if (Type == BlockCarriageDrive.Types.Translocator.ordinal()) {
			if (Item.stackTagCompound == null) {
				TooltipLines.add(Arrays.asList(Lang.translate(ModRiM.Handle + ".pleaseUpdateCarriage").split("##/##")));
			}

			boolean Private = GetPrivateFlag(Item);

			int Label = GetLabel(Item);

			if (Private) {
				TooltipLines.add(Lang.translate(ModRiM.Handle + ".labelPrivate"));
			} else {
				TooltipLines.add(Lang.translate(ModRiM.Handle + ".label"));
			}

			if (Label == 0) {
				TooltipLines.add(Lang.translate(ModRiM.Handle + ".blank"));
			} else {
				for (Vanilla.DyeTypes DyeType : Vanilla.DyeTypes.values()) {
					if (LabelHasDye(Label, DyeType)) {
						TooltipLines.add(" - " + Lang.translate(ModRiM.Handle + DyeType.Handle));
					}
				}
			}
		}
	}
}
