package me.planetguy.remaininmotion.fmp;

import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.core.Items;
import me.planetguy.remaininmotion.core.RIMBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = me.planetguy.remaininmotion.core.Mod.Handle+"_HollowCarriages", dependencies="after:"+me.planetguy.remaininmotion.core.Mod.Handle+";after:ForgeMultipart")
public class HollowCarriagesMod {
	
	boolean alive;
	public static Item hollowCarriage;
	
	@Optional.Method(modid = "ForgeMultipart")
	@Mod . EventHandler
	public void preInit (FMLPreInitializationEvent event ){
		alive=Loader.isModLoaded("ForgeMultipart");
		if(!alive)return;
		hollowCarriage=new FMPCarriageItem();
		hollowCarriage.setUnlocalizedName(me.planetguy.remaininmotion.core.Mod.Handle+":hollowCarriage");
		hollowCarriage.setCreativeTab(CreativeTab.Instance);
		
		GameRegistry.registerItem(hollowCarriage, "Hollow carriage");

		LanguageRegistry.addName(hollowCarriage, "Hollow carriage");

		//Attempting to fix FMP crashing when trying to set creative tab

		MultiPartRegistry.registerParts(new IPartFactory(){

			public TMultiPart createPart(String arg0, boolean arg1) {
				if(arg0.equals("FMPCarriage"))
				return new FMPCarriage();
				return null;
			}
			
		}, new String[]{"FMPCarriage"});
		
		RiMRegistry.registerMatcher(new FMPCarriageMatcher());
	}
	
	@Optional.Method(modid = "ForgeMultipart")
	@EventHandler
	public void postInit(FMLPostInitializationEvent ev){
		if(!alive)return;
		GameRegistry.addRecipe(new ItemStack(HollowCarriagesMod.hollowCarriage, 8), 
				"ccc", 
				"c c",
				"ccc",
				Character.valueOf('c'), new ItemStack(RIMBlocks.Carriage, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.SimpleItemSet), 
				new ItemStack(HollowCarriagesMod.hollowCarriage));
	}

}
