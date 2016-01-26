package me.planetguy.remaininmotion.drive.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import me.planetguy.lib.prefab.GuiPrefab;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;
import me.planetguy.remaininmotion.network.PacketCarriageUpdate;

public class GuiDriveCommon extends GuiPrefab implements ITooltipDrawer {

	public boolean initialized=false;
	
	private static ResourceLocation rl=new ResourceLocation(ModRiM.Handle+":textures/gui/container/disposer.png");
	
	public TileEntityCarriageDrive cde;
	
	protected int buttonID=0;

	protected long state=0;
	
	private List<String> deferredTooltipLines;

    // TODO make a fucking gui for elevator mode
	
	public GuiDriveCommon(InventoryPlayer playerInv, TileEntity te) {
		super(new ContainerDrive(playerInv, te), rl);
		cde=(TileEntityCarriageDrive) te;
		stateFromTE(cde);
	}
	
	public void stateFromTE(TileEntityCarriageDrive te){
        // reset just to be sure
        state = 0;
		for(int i=0; i<6; i++){
			if(te.SideClosed[i])
				state=state|(1<<(i+3+Buttons.DOWN.ordinal()));
		}
		if(te.Continuous)
			state=state|(1<<(3+Buttons.CONTINUOUS_MODE.ordinal()));
        if(te.zeroContinuousCooldown)
            state=state|(1<<(3+Buttons.ZERO_COOLDOWN.ordinal()));
		if(te.requiresScrewdriverToOpen)
			state=state|(1<<(3+Buttons.SCREWDRIVER_MODE.ordinal()));
		if(!te.isAnchored)
			state=state|(1<<(3+Buttons.MOVE_WITH_CARRIAGE.ordinal()));
	}
	
	public void stateToButtons(){
		for(GuiButton b: (List<GuiButton>) this.buttonList){
			if(b instanceof IconButton){
				((IconButton) b).setIsActive(((state) & (1 << ((IconButton) b).icon.ordinal()+3))!=0);
			}
		}
	}
	
	public void stateFromButtons(){
        //reset just to be sure
        state = 0;
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
		
		createButton(-81, -60, Buttons.SCREWDRIVER_MODE);
		createButton(-59, -60, Buttons.CONTINUOUS_MODE);
        createButton(-14, -60, Buttons.ZERO_COOLDOWN);
		createAnchorButton();
		
		
		createButton(-59, -31, Buttons.NORTH);
		createButton(-37, -31, Buttons.DOWN);
		createButton(-81, -9, Buttons.WEST);
		createButton(-59, -9, Buttons.UP);
		createButton(-37, -9, Buttons.EAST);
		createButton(-59, 13, Buttons.SOUTH);
		stateToButtons();
	}
	
	protected void createAnchorButton(){
		createButton(-37, -60, Buttons.MOVE_WITH_CARRIAGE);
	}
	
	protected void createButton(int x, int y, Buttons button){
		buttonList.add(new IconButton(buttonID++, width/2 + x, height/2 + y, ((state & (1L<<button.ordinal()))!=0), button, this));
	}
	
	
	public boolean handle(IconButton b){
		b.isActive=!b.isActive;
		return true;
	}
	
    protected void actionPerformed(GuiButton b) {
    	if(b instanceof IconButton)
    		handle((IconButton) b);
    }

	@Override
	public void drawTooltip(List<String> icon, int mouseX, int mouseY) {
		deferredTooltipLines=icon;
	}

    // This shouldn't be needed now, but whatever
	public void onGuiClosed(){
		if(initialized){
			stateFromButtons();
			PacketCarriageUpdate.send(cde, state);
		}
	}
    
	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		FontRenderer fr=Minecraft.getMinecraft().fontRenderer;
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fr.drawString(getLabel(), 8, 6, 4210752);
		
		initialized=true;
	}
	
    public void drawScreen(int mouseX, int mouseY, float p_73863_3_) {
        deferredTooltipLines=null;
        super.drawScreen(mouseX, mouseY, p_73863_3_);
        if(deferredTooltipLines != null)
        	drawHoveringText(deferredTooltipLines, mouseX, mouseY, Minecraft.getMinecraft().fontRenderer);
    }

    // This only checks to see if a click was on a button
    private boolean clickOnButton(int x, int y) {
        for(Object button : buttonList) {
            if(button instanceof GuiButton) {
                if(x >= ((GuiButton) button).xPosition) {
                    if(x <= ((GuiButton) button).xPosition + ((GuiButton) button).width) {
                        if(y >= ((GuiButton) button).yPosition) {
                            if(y <= ((GuiButton) button).yPosition + ((GuiButton) button).height) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void mouseClicked(int screenX, int screenY, int key) {
        super.mouseClicked(screenX, screenY, key);

        // inited and left click and on a button
        if(initialized && key == 0 && clickOnButton(screenX,screenY)) {
            stateFromButtons();
            PacketCarriageUpdate.send(cde, state);
        }
    }
}
