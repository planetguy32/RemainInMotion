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
		buttonList.add(new ReorientButton(buttonID++, width/2, height/2 - 30, false, this));
		buttonList.add(new ReorientButton(buttonID++, width/2, height/2 - 8, true, this));
	}
	
	public String getLabel() {
		return "Directional Carriage Drive";
	}
	
	public void stateFromButtons(){
		super.stateFromButtons();
		state |= heading;
	}
	
    protected void actionPerformed(GuiButton b) {
    	super.actionPerformed(b);
    	if(b instanceof ReorientButton){
    		if(((ReorientButton) b).isUpAdjust){
    			heading=(heading+1)%6;
    		}else{
    			heading=(heading+5)%6; // +6-1 -> avoid having negative modulus results
    		}
    	}
    }

}
