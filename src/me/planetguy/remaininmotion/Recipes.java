package me.planetguy.remaininmotion ;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class Recipes
{
	public static void Register ( )
	{
		RegisterSimpleItemRecipes ( ) ;

		RegisterToolItemRecipes ( ) ;

		RegisterCarriageRecipes ( ) ;

		RegisterCarriageDriveRecipes ( ) ;

		Registry . RegisterCustomRecipe ( new CarriageDecorationRecipe ( ) ) ;

		Registry . RegisterCustomRecipe ( new CarriageTranslocatorLabelRecipe ( ) ) ;

		Registry . RegisterCustomRecipe ( new CarriageDecorationConversionRecipe ( ) ) ;

		Registry . RegisterCustomRecipe ( new CarriageTranslocatorLabelConversionRecipe ( ) ) ;
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

			'I' , Stack . New ( net . minecraft . item . Item . ingotIron ) ,
			'S' , Stack . New ( net . minecraft . block . Block . cobblestone ) ,
			'L' , Stack . New ( net . minecraft . item . Item . leather )
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
		
		
		//allow dyeing carriages one at a time
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

		RegisterCarriageRecipe ( Carriage . Types . Platform , Vanilla . DyeTypes . Blue ) ;

		RegisterCarriageRecipe ( Carriage . Types . Structure , Vanilla . DyeTypes . Yellow ) ;

		RegisterCarriageRecipe ( Carriage . Types . Support , Vanilla . DyeTypes . Lime ) ;

		RegisterCarriageRecipe ( Carriage . Types . Template , Vanilla . DyeTypes . Purple ) ;
		
		//allow un-dyeing carriages
		GameRegistry.addShapelessRecipe(Types . CarriageFramework . Stack ( ) ,
				Blocks.Carriage);
		
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

			'R' , Stack . New ( net . minecraft . block . Block . blockRedstone ) ,
			'F' , Stack . New ( net . minecraft . block . Block . furnaceIdle ) ,
			'I' , Stack . New ( net . minecraft . block . Block . blockIron )
		) ;

		RegisterCarriageDriveRecipe
		(
			CarriageDrive . Types . Engine ,

			"TTT" , "TMT" , "TTT" ,

			'M' , CarriageDriveItem . Stack ( CarriageDrive . Types . Motor . ordinal ( ) , 0 ) ,
			'T' , Stack . New ( net . minecraft . block . Block . torchRedstoneActive )
		) ;

		RegisterCarriageDriveRecipe
		(
			CarriageDrive . Types . Translocator ,

			"OOO" , "OEO" , "PCP" ,

			'E' , CarriageDriveItem . Stack ( CarriageDrive . Types . Engine . ordinal ( ) , 0 ) ,
			'O' , Stack . New ( net . minecraft . block . Block . obsidian ) ,
			'P' , Stack . New ( net . minecraft . item . Item . enderPearl ) ,
			'C' , Stack . New ( net . minecraft . item . Item . compass )
		) ;

		if ( ModInteraction . ComputerCraft . CarriageControllerEntity != null )
		{
			RegisterCarriageDriveRecipe
			(
				CarriageDrive . Types . Controller ,

				"RRR" , "RMR" , "RRR" ,

				'R' , Stack . New ( net . minecraft . item . Item . redstoneRepeater ) ,
				'M' , CarriageDriveItem . Stack ( CarriageDrive . Types . Motor . ordinal ( ) , 0 )
			) ;
		}
	}
}
