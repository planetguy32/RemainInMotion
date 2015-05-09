package me.planetguy.remaininmotion.drive.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import me.planetguy.lib.prefab.ContainerPrefab;
import me.planetguy.lib.prefab.GuiPrefab;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;
import me.planetguy.remaininmotion.network.PacketCarriageUpdate;

public class GuiDriveCommon extends GuiPrefab implements ITooltipDrawer {

	private static ResourceLocation rl=new ResourceLocation(ModRiM.Handle+":textures/gui/container/disposer.png");
	
	public TileEntityCarriageDrive cde;
	
	int buttonID=0;

	private long state=0;
	
	
	public GuiDriveCommon(InventoryPlayer playerInv, TileEntity te) {
		super(new ContainerDrive(playerInv, te), rl);
		cde=(TileEntityCarriageDrive) te;
		readState(cde);
	}
	
	public void readState(TileEntityCarriageDrive te){
		for(int i=0; i<6; i++){
			if(te.SideClosed[i])
				state=state|(1<<(i+3+Buttons.DOWN.ordinal()));
		}
		if(te.Continuous)
			state=state|(1<<(3+Buttons.CONTINUOUS_MODE.ordinal()));
		if(te.requiresScrewdriverToOpen)
			state=state|(1<<(3+Buttons.SCREWDRIVER_MODE.ordinal()));
	}
	
	@Override
	public String getLabel() {
		return "Carriage Drive";
	}
	
	public void initGui(){		
		super.initGui();
		int iconID=0;
		for(int i=0; i<4; i++){
			for(int k=0; k<4; k++){
				buttonList.add(new IconButton(buttonID++, width/2 - 50 + i* 22, height/2 - 50 + k * 22, true, Buttons.values()[iconID++], this));
			}
		}
	}
	
	
	public boolean handle(GuiButton b){
		Debug.dbg(b.enabled);
		b.enabled=!b.enabled;
		return false;
	}
	
    protected void actionPerformed(GuiButton b) {
    	handle(b);
    }

	@Override
	public void drawTooltip(Buttons icon, int mouseX, int mouseY) {
		drawHoveringText(icon.getTooltip(), mouseX, mouseY, Minecraft.getMinecraft().fontRenderer);
	}
	
	public void onGuiClosed(){
		PacketCarriageUpdate.send(cde, state);
	}
    
    //TODO draw tooltips:  protected void drawHoveringText(List lines, int x, int y, FontRenderer renderer)


}
