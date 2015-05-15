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
		stateFromTE(cde);
	}
	
	public void stateFromTE(TileEntityCarriageDrive te){
		for(int i=0; i<6; i++){
			if(te.SideClosed[i])
				state=state|(1<<(i+3+Buttons.DOWN.ordinal()));
		}
		if(te.Continuous)
			state=state|(1<<(3+Buttons.CONTINUOUS_MODE.ordinal()));
		if(te.requiresScrewdriverToOpen)
			state=state|(1<<(3+Buttons.SCREWDRIVER_MODE.ordinal()));
	}
	
	public void stateFromButtons(){
		for(GuiButton b: (List<GuiButton>) this.buttonList){
			if(b instanceof IconButton){
				state=((IconButton) b).writeInto(state);
			}
		}
	}
	
	@Override
	public String getLabel() {
		return "Carriage Drive";
	}
	
	public void initGui(){		
		super.initGui();
		int iconID=0;
		
		createButton(-80, -60, Buttons.SCREWDRIVER_MODE);
		
		createButton(-58, -60, Buttons.CONTINUOUS_MODE);
		
		createButton(-58, -30, Buttons.NORTH);
		createButton(-36, -30, Buttons.DOWN);
		createButton(-80, -8, Buttons.WEST);
		createButton(-58, -8, Buttons.UP);
		createButton(-36, -8, Buttons.EAST);
		createButton(-58, 14, Buttons.NORTH);
		
	}
	
	private void createButton(int x, int y, Buttons button){
		buttonList.add(new IconButton(buttonID++, width/2 + x, height/2 + y, ((state & (1L<<button.ordinal()))!=0), button, this));
	}
	
	
	public boolean handle(IconButton b){
		Debug.dbg(b.isActive);
		b.isActive=!b.isActive;
		return true;
	}
	
    protected void actionPerformed(GuiButton b) {
    	if(b instanceof IconButton)
    		handle((IconButton) b);
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
