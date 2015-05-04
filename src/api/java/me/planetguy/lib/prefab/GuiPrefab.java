package me.planetguy.lib.prefab;

import org.lwjgl.opengl.GL11;

import me.planetguy.lib.util.Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class GuiPrefab extends GuiContainer {

	private ContainerPrefab container;
	
	private final ResourceLocation gui;
	
	public GuiPrefab(ContainerPrefab container, ResourceLocation gui){
		super(container);
		this.container=container;
		this.gui=gui;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		FontRenderer fr=Minecraft.getMinecraft().fontRenderer;
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fr.drawString(getLabel(), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fr.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(gui);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	
	/**
	 */
	public abstract String getLabel();

}
