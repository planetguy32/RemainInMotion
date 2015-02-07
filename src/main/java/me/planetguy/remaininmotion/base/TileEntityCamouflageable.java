package me.planetguy.remaininmotion.base;

import me.planetguy.remaininmotion.carriage.ItemCarriage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class TileEntityCamouflageable extends TileEntityRiM{
	
	public Block Decoration;

	public int	DecorationMeta;

	@Override
	public void Setup(EntityPlayer Player, ItemStack Item) {
		Decoration = ItemCarriage.GetDecorationId(Item);

		DecorationMeta = ItemCarriage.GetDecorationMeta(Item);

	}
	
	public Block getDecoration() {
		return Decoration;
	}
	
	public int getDecorationMeta() {
		return DecorationMeta;
	}
	
	public boolean isSideCamouflaged(int side) {
		return false;
	}

}
