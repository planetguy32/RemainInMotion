package me.planetguy.remaininmotion.core.interop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import cpw.mods.fml.common.Loader;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.core.Core;
import me.planetguy.remaininmotion.core.interop.buildcraft.EventHandlerBuildcraft;
import me.planetguy.remaininmotion.core.interop.chickenchunks.EventHandlerChickenChunks;
import me.planetguy.remaininmotion.core.interop.enderio.EventHandlerEnderIO;
import me.planetguy.remaininmotion.core.interop.fmp.EventHandlerFMP;
import me.planetguy.remaininmotion.drive.TileEntityCarriageController;
import me.planetguy.remaininmotion.util.general.Computers;
import net.minecraft.item.ItemStack;

public abstract class ModInteraction {
	
	public static Field		PendingBlockUpdateSetField;
	public static Method	RemovePendingBlockUpdate;


	public static void Establish() {
		
		RiMRegistry.blockMoveBus.register(new EventHandlerAPI());
		
        if(Loader.isModLoaded("ChickenChunks")) {
        	RiMRegistry.blockMoveBus.register(new EventHandlerChickenChunks());
        }
        
    	if(Loader.isModLoaded("ForgeMultipart"))
		{
			RiMRegistry.blockMoveBus.register(new EventHandlerFMP());
		}
    	
    	if(Loader.isModLoaded("BuildCraft|Transport"))
    	{
    		RiMRegistry.blockMoveBus.register(new EventHandlerBuildcraft());
    	}
    	
    	if(Loader.isModLoaded("EnderIO"))
    	{
    		RiMRegistry.blockMoveBus.register(new EventHandlerEnderIO());
    	}
    	
		Wrenches.init();

		Computers.setup();

		if (Computers.load) {
			Core.CarriageControllerEntity = TileEntityCarriageController.class;
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
			return c.getDeclaredMethod(string, params);
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
}
