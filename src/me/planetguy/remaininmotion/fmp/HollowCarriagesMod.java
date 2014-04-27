package me.planetguy.remaininmotion.fmp;

import java.io.File;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;
import me.planetguy.remaininmotion.Blocks;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.api.RiMRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = me.planetguy.remaininmotion.Mod.Handle+"_HollowCarriages", dependencies="after:"+me.planetguy.remaininmotion.Mod.Handle+";after:ForgeMultipart")
public class HollowCarriagesMod {
	
	boolean alive;
	public static int hollowCarriageId;
	public static net.minecraft.item.Item hollowCarriage;
	
	@Optional.Method(modid = "ForgeMultipart")
	@Mod . EventHandler
	public void init (FMLPreInitializationEvent event ){
		Configuration cfg=new Configuration(event.getSuggestedConfigurationFile());
		cfg.load();
		hollowCarriageId=cfg.getItem("Hollow carriage", 11237).getInt(11237);
		cfg.save();
		
		alive=Loader.isModLoaded("ForgeMultipart");
		if(!alive)return;
		hollowCarriage=new FMPCarriageItem(HollowCarriagesMod.hollowCarriageId);
		hollowCarriage.setUnlocalizedName(me.planetguy.remaininmotion.Mod.Handle+":hollowCarriage");
		
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
		hollowCarriage.tabToDisplayOn=CreativeTab.Instance;
		GameRegistry.addRecipe(new ItemStack(HollowCarriagesMod.hollowCarriage, 8), 
				"ccc", 
				"c c",
				"ccc",
				Character.valueOf('c'), new ItemStack(Blocks.Carriage, 1, 0));
	}

}
