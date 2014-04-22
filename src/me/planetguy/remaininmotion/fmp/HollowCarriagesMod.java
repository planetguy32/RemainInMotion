package me.planetguy.remaininmotion.fmp;

import net.minecraft.item.ItemStack;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;
import me.planetguy.remaininmotion.Blocks;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.Items;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = me.planetguy.remaininmotion.Mod.Handle+"_HollowCarriages")
public class HollowCarriagesMod {
	
	@Optional.Method(modid = "ForgeMultipart")
	@Mod . EventHandler
	public void init (FMLPreInitializationEvent event ){
		Items.hollowCarriage=new FMPCarriageItem(Items.hollowCarriageId);

		LanguageRegistry.addName(((net.minecraft.item.Item)Items.hollowCarriage), "Hollow carriage");

		//Attempting to fix FMP crashing when trying to set creative tab
		((net.minecraft.item.Item)Items.hollowCarriage).tabToDisplayOn=CreativeTab.Instance;
		

		MultiPartRegistry.registerParts(new IPartFactory(){

			public TMultiPart createPart(String arg0, boolean arg1) {
				if(arg0.equals("FMPCarriage"))
				return new FMPCarriage();
				return null;
			}
			
		}, new String[]{"FMPCarriage"});
		
		GameRegistry.addRecipe(new ItemStack(Items.hollowCarriage, 8), 
				"ccc", 
				"c c",
				"ccc",
				Character.valueOf('c'), new ItemStack(Blocks.Carriage, 1, 0));
		
	}

}
