package me.planetguy.remaininmotion.core.interop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.api.event.EventManager;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.interop.mod.Buildcraft;
import me.planetguy.remaininmotion.core.interop.mod.BuildcraftClient;
import me.planetguy.remaininmotion.core.interop.mod.CarpentersBlocks;
import me.planetguy.remaininmotion.core.interop.mod.ChickenChunks;
import me.planetguy.remaininmotion.core.interop.mod.EnderIO;
import me.planetguy.remaininmotion.core.interop.mod.ForgeMultipart;
import me.planetguy.remaininmotion.core.interop.mod.ImmersiveEngineering;
import me.planetguy.remaininmotion.core.interop.mod.QmunityMultipart;
import me.planetguy.remaininmotion.drive.TileEntityCarriageController;
import me.planetguy.remaininmotion.util.general.Computers;
import net.minecraft.item.ItemStack;

public abstract class ModInteraction {
	
	public static Field		PendingBlockUpdateSetField;
	public static Method	RemovePendingBlockUpdate;
	
	public static Runnable qlInteraction;
	
	public static void Establish() {
		
		EventManager.registerEventHandler(new EventHandlerAPI());
		
        if(Loader.isModLoaded("ChickenChunks")) {
        	EventManager.registerEventHandler(new ChickenChunks());
        }
        
    	if(Loader.isModLoaded("ForgeMultipart")) {
    		if(Loader.isModLoaded("qmunitylib")){
    			qlInteraction=new QmunityMultipart();
    			EventManager.registerEventHandler(qlInteraction);
    			
    		}
    		EventManager.registerEventHandler(new ForgeMultipart());
		}
    	
    	if(Loader.isModLoaded("BuildCraft|Transport")) {
    		if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT){
    			EventManager.registerEventHandler(new BuildcraftClient());
    		}
    		EventManager.registerEventHandler(new Buildcraft());
    	}
    	
    	if(Loader.isModLoaded("EnderIO")) {
    		EventManager.registerEventHandler(new EnderIO());
    	}

        if(Loader.isModLoaded("CarpentersBlocks")) {
        	EventManager.registerEventHandler(new CarpentersBlocks());
        }
        
        if(Loader.isModLoaded("ImmersiveEngineering")) {
        	EventManager.registerEventHandler(new ImmersiveEngineering());
        }
        
		Wrenches.init();

		Computers.setup();

		if (Computers.load) {
			ModRiM.CarriageControllerEntity = TileEntityCarriageController.class;
		}

		PendingBlockUpdateSetField = getField(net.minecraft.world.WorldServer.class, "tickEntryQueue");

		RemovePendingBlockUpdate = getMethod(net.minecraft.world.WorldServer.class, "removeNextTickIfNeeded",
				net.minecraft.world.NextTickListEntry.class);
		
	}

	static Class getClass(String string) {
		try {
			return Class.forName(string);
		} catch (Exception e) {

		}
		return null;
	}

	private static Method getMethod(Class c, String string, Class... params) {
		try {
			Method m=c.getDeclaredMethod(string, params);
			m.setAccessible(true);
			return m;
		} catch (Exception e) {

		}
		return null;
	}

	private static Field getField(Class c, String string) {
		try {
			return c.getField(string);
		} catch (Exception e) {

		}
		return null;
	}

	public static abstract class Wrenches {

		static ArrayList<Class>	wrenchClasses	= new ArrayList<Class>();

		public static void init() {
			for (String className : new String[] {
					// can add or remove FQCNs here
					// should probably prefer API interface names where possible
					// note that many mods implement BC wrench API only if BC is
					// installed
					"buildcraft.api.tools.IToolWrench", // Buildcraft
					"resonant.core.content.ItemScrewdriver", // Resonant
					// Induction
					"ic2.core.item.tool.ItemToolWrench", // IC2
					"ic2.core.item.tool.ItemToolWrenchElectric", // IC2 (more)
					"mrtjp.projectred.api.IScrewdriver", // Project Red
					"mods.railcraft.api.core.items.IToolCrowbar", // Railcraft
					"com.bluepowermod.items.ItemScrewdriver", // BluePower
					"cofh.api.item.IToolHammer", // Thermal Expansion and
					// compatible
					"appeng.items.tools.quartz.ToolQuartzWrench", // Applied
					// Energistics
					"crazypants.enderio.api.tool.ITool", // Ender IO
					"mekanism.api.IMekWrench", // Mekanism
			}) {
				try {
					wrenchClasses.add(Class.forName(className));
				} catch (ClassNotFoundException e) {
					Debug.dbg("Failed to load wrench class " + className);
				}
			}
		}

		public static boolean isAWrench(ItemStack stk) {
			for (Class c : wrenchClasses) {
				if (stk != null && stk.getItem() != null
						&& (c.isInstance(stk.getItem()))) { return true; }
			}
			return false;
		}

	}

	public static void init() {
		if(qlInteraction != null) qlInteraction.run();
	}
}
