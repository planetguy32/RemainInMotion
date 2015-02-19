package me.planetguy.remaininmotion.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import cpw.mods.fml.common.Loader;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.drive.TileEntityCarriageController;
import me.planetguy.remaininmotion.util.general.Computers;
import net.minecraft.item.ItemStack;

public abstract class ModInteraction {

	public abstract static class ForgeMultipart {
		public static Class		MultipartSaveLoad;
		public static Method	MultipartSaveLoad_loadingWorld_$eq;

		public static Class		MultipartSPH;
		public static Method	MultipartSPH_onChunkWatch;

		public static Class		TileMultipart;
		public static Method	TileMultipart_createFromNBT;
		public static Method	TileMultipart_onChunkLoad;

		public static Class		MultipartHelper;
		public static Method	MultipartHelper_createTileFromNBT;
		public static Method	MultipartHelper_sendDescPacket;

		public static void Establish() {
			MultipartSaveLoad = ModInteraction.getClass("codechicken.multipart.handler.MultipartSaveLoad");

			MultipartSaveLoad_loadingWorld_$eq = getMethod(MultipartSaveLoad, "loadingWorld_$eq",
					net.minecraft.world.World.class);

			MultipartSPH = ModInteraction.getClass("codechicken.multipart.handler.MultipartSPH");

			MultipartSPH_onChunkWatch = getMethod(MultipartSPH, "onChunkWatch",
					net.minecraft.entity.player.EntityPlayer.class, net.minecraft.world.chunk.Chunk.class);

			TileMultipart = ModInteraction.getClass("codechicken.multipart.TileMultipart");

			TileMultipart_createFromNBT = getMethod(TileMultipart, "createFromNBT",
					net.minecraft.nbt.NBTTagCompound.class);

			TileMultipart_onChunkLoad = getMethod(TileMultipart, "onChunkLoad");

			MultipartHelper = ModInteraction.getClass("codechicken.multipart.MultipartHelper");

			MultipartHelper_createTileFromNBT = getMethod(MultipartHelper, "createTileFromNBT",
					net.minecraft.world.World.class, net.minecraft.nbt.NBTTagCompound.class);

			MultipartHelper_sendDescPacket = getMethod(MultipartHelper, "sendDescPacket",
					net.minecraft.world.World.class, net.minecraft.tileentity.TileEntity.class);
		}
	}

	public static Field		PendingBlockUpdateSetField;
	public static Method	RemovePendingBlockUpdate;

	public static boolean BCInstalled;
	public static boolean MPInstalled;

	public static void Establish() {
		Wrenches.init();

		Computers.setup();

		if (Computers.load) {
			Core.CarriageControllerEntity = TileEntityCarriageController.class;
		}

		ForgeMultipart.Establish();

		{
			PendingBlockUpdateSetField = getField(net.minecraft.world.WorldServer.class, "tickEntryQueue");

			RemovePendingBlockUpdate = getMethod(net.minecraft.world.WorldServer.class, "removeNextTickIfNeeded",
					net.minecraft.world.NextTickListEntry.class);
		}

		BCInstalled = Loader.isModLoaded("BuildCraft|Transport");
		MPInstalled = Loader.isModLoaded("ForgeMultipart");
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
			for (Class c : wrenchClasses) { // must iterate - testing
				// isAssignableFrom, not equals
				if (stk != null && stk.getItem() != null
						&& (c.isInstance(stk.getItem()) || isEitherWayAssignable(c, stk.getItem().getClass()))) { return true; }
			}
			return false;
		}

		public static boolean isEitherWayAssignable(Class a, Class b) {
			return a.isAssignableFrom(b) || b.isAssignableFrom(a);
		}

	}
}
