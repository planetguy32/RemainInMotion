package me.planetguy.remaininmotion.drive.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.core.ModRiM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ReorientButton extends GuiButton{

	boolean isUpAdjust;
	
	ITooltipDrawer tooltips;
	private List<String> tooltip;

	public ReorientButton(int id, int x, int y, boolean isUpAdjust , ITooltipDrawer d){
		super(id, x, y, 20, 20, "");
		tooltips=d;
		this.isUpAdjust=isUpAdjust;
		tooltip=new ArrayList<String>();
		tooltip.addAll(Arrays.asList(Lang.translate(ModRiM.Handle+":"+ (isUpAdjust ? "REORIENT_UP" : "REORIENT_DOWN") +".tooltip").split("##/##")));
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(Buttons.icons);
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

		//draw widget
		this.drawTexturedModalRect(this.xPosition+2, this.yPosition+2,
				0, 44+(isUpAdjust ? 1 : 0),
				this.width-4, this.height-4);
		
		tooltips.drawTooltip(tooltip, mouseX, mouseY);
	}

}
