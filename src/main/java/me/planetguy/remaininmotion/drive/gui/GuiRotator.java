package me.planetguy.remaininmotion.drive.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class GuiRotator extends GuiDirectional {
    public GuiRotator(InventoryPlayer playerInv, TileEntity te) {
        super(playerInv, te);
    }

    @Override
    public void initGui() {
        super.initGui();

        createButton(0,13,Buttons.TOGGLE_ADAPTER);
        stateToButtons();
    }

    @Override
    public String getLabel() {
        return "Carriage Rotator";
    }
}
