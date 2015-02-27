package me.planetguy.remaininmotion.base;

import java.util.List;

import me.planetguy.lib.util.NBTUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class ItemBlockRiM extends ItemBlockWithMetadata {
	public ItemBlockRiM(Block id) {
		super(id, id);

		setHasSubtypes(true);
	}

	public static int GetBlockType(ItemStack Item) {
		return (Item.getItemDamage());
	}

	public void AddTooltip(ItemStack Item, List TooltipLines) {}

	@Override
	public void addInformation(ItemStack Item, EntityPlayer Player, List TooltipLines, boolean AdvancedTooltipsActive) {
		AddTooltip(Item, TooltipLines);
	}

	@Override
	public String getUnlocalizedName(ItemStack s) {
		return super.getUnlocalizedName(s) + "." + s.getItemDamage();
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata){
		if(super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
			TileEntity te=world.getTileEntity(x, y, z);
			if(te != null) {
				NBTTagCompound itemTag=(NBTTagCompound) stack.getTagCompound();
				NBTTagCompound readTag=new NBTTagCompound();
				te.writeToNBT(readTag);
				NBTUtils.copySubTags(itemTag, readTag);
				te.readFromNBT(readTag);
			}
			return true;
		}else {
			return false;
		}
	}

}
