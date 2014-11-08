package me.planetguy.remaininmotion.core ;

import me.planetguy.lib.util.Blacklist;
import me.planetguy.remaininmotion.BlacklistManager;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.MotiveSpectreEntity;
import me.planetguy.remaininmotion.Registry;
import net.minecraft.block.Block;


public class Configuration extends Config
{
	public abstract static class Carriage
	{
		public static int MaxPlatformBurden = 5000 ;

		public static int MaxSupportBurden = 5000 ;

		public static boolean BlacklistBedrock = true ;

		public static boolean BlacklistByPiston = false ;
		
	}

	public abstract static class CarriageDrive
	{
		public static int ContinuousCooldown = 5 ;
	}

	public abstract static class CarriageMotion
	{
		public static boolean CapturePlayerEntities = true ;

		public static boolean CaptureOtherLivingEntities = true ;

		public static boolean CaptureItemEntities = true ;

		public static boolean CaptureOtherEntities = true ;

		public static boolean TeleportEntities = true ;

		public static boolean RenderInFinalPositionDuringLag = false ;

		public static int MotionDuration = 20 ;

		public static int TeleportationDuration = 20 * 8 ;
		
	}

	public abstract static class Cosmetic
	{
		public static boolean ShowHelpInTooltips = true ;
		
		public static boolean renderFallback=false;
	}

	public abstract static class DirtyHacks
	{
		public static boolean UpdateBuildcraftPipes = true ;
	}

	public abstract static class Debug
	{
		public static boolean LogMotionExceptions = false ;
		
		public static boolean MuteMotionExceptions=true;
		
		public static boolean verbose=false;
	}

	public static boolean HardmodeActive = false ;

	public static double PowerConsumptionFactor=1.0;
	
	public static int powerCapacity=10000;
	
	public enum TextureSets
	{
		JAKJ
		(
			"default 256x textures" ,
			256 ,
			102 , 99 , 159 , 155
		) ,

		cubemelon
		(
			"16x textures by cubemelon" ,
			16 ,
			7 , 7 , 9 , 9
		) ,

		JustHev
		(
			"16x textures (with support for colourblindness) by JustHev" ,
			16 ,
			7 , 5 , 9 , 7
		) ,

		uberifix_wood
		(
			"16x wood-style textures by uberifix" ,
			16 ,
			7 , 4 , 9 , 6
		) ,

		uberifix_metal
		(
			"16x metal-style textures (with support for colourblindness) by uberifix" ,
			16 ,
			7 , 4 , 9 , 6
		) ,

		Lethosos
		(
			"16x Dynasty-style textures by Lethosos" ,
			16 ,
			7 , 8 , 9 , 10
		) ;

		public String Description ;

		public double LabelMinH ;
		public double LabelMinV ;

		public double LabelMaxH ;
		public double LabelMaxV ;

		private TextureSets ( String Description , double Resolution , double LabelMinH , double LabelMinV , double LabelMaxH , double LabelMaxV )
		{
			this . Description = Description ;

			this . LabelMinH = LabelMinH / Resolution ;
			this . LabelMinV = LabelMinV / Resolution ;

			this . LabelMaxH = LabelMaxH / Resolution ;
			this . LabelMaxV = LabelMaxV / Resolution ;
		}

		public static int TextureSet ;
	}

	public Configuration ( java . io . File File )
	{
		super ( File ) ;
	}
	
	private void setupBlacklist(String bl, Blacklist blacklist){
		String Blacklist = String ( bl , "" ) ;

		if ( ! Blacklist . equals ( "" ) )
		{
			for ( String BlacklistItem : Blacklist . split ( "," ) )
			{
				String [ ] BlacklistItemElements = BlacklistItem . split ( ":" ) ;

				try
				{
					if ( BlacklistItemElements . length == 1 )
					{
						blacklist . blacklist ( Block.getBlockFromName(BlacklistItemElements[0]) ) ;

						continue ;
					}

					if ( BlacklistItemElements . length == 2 )
					{
						blacklist . blacklist ( Block.getBlockFromName(BlacklistItemElements[0]) , Integer . parseInt ( BlacklistItemElements [ 1 ] ) ) ;

						continue ;
					}
				}
				catch ( Throwable Throwable )
				{
					Throwable . printStackTrace ( ) ;
				}

				new RuntimeException ( "Invalid blacklist item: " + BlacklistItem ) . printStackTrace ( ) ;
			}
		}
	}

