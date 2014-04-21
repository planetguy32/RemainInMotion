package me.planetguy.remaininmotion.core ;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import me.planetguy.remaininmotion.Carriage;
import me.planetguy.remaininmotion.CarriageDecorationConversionRecipe;
import me.planetguy.remaininmotion.CarriageDecorationRecipe;
import me.planetguy.remaininmotion.CarriageDrive;
import me.planetguy.remaininmotion.CarriageDriveItem;
import me.planetguy.remaininmotion.CarriageItem;
import me.planetguy.remaininmotion.CarriageTranslocatorLabelConversionRecipe;
import me.planetguy.remaininmotion.CarriageTranslocatorLabelRecipe;
import me.planetguy.remaininmotion.Registry;
import me.planetguy.remaininmotion.SimpleItemSet;
import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.ToolItemSet.Types;
import me.planetguy.remaininmotion.base.Stack;
import me.planetguy.remaininmotion.core.ModInteraction.ComputerCraft;
import me.planetguy.remaininmotion.util.Vanilla;
import me.planetguy.remaininmotion.util.Vanilla.DyeTypes;

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

		/*
		Registry . RegisterCustomRecipe ( new CarriageDecorationRecipe ( ) ) ;

		Registry . RegisterCustomRecipe ( new CarriageTranslocatorLabelRecipe ( ) ) ;

		Registry . RegisterCustomRecipe ( new CarriageDecorationConversionRecipe ( ) ) ;

		Registry . RegisterCustomRecipe ( new CarriageTranslocatorLabelConversionRecipe ( ) ) ;
		*/
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

			'I' , Stack . New ( (Item) net . minecraft . item . Item .itemRegistry.getObject("ingotIron")   ) ,
			'S' , Stack . New ( (net.minecraft.block.Block) net . minecraft . block . Block.blockRegistry.getObjectById(4) ) ,
			'L' , Stack . New ( (net.minecraft.item.Item) net . minecraft . item . Item.itemRegistry.getObject("leather")   )
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
		
		GameRegistry.addRecipe(new ItemStack(Items.hollowCarriage, 8), 
				"ccc", 
				"c c",
				"ccc",
				Character.valueOf('c'), new ItemStack(Blocks.Carriage, 1, 0));
		
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

			'R' , Stack . New ( (Block) net . minecraft . block . Block .blockRegistry.getObject("blockRedstone") ) ,
			'F' , Stack . New ( (Block) net . minecraft . block . Block.blockRegistry.getObject("furnaceIdle") ) ,
			'I' , Stack . New ( (Block) net . minecraft . block . Block.blockRegistry.getObject("blockIron") )
		) ;

		RegisterCarriageDriveRecipe
		(
			CarriageDrive . Types . Engine ,

			"TTT" , "TMT" , "TTT" ,

			'M' , CarriageDriveItem . Stack ( CarriageDrive . Types . Motor . ordinal ( ) , 0 ) ,
			'T' , Stack . New ( (Block) net . minecraft . block . Block . blockRegistry.getObject("torchRedstoneActive") )
		) ;

		RegisterCarriageDriveRecipe
		(
			CarriageDrive . Types . Translocator ,

			"OOO" , "OEO" , "PCP" ,

			'E' , CarriageDriveItem . Stack ( CarriageDrive . Types . Engine . ordinal ( ) , 0 ) ,
			'O' , Stack . New ( (Block) net . minecraft . block .Block.blockRegistry.getObject("obsidian") ) ,
			'P' , Stack . New ( (Item) net . minecraft . item . Item.getItemById(368) ) ,
			'C' , Stack . New ( (Item) net . minecraft . item . Item.itemRegistry.getObjectById(345) )
		) ;

		if ( ModInteraction . ComputerCraft .carriageControllerEntity != null )
		{
			RegisterCarriageDriveRecipe
			(
				CarriageDrive . Types . Controller ,

				"RRR" , "RMR" , "RRR" ,

				'R' , Stack . New ( (Item) net . minecraft . item . Item.itemRegistry.getObject("redstoneRepeater") ) ,
				'M' , CarriageDriveItem . Stack ( CarriageDrive . Types . Motor . ordinal ( ) , 0 )
			) ;
		}
	}
}
