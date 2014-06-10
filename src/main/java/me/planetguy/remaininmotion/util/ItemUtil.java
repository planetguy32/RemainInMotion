package me.planetguy.remaininmotion.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtil {
	
	public static boolean matches(ItemStack stack, Item i){
		return stack.getItem().equals(i);
	}
	
	public static boolean matches(ItemStack stack, Block b){
		return matches(stack, Item.getItemFromBlock(b));
	}

}
