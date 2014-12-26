package me.planetguy.remaininmotion.carriage;

import java.util.Arrays;
import java.util.List;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.Stack;
import me.planetguy.remaininmotion.base.ItemBlockRiM;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemCarriage extends ItemBlockRiM {
	public ItemCarriage(Block b) {
		super(b);
	}

	public static int GetDecorationId(ItemStack Item) {
		if (Item.stackTagCompound != null) { return (Item.stackTagCompound.getInteger("DecorationId")); }

		return (Item.getItemDamage() >>> 8);
	}

	public static int GetDecorationMeta(ItemStack Item) {
		if (Item.stackTagCompound != null) { return (Item.stackTagCompound.getInteger("DecorationMeta")); }

		return ((Item.getItemDamage() >>> 4) & 0xF);
	}

	public static int GetTier(ItemStack Item) {
		if (Item.stackTagCompound != null) { return (Item.stackTagCompound.getInteger("Tier")); }

		return (0);
	}

	public static ItemStack Stack(int Type, int Tier) {
		return (Stack(Type, Tier, 0, 0));
	}

	public static ItemStack Stack(int Type, int Tier, int DecorationId, int DecorationMeta) {
		ItemStack Item = Stack.Tag(Stack.New(RIMBlocks.Carriage, Type));

		Item.stackTagCompound.setInteger("DecorationId", DecorationId);

		Item.stackTagCompound.setInteger("DecorationMeta", DecorationMeta);

		Item.stackTagCompound.setInteger("Tier", Tier);

		return (Item);
	}

	/*
	 * @Override public String getItemDisplayName ( net . minecraft . item .
	 * ItemStack Item ) { try { switch ( Carriage . Types . values ( ) [
	 * GetBlockType ( Item ) ] ) { case Frame :
	 * 
	 * return ( "Frame Carriage" ) ;
	 * 
	 * case Platform :
	 * 
	 * /* renaming is intentional * / return ( "Support Carriage" ) ;
	 * 
	 * case Structure :
	 * 
	 * return ( "Structure Carriage" ) ;
	 * 
	 * case Support :
	 * 
	 * /* renaming is intentional * / return ( "Platform Carriage" ) ;
	 * 
	 * case Template :
	 * 
	 * return ( "Template Carriage" ) ; } } catch ( Throwable Throwable ) {
	 * Throwable . printStackTrace ( ) ; }
	 * 
	 * return ( "INVALID CARRIAGE" ) ; }
	 */

	@Override
	public void AddTooltip(ItemStack Item, List TooltipLines) {
		if (RiMConfiguration.Cosmetic.ShowHelpInTooltips) {
			try {
				for (String s : Lang.translate(ModRiM.Handle + ".carriage.tooltip." + GetBlockType(Item))
						.split("##/##")) {
					TooltipLines.add(s);
				}

				/*
				 * switch ( Carriage . Types . values ( ) [ GetBlockType ( Item
				 * ) ] ) { case Frame :
				 * 
				 * TooltipLines . add ( "Carries blocks directly touching it" )
				 * ;
				 * 
				 * break ;
				 * 
				 * case Support :
				 * 
				 * TooltipLines . add ( "Carries straight line of blocks" ) ;
				 * TooltipLines . add (
				 * "(Formerly known as 'Support Carriage'.)" ) ;
				 * 
				 * break ;
				 * 
				 * case Structure :
				 * 
				 * TooltipLines . add ( "Carries entire cuboid of world" ) ;
				 * 
				 * break ;
				 * 
				 * case Platform :
				 * 
				 * TooltipLines . add (
				 * "Carries entire body of blocks it's connected to" ) ;
				 * TooltipLines . add (
				 * "(Formerly known as 'Platform Carriage'.)" ) ;
				 * 
				 * break ;
				 * 
				 * case Template :
				 * 
				 * TooltipLines . add (
				 * "Carries blocks in the exact pattern prepared" ) ;
				 * 
				 * break ; }
				 */
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();

				return;
			}
		}

		int DecorationId = GetDecorationId(Item);

		if (DecorationId == 0) { return; }

		if (Item.stackTagCompound == null) {
			TooltipLines.add(Arrays.asList(Lang.translate(ModRiM.Handle + ".pleaseUpdateCarriage").split("##/##")));
		}

		ItemStack Decoration = Stack.New(Block.getBlockById(DecorationId), GetDecorationMeta(Item));

		try {
			TooltipLines.add(Lang.translate(ModRiM.Handle + ".decoration")
					+ Decoration.getItem().getItemStackDisplayName(Decoration));
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();

			TooltipLines.add(Lang.translate(ModRiM.Handle + ".decoration") + "!!ERR!!");
		}
	}
}
