package me.planetguy.remaininmotion.crafting;

import me.planetguy.remaininmotion.util.Registry;
import me.planetguy.remaininmotion.util.Stack;
import me.planetguy.remaininmotion.base.ToolItemSet;
import me.planetguy.remaininmotion.core.ItemTypes;
import me.planetguy.remaininmotion.util.Vanilla;
import me.planetguy.remaininmotion.carriage.BlockCarriage;
import me.planetguy.remaininmotion.carriage.ItemCarriage;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMItems;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive;
import me.planetguy.remaininmotion.drive.ItemCarriageDrive;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class Recipes {
	public static void Register() {
		RegisterSimpleItemRecipes();

		RegisterToolItemRecipes();

		RegisterCarriageRecipes();

		RegisterCarriageDriveRecipes();

		Registry.registerClassRecipe(CarriageDecorationRecipe.class);

		Registry.registerClassRecipe(CarriageTranslocatorLabelRecipe.class);

		Registry.registerClassRecipe(CarriageTranslocatorLabelConversionRecipe.class);
	}

	public static void RegisterSimpleItemRecipes() {
		Registry.RegisterShapedDictionaryRecipe(ItemTypes.CarriageCrosspiece.Stack(),

		"S S", " S ", "S S",

		'S', "stickWood");

		Registry.RegisterShapedDictionaryRecipe(ItemTypes.CarriagePanel.Stack(),

		"SSS", "SCS", "SSS",

		'S', "stickWood", 'C', ItemTypes.CarriageCrosspiece.Stack());

		Registry.RegisterShapelessRecipe(ItemTypes.CarriageFramework.Stack(),

		ItemTypes.CarriagePanel.Stack(), ItemTypes.CarriagePanel.Stack(), ItemTypes.CarriagePanel.Stack(),
				ItemTypes.CarriagePanel.Stack(), ItemTypes.CarriagePanel.Stack(), ItemTypes.CarriagePanel.Stack());
	}

	public static void RegisterToolItemRecipes() {
		Registry.RegisterShapedRecipe(ToolItemSet.Types.Screwdriver.Stack(),

		" I ", " I ", "LSL",

		'I', Stack.New(Items.iron_ingot), 'S', Stack.New(Blocks.cobblestone), 'L', Stack.New(Items.leather));
	}

	public static void RegisterCarriageRecipe(BlockCarriage.Types CarriageType, Vanilla.DyeTypes DyeType) {
		
		
		ItemStack output;
		if(CarriageType == BlockCarriage.Types.Frame)
			output=new ItemStack(RIMBlocks.plainFrame, 8);
		else
			output=Stack.Resize(ItemCarriage.Stack(CarriageType.ordinal()), 8);
		
		
		Registry.RegisterShapelessDictionaryRecipe(
				output,

		ItemTypes.CarriageFramework.Stack(), ItemTypes.CarriageFramework.Stack(), ItemTypes.CarriageFramework.Stack(),
				ItemTypes.CarriageFramework.Stack(), ItemTypes.CarriageFramework.Stack(), ItemTypes.CarriageFramework.Stack(),
				ItemTypes.CarriageFramework.Stack(), ItemTypes.CarriageFramework.Stack(),

				DyeType.Handle);

		// allow dyeing carriages one at a time - more convenient that way
		Registry.RegisterShapelessDictionaryRecipe(Stack.Resize(ItemCarriage.Stack(CarriageType.ordinal()), 1),

		ItemTypes.CarriageFramework.Stack(),

		DyeType.Handle);

	}

	public static void RegisterCarriageRecipes() {
		RegisterCarriageRecipe(BlockCarriage.Types.Frame, Vanilla.DyeTypes.Orange);

		RegisterCarriageRecipe(BlockCarriage.Types.Platform, Vanilla.DyeTypes.Blue);

		RegisterCarriageRecipe(BlockCarriage.Types.Structure, Vanilla.DyeTypes.Yellow);

		RegisterCarriageRecipe(BlockCarriage.Types.Support, Vanilla.DyeTypes.Lime);

		RegisterCarriageRecipe(BlockCarriage.Types.Template, Vanilla.DyeTypes.Purple);

		// allow un-dyeing carriages
		GameRegistry.addShapelessRecipe(ItemTypes.CarriageFramework.Stack(),
				new ItemStack(RIMBlocks.Carriage));

	}

	public static void RegisterCarriageDriveRecipe(BlockCarriageDrive.Types Type, Object... BasePattern) {
		Registry.RegisterShapedRecipe(ItemCarriageDrive.Stack(Type.ordinal(), 0), BasePattern);
	}

	public static void RegisterCarriageDriveRecipes() {
		RegisterCarriageDriveRecipe(BlockCarriageDrive.Types.Motor,

		"RFR", "FIF", "RFR",

		'R', Stack.New(Blocks.redstone_block), 'F', Stack.New(Blocks.furnace), 'I', Stack.New(Blocks.iron_block));

		/* Toggle this in the GUI
		RegisterCarriageDriveRecipe(BlockCarriageDrive.Types.Engine,

		"TTT", "TMT", "TTT",

		'M', ItemCarriageDrive.Stack(BlockCarriageDrive.Types.Motor.ordinal(), 0), 'T',
				Stack.New(Blocks.redstone_torch));
		*/
		
		RegisterCarriageDriveRecipe(BlockCarriageDrive.Types.Translocator,

		"OOO", "OEO", "PCP",

		'E', ItemCarriageDrive.Stack(BlockCarriageDrive.Types.Engine.ordinal(), 0), 'O', Stack.New(Blocks.obsidian),
				'P', Stack.New(Items.ender_pearl), 'C', Stack.New(Items.compass));

		if (ModRiM.CarriageControllerEntity != null) {
			RegisterCarriageDriveRecipe(BlockCarriageDrive.Types.Controller,

			"RRR", "RMR", "RRR",

			'R', Stack.New(Items.repeater), 'M', ItemCarriageDrive.Stack(BlockCarriageDrive.Types.Motor.ordinal(), 0));
		}

		RegisterCarriageDriveRecipe(BlockCarriageDrive.Types.Adapter, "MF", 'M',
				ItemCarriageDrive.Stack(BlockCarriageDrive.Types.Motor.ordinal(), 0), 'F',
				Stack.New(RiMItems.SimpleItemSet, 2));
		
		RegisterCarriageDriveRecipe(BlockCarriageDrive.Types.Predirected,
				"ttt",
				"tdl",
				"ttt",
				'd', ItemCarriageDrive.Stack(BlockCarriageDrive.Types.Motor.ordinal(), 0),
				't', Stack.New(Blocks.redstone_torch),
				'l', Stack.New(Blocks.lever));
	}
}
