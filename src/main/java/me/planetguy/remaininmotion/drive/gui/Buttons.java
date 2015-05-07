package me.planetguy.remaininmotion.drive.gui;

import me.planetguy.remaininmotion.core.ModRiM;
import net.minecraft.util.ResourceLocation;

public enum Buttons {
	//First row:
	DYE_0,
	DYE_1,
	DYE_2,
	DYE_3,
	DYE_4,
	DYE_5,
	DYE_6,
	DYE_7,
	DYE_8,
	DYE_9,
	DYE_10,
	DYE_11,
	DYE_12,
	DYE_13,
	DYE_14,
	DYE_15,
	
	//Second row:
	PRIVATE,
	DOWN,
	UP,
	NORTH,
	SOUTH,
	WEST,
	EAST,
	SCREWDRIVER_MODE,
	CONTINUOUS_MODE,
	HEADING_PLUS,
	HEADING_MINUS,
	
	;
	
	int posX, posY;
	
	Buttons() {
		posX=ordinal()%16; //12*20 fits into 256 pixels
		//integer division intended, skips last button row's 2 states (active/inactive) and 20px for button outlines
		posY=20+2*(ordinal()/16);
	}
	
	public static ResourceLocation icons=new ResourceLocation(ModRiM.Handle+":textures/gui/container/buttons.png");
	
}
