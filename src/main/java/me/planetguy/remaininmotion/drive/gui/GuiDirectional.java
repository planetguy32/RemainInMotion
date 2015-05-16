package me.planetguy.remaininmotion.drive.gui;

import java.util.List;

import me.planetguy.remaininmotion.drive.TileEntityCarriageDirected;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class GuiDirectional extends GuiDriveCommon {
	
	int heading;

	public GuiDirectional(InventoryPlayer playerInv, TileEntity te) {
		super(playerInv, te);
		heading=((TileEntityCarriageDirected) te).pointedDir.ordinal();
	}
	
	public void initGui(){
		super.initGui();
		
	}
	
	public String getLabel() {
		return "Directional Carriage Drive";
	}
	
	public void stateFromButtons(){
		super.stateFromButtons();
		state |= heading;
	}

}