	public void Process ( )
	{

		{
			Category = "Carriage" ;

			{
				setupBlacklist("blacklistSoft", BlacklistManager.blacklistSoft);
				
				setupBlacklist("blacklistHard", BlacklistManager.blacklistHard);
			}

			CarriagePackage . MaxBlockCount = BoundedInteger ( "Maximum size of any carriage (0 = no limit)" , 0 , CarriagePackage . MaxBlockCount , Integer . MAX_VALUE ) ;

			Carriage . MaxPlatformBurden = BoundedInteger ( "Maximum blocks carried by platform carriage" , 1 , Carriage . MaxPlatformBurden , Integer . MAX_VALUE ) ;

			Carriage . MaxSupportBurden = BoundedInteger ( "Maximum blocks carried by a support carriage" , 1 , Carriage . MaxSupportBurden , Integer . MAX_VALUE ) ;

			Carriage . BlacklistBedrock = Boolean ( "Carriages refuse to move bedrock (DANGEROUS IF FALSE)" , Carriage . BlacklistBedrock ) ;

			Carriage . BlacklistByPiston = Boolean ( "Carriages refuse to move blocks that pistons cannot move" , Carriage . BlacklistByPiston ) ;
		}

		{
			Category = "Carriage Drive" ;

			CarriageDrive . ContinuousCooldown = BoundedInteger ( "Cooldown (in ticks) between motions in continuous mode" , 0 , CarriageDrive . ContinuousCooldown , Integer . MAX_VALUE ) ;
		}

		{
			Category = "Carriage Motion" ;

			CarriageMotion . CapturePlayerEntities = Boolean ( "Should grab players during motion" , CarriageMotion . CapturePlayerEntities ) ;

			CarriageMotion . CaptureOtherLivingEntities = Boolean ( "Should grab non-player living entities during motion" , CarriageMotion . CaptureOtherLivingEntities ) ;

			CarriageMotion . CaptureItemEntities = Boolean ( "Should grab floating items during motion" , CarriageMotion . CaptureItemEntities ) ;

			CarriageMotion . CaptureOtherEntities = Boolean ( "Should grab all other miscellaneous entities during motion" , CarriageMotion . CaptureOtherEntities ) ;

			CarriageMotion . TeleportEntities = Boolean ( "Should translocate grabbed entities" , CarriageMotion . TeleportEntities ) ;

			CarriagePackage . ObstructedByLiquids = Boolean ( "Carriage motion is obstructed by liquids" , CarriagePackage . ObstructedByLiquids ) ;

			CarriagePackage . ObstructedByFragileBlocks = Boolean ( "Carriage motion is obstructed by fragile blocks like tall grass" , CarriagePackage . ObstructedByFragileBlocks ) ;

			CarriageMotion . RenderInFinalPositionDuringLag = Boolean ( "Animation of motion should stop even during severe lag" , CarriageMotion . RenderInFinalPositionDuringLag ) ;

			CarriageMotion . MotionDuration = BoundedInteger ( "Duration of motion in ticks" , 10 , CarriageMotion . MotionDuration , Integer . MAX_VALUE ) ;

			MotiveSpectreEntity . Velocity = 1 / ( ( double ) CarriageMotion . MotionDuration ) ;

			CarriageMotion . TeleportationDuration = BoundedInteger ( "Duration of translocation in ticks" , 10 , CarriageMotion . TeleportationDuration , Integer . MAX_VALUE ) ;
		}

		{
			Category = "Texture Sets" ;

			int TextureSetCount = TextureSets . values ( ) . length ;

			TextureSets . TextureSet = BoundedInteger ( "Index of texture set" , 0 , TextureSets . TextureSet , TextureSetCount - 1 ) ;

			Registry . TexturePrefix = TextureSets . values ( ) [ TextureSets . TextureSet ] . name ( ) + "/" ;

			String TextureSetDescriptions = "" ;

			for ( TextureSets TextureSet : TextureSets . values ( ) )
			{
				if ( TextureSet . ordinal ( ) > 0 )
				{
					TextureSetDescriptions += "\n" ;
				}

				TextureSetDescriptions += TextureSet . ordinal ( ) + " - " + TextureSet . Description ;
			}

			Configuration . addCustomCategoryComment ( Category , TextureSetDescriptions ) ;
		}

		{
			Category = "Cosmetics" ;

			Cosmetic . ShowHelpInTooltips = Boolean ( "Show descriptions of purposes/uses of blocks/items in tooltips" , Cosmetic . ShowHelpInTooltips ) ;
			
			Cosmetic.renderFallback = Boolean ( "Use the fallback renderer (try this if Minecraft closes instantly when moving carriages)" , Cosmetic.renderFallback ) ;
		}

		{
			Category = "Dirty Hacks" ;

			DirtyHacks . UpdateBuildcraftPipes = Boolean ( "Attempt to hyper-reinitialize Buildcraft pipes after motion" , DirtyHacks . UpdateBuildcraftPipes ) ;
		}

		{
			Category = "Debugging" ;

			Debug . LogMotionExceptions = Boolean ( "Write carriage-motion errors to client/server log" , Debug . LogMotionExceptions ) ;
			
			Debug.MuteMotionExceptions = Boolean ( "Mute carriage-motion errors completely" , Debug.MuteMotionExceptions);
			
			Debug.verbose = Boolean ( "Log everything (will fill up your console/logs)" , Debug.verbose);

		}

		{
			Category = "Hardmode" ;

			HardmodeActive = Boolean ( "Hardmode is activated" , HardmodeActive ) ;
			
			PowerConsumptionFactor=Configuration.get(Category, "Power consumption factor", PowerConsumptionFactor).getDouble(PowerConsumptionFactor);
			
			powerCapacity=Configuration.get(Category, "Power capacity of carriages", powerCapacity).getInt(powerCapacity);
		}

		Configuration . save ( ) ;
	}
}
