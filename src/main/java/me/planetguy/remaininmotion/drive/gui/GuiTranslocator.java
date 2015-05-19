package me.planetguy.remaininmotion.drive.gui;

import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;
import me.planetguy.remaininmotion.drive.TileEntityCarriageTranslocator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class GuiTranslocator extends GuiDriveCommon {
	
	public GuiTranslocator(InventoryPlayer playerInv, TileEntity te) {
		super(playerInv, te);
	}
	
	public void initGui(){		
		createButton(-5, -60, Buttons.PRIVATE);
		int button=0;
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				createButton(22*i-5, -30+22*j, Buttons.values()[button++]);
			}
		}
		super.initGui();
	}
	
	public void stateFromTE(TileEntityCarriageDrive te){
		state |= (((long)((TileEntityCarriageTranslocator)te).Label)<<3);
		state |= (((TileEntityCarriageTranslocator)te).Player.equals("") ? 0 : 1L<< (Buttons.PRIVATE.ordinal()+3));
		super.stateFromTE(te);
	}
	
	public String getLabel() {
		return "Carriage Translocator";
	}
	
	// Do nothing - translocators 
	protected void createContinuousButton(){
		
	}

}
