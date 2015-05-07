package me.planetguy.lib.prefab;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public abstract class ContainerPrefab extends Container {
	
	public ContainerPrefab(InventoryPlayer inventoryPlayer, TileEntity te){
		makeSlots(te);
        bindPlayerInventory(inventoryPlayer);
	}
	
	public abstract void makeSlots(TileEntity te);

    @Override
    public boolean canInteractWith(EntityPlayer player) {
    	return true;//tileEntity.isUseableByPlayer(player);
    }

    private void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 9; j++) {
    			addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
    					8 + j * 18, 84 + i * 18));
    		}
    	}

    	for (int i = 0; i < 9; i++) {
    		addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
    	}
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer p, int slot){
        return null;
    }
}
