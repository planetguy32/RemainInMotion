package me.planetguy.remaininmotion.base;

import me.planetguy.remaininmotion.carriage.ItemCarriage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityCamouflageable extends TileEntityRiM {

	public Block	Decoration;

	public int		DecorationMeta;

	@Override
	public void Setup(EntityPlayer Player, ItemStack Item) {
		Decoration = ItemCarriage.GetDecorationBlock(Item);

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
	
	@Override
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		Decoration = Block.getBlockById(TagCompound.getInteger("DecorationId"));

		DecorationMeta = TagCompound.getInteger("DecorationMeta");
	}
	
	@Override
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		TagCompound.setInteger("DecorationId", Block.getIdFromBlock(Decoration));

		TagCompound.setInteger("DecorationMeta", DecorationMeta);
	}

}
