package me.planetguy.remaininmotion.crafting ;

import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.Stack;
import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.Types;
import me.planetguy.remaininmotion.Vanilla;
import me.planetguy.remaininmotion.carriage.Carriage;
import me.planetguy.remaininmotion.carriage.CarriageItem;
import me.planetguy.remaininmotion.core.Core;
import me.planetguy.remaininmotion.core.RiMItems;
import me.planetguy.remaininmotion.drive.CarriageDrive;
import me.planetguy.remaininmotion.drive.CarriageDriveItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class Recipes
{
	public static void Register ( )
	{
		RegisterSimpleItemRecipes ( ) ;

		RegisterToolItemRecipes ( ) ;

		RegisterCarriageRecipes ( ) ;

		RegisterCarriageDriveRecipes ( ) ;

		Registry . registerClassRecipe ( CarriageDecorationRecipe.class ) ;

		Registry . registerClassRecipe (CarriageTranslocatorLabelRecipe.class) ;

		Registry . registerClassRecipe (CarriageTranslocatorLabelConversionRecipe.class) ;
	}

	public static void RegisterSimpleItemRecipes ( )
	{
		Registry . RegisterShapedDictionaryRecipe
		(
			Types . CarriageCrosspiece . Stack ( ) ,

			"S S" , " S " , "S S" ,

			'S' , "stickWood"
		) ;

		Registry . RegisterShapedDictionaryRecipe
		(
			Types . CarriagePanel . Stack ( ) ,

			"SSS" , "SCS" , "SSS" ,

			'S' , "stickWood" ,
			'C' , Types . CarriageCrosspiece . Stack ( )
		) ;

		Registry . RegisterShapelessRecipe
		(
			Types . CarriageFramework . Stack ( ) ,

			Types . CarriagePanel . Stack ( ) ,
			Types . CarriagePanel . Stack ( ) ,
			Types . CarriagePanel . Stack ( ) ,
			Types . CarriagePanel . Stack ( ) ,
			Types . CarriagePanel . Stack ( ) ,
			Types . CarriagePanel . Stack ( )
		) ;
	}

	public static void RegisterToolItemRecipes ( )
	{
		Registry . RegisterShapedRecipe
		(
			ToolItemSet . Types . Screwdriver . Stack ( ) ,

			" I " , " I " , "LSL" ,

			'I' , Stack . New ( Items.iron_ingot ) ,
			'S' , Stack . New ( Blocks.cobblestone ) ,
			'L' , Stack . New ( Items.leather )
		) ;
	}

	public static void RegisterCarriageRecipe ( Carriage . Types CarriageType , Vanilla . DyeTypes DyeType )
	{
		Registry . RegisterShapelessDictionaryRecipe
		(
			Stack . Resize ( CarriageItem . Stack ( CarriageType . ordinal ( ) , 0 ) , 8 ) ,

			Types . CarriageFramework . Stack ( ) ,
			Types . CarriageFramework . Stack ( ) ,
			Types . CarriageFramework . Stack ( ) ,
			Types . CarriageFramework . Stack ( ) ,
			Types . CarriageFramework . Stack ( ) ,
			Types . CarriageFramework . Stack ( ) ,
			Types . CarriageFramework . Stack ( ) ,
			Types . CarriageFramework . Stack ( ) ,

			DyeType . Handle
		) ;
		
		
		//allow dyeing carriages one at a time - more convenient that way
		Registry . RegisterShapelessDictionaryRecipe
		(
			Stack . Resize ( CarriageItem . Stack ( CarriageType . ordinal ( ) , 0 ) , 8 ) ,

			Types . CarriageFramework . Stack ( ) ,

			DyeType . Handle
		) ;
		
	}

	public static void RegisterCarriageRecipes ( )
	{
		RegisterCarriageRecipe ( Carriage . Types . Frame , Vanilla . DyeTypes . Orange ) ;

		RegisterCarriageRecipe ( Carriage . Types .Platform , Vanilla . DyeTypes . Blue ) ;

		RegisterCarriageRecipe ( Carriage . Types . Structure , Vanilla . DyeTypes . Yellow ) ;

		RegisterCarriageRecipe ( Carriage . Types .Support , Vanilla . DyeTypes . Lime ) ;

		RegisterCarriageRecipe ( Carriage . Types . Template , Vanilla . DyeTypes . Purple ) ;
		
		//allow un-dyeing carriages
		GameRegistry.addShapelessRecipe(Types . CarriageFramework . Stack ( ) ,
				me.planetguy.remaininmotion.core.RIMBlocks.Carriage);
		
	}

	public static void RegisterCarriageDriveRecipe ( CarriageDrive . Types Type , Object ... BasePattern )
	{
		Registry . RegisterShapedRecipe ( CarriageDriveItem . Stack ( Type . ordinal ( ) , 0 ) , BasePattern ) ;
	}

	public static void RegisterCarriageDriveRecipes ( )
	{
		RegisterCarriageDriveRecipe
		(
			CarriageDrive . Types . Motor ,

			"RFR" , "FIF" , "RFR" ,

			'R' , Stack . New ( Blocks.redstone_block ) ,
			'F' , Stack . New ( Blocks.furnace) ,
			'I' , Stack . New ( Blocks.iron_block )
		) ;

		RegisterCarriageDriveRecipe
		(
			CarriageDrive . Types . Engine ,

			"TTT" , "TMT" , "TTT" ,

			'M' , CarriageDriveItem . Stack ( CarriageDrive . Types . Motor . ordinal ( ) , 0 ) ,
			'T' , Stack . New ( Blocks.redstone_torch )
		) ;

		RegisterCarriageDriveRecipe
		(
			CarriageDrive . Types . Translocator ,

			"OOO" , "OEO" , "PCP" ,

			'E' , CarriageDriveItem . Stack ( CarriageDrive . Types . Engine . ordinal ( ) , 0 ) ,
			'O' , Stack . New ( Blocks.obsidian ) ,
			'P' , Stack . New ( Items.ender_pearl ) ,
			'C' , Stack . New ( Items.compass )
		) ;

		if ( Core . CarriageControllerEntity != null )
		{
			RegisterCarriageDriveRecipe
			(
				CarriageDrive . Types . Controller ,

				"RRR" , "RMR" , "RRR" ,

				'R' , Stack . New ( Items.repeater ) ,
				'M' , CarriageDriveItem . Stack ( CarriageDrive . Types . Motor . ordinal ( ) , 0 )
			) ;
		}
		
		RegisterCarriageDriveRecipe(
				CarriageDrive.Types.Adapter,
				"MF",
				'M',CarriageDriveItem.Stack(CarriageDrive.Types.Motor.ordinal(), 0),
				'F',Stack.New(RiMItems.SimpleItemSet, 2));
	}
}
