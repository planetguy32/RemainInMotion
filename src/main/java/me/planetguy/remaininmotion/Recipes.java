package me.planetguy.remaininmotion ;

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
			SimpleItemSet . Types . CarriageCrosspiece . Stack ( ) ,

			"S S" , " S " , "S S" ,

			'S' , "stickWood"
		) ;

		Registry . RegisterShapedDictionaryRecipe
		(
			SimpleItemSet . Types . CarriagePanel . Stack ( ) ,

			"SSS" , "SCS" , "SSS" ,

			'S' , "stickWood" ,
			'C' , SimpleItemSet . Types . CarriageCrosspiece . Stack ( )
		) ;

		Registry . RegisterShapelessRecipe
		(
			SimpleItemSet . Types . CarriageFramework . Stack ( ) ,

			SimpleItemSet . Types . CarriagePanel . Stack ( ) ,
			SimpleItemSet . Types . CarriagePanel . Stack ( ) ,
			SimpleItemSet . Types . CarriagePanel . Stack ( ) ,
			SimpleItemSet . Types . CarriagePanel . Stack ( ) ,
			SimpleItemSet . Types . CarriagePanel . Stack ( ) ,
			SimpleItemSet . Types . CarriagePanel . Stack ( )
		) ;
	}

	public static void RegisterToolItemRecipes ( )
	{
		Registry . RegisterShapedRecipe
		(
			ToolItemSet . Types . Screwdriver . Stack ( ) ,

			" I " , " I " , "LSL" ,

			'I' , Stack . New ( (Item) net . minecraft . item . Item .itemRegistry.getObject("ingotIron")   ) ,
			'S' , Stack . New ( (Block) net . minecraft . block . Block.blockRegistry.getObject("cobblestone") ) ,
			'L' , Stack . New ( (Item) net . minecraft . item . Item.itemRegistry.getObject("leather")   )
		) ;
	}

	public static void RegisterCarriageRecipe ( Carriage . Types CarriageType , Vanilla . DyeTypes DyeType )
	{
		Registry . RegisterShapelessDictionaryRecipe
		(
			Stack . Resize ( CarriageItem . Stack ( CarriageType . ordinal ( ) , 0 ) , 8 ) ,

			SimpleItemSet . Types . CarriageFramework . Stack ( ) ,
			SimpleItemSet . Types . CarriageFramework . Stack ( ) ,
			SimpleItemSet . Types . CarriageFramework . Stack ( ) ,
			SimpleItemSet . Types . CarriageFramework . Stack ( ) ,
			SimpleItemSet . Types . CarriageFramework . Stack ( ) ,
			SimpleItemSet . Types . CarriageFramework . Stack ( ) ,
			SimpleItemSet . Types . CarriageFramework . Stack ( ) ,
			SimpleItemSet . Types . CarriageFramework . Stack ( ) ,

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
			'P' , Stack . New ( (Item) net . minecraft . item . Item.itemRegistry.getObject("enderPearl") ) ,
			'C' , Stack . New ( (Item) net . minecraft . item . Item.itemRegistry.getObject("compass") )
		) ;

		if ( ModInteraction . ComputerCraft . CarriageControllerEntity != null )
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
