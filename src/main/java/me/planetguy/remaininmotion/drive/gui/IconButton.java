package me.planetguy.remaininmotion.drive.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class IconButton extends GuiButton{

	public Buttons icon;
	
	public boolean isActive;
	
	public ITooltipDrawer tooltips;

	public IconButton(int id, int x, int y, boolean isActive, Buttons icon, ITooltipDrawer d){
		super(id, x, y, 20, 20, "");
		this.icon=icon;
		this.isActive=isActive;
		tooltips=d;
	}

	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(Buttons.icons);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		boolean shouldDrawHighlighted = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

		int xDifference=0;

		if (shouldDrawHighlighted) {
			xDifference=20;
		}else{
			xDifference=0;
		}


		this.drawTexturedModalRect(this.xPosition, this.yPosition,
				xDifference, 0, 
				this.width, this.height);

		int posY=icon.posY;

		if(!isActive){
			posY+=16;
		}

		//draw widget
		this.drawTexturedModalRect(this.xPosition+2, this.yPosition+2,
				icon.posX, posY, 
				this.width-4, this.height-4);
		
		tooltips.drawTooltip(icon, mouseX, mouseY);
	}

	public void setIsActive(boolean b){
		isActive=b;
	}
}