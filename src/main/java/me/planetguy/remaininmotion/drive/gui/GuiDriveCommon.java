package me.planetguy.remaininmotion.drive.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import me.planetguy.lib.prefab.ContainerPrefab;
import me.planetguy.lib.prefab.GuiPrefab;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;

public class GuiDriveCommon extends GuiPrefab {

	private static ResourceLocation rl=new ResourceLocation(ModRiM.Handle+":textures/gui/container/disposer.png");
	
	public TileEntityCarriageDrive cde;
	
	public GuiDriveCommon(InventoryPlayer playerInv, TileEntity te) {
		super(new ContainerDrive(playerInv, te), rl);
		cde=(TileEntityCarriageDrive) te;
	}
	
	@Override
	public String getLabel() {
		return "Carriage Drive";
	}
	
	int buttonID=0;
	
	public void initGui(){		
		super.initGui();
		int iconID=0;
		for(int i=0; i<4; i++){
			for(int k=0; k<4; k++){
				buttonList.add(new IconButton(buttonID++, width/2 + i* 22, height/2 + k * 22, false, Buttons.values()[iconID++]));
			}
		}
	}
	
	
	public boolean handle(GuiButton b){
		Debug.dbg(b.enabled);
		b.enabled=!b.enabled;
		return false;
	}
	
    protected void actionPerformed(GuiButton b)
    {
    	handle(b);
    }
	

}
