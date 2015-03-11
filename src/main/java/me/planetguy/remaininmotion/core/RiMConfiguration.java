package me.planetguy.remaininmotion.core;

import me.planetguy.lib.util.BlacklistDynamic;
import me.planetguy.remaininmotion.BlacklistManager;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;

public class RiMConfiguration extends Config {


    public abstract static class Carriage {
		public static int		MaxPlatformBurden	= 5000;

		public static int		MaxSupportBurden	= 5000;

		public static int		MaxTemplateBurden	= 5000;

		public static int		MaxMemoryBurden		= 5000;

		public static boolean	BlacklistBedrock	= true;

		public static boolean	BlacklistByPiston	= false;

	}

	public abstract static class CarriageDrive {
		public static int	ContinuousCooldown	= 5;
	}

	public abstract static class CarriageMotion {
		public static boolean	CapturePlayerEntities			= true;

		public static boolean	CaptureOtherLivingEntities		= true;

		public static boolean	CaptureItemEntities				= true;

		public static boolean	CaptureOtherEntities			= true;

		public static boolean	TeleportEntities				= true;

		public static boolean	RenderInFinalPositionDuringLag	= false;

		public static int		MotionDuration					= 20;

		public static int		TeleportationDuration			= 20 * 8;

		public static int		SoundIndex						= 0;
		public static String	SoundFile						= "hum";
		
		public static float volume=1.2f;

	}

	public abstract static class Cosmetic {
		public static boolean	ShowHelpInTooltips	= true;

		public static boolean	renderFallback		= false;

		public static int		maxTags				= -1;
	}

	public abstract static class DirtyHacks {
		public static boolean	UpdateBuildcraftPipes	= true;

		public static boolean	allowRotation			= true;

        public static boolean experimentalColor = true;
	}

	public abstract static class Debug {
		public static boolean	LogMotionExceptions		= false;

		public static boolean	MuteMotionExceptions	= true;

		public static boolean	verbose					= false;
	}

    public abstract static class HardMode {

        public static boolean HardmodeActive = false;

        public static double PowerConsumptionFactor = 10.0;

        public static int powerCapacity = 10000;

        public static boolean distanceAffectsEnergy = true;

        public static double otherDimensionMultiplier = 4;
        public static int peakDistance = 4000;
        public static double maxDistanceMultiplier = 15;
    }

	public enum TextureSets {
		JAKJ("256x textures", 256, 102, 99, 159, 155),

		cubemelon("default 16x textures by cubemelon", 16, 7, 7, 9, 9),

		JustHev("16x textures (with support for colourblindness) by JustHev", 16, 7, 5, 9, 7),

		uberifix_wood("16x wood-style textures by uberifix", 16, 7, 4, 9, 6),

		uberifix_metal("16x metal-style textures (with support for colourblindness) by uberifix", 16, 7, 4, 9, 6),

		Lethosos("16x Dynasty-style textures by Lethosos", 16, 7, 8, 9, 10);

		public String	Description;

		public double	LabelMinH;
		public double	LabelMinV;

		public double	LabelMaxH;
		public double	LabelMaxV;

		private TextureSets(String Description, double Resolution, double LabelMinH, double LabelMinV,
				double LabelMaxH, double LabelMaxV) {
			this.Description = Description;

			this.LabelMinH = LabelMinH / Resolution;
			this.LabelMinV = LabelMinV / Resolution;

			this.LabelMaxH = LabelMaxH / Resolution;
			this.LabelMaxV = LabelMaxV / Resolution;
		}

		public static int	TextureSet	= 1;
	}

	public RiMConfiguration(java.io.File File) {
		super(File);
	}

	public void Process() {

		{
			Category = "Carriage";

			{

				BlacklistManager.blacklistSoft = new BlacklistDynamic(Configuration, Configuration.get(Category,
						"blacklistSoft", ""), "RemIMBlacklistSoft");

				BlacklistManager.blacklistHard = new BlacklistDynamic(Configuration, Configuration.get(Category,
						"blacklistHard", ""), "RemIMBlacklistHard");

				BlacklistManager.blacklistRotation = new BlacklistDynamic(Configuration, Configuration.get(Category,
						"rotationBlacklist", ""), "RemIMBlacklistRotation");

			}

			CarriagePackage.MaxBlockCount = BoundedInteger("CarriageMaxSize",
					"Maximum size of any carriage (0 = no limit)", 0, CarriagePackage.MaxBlockCount, Integer.MAX_VALUE);

			Carriage.MaxPlatformBurden = BoundedInteger("PlatformCarriageMaxSize",
					"Maximum blocks carried by platform carriage", 1, Carriage.MaxPlatformBurden, Integer.MAX_VALUE);

			Carriage.MaxSupportBurden = BoundedInteger("SupportCarriageMaxSize",
					"Maximum blocks carried by a support carriage", 1, Carriage.MaxSupportBurden, Integer.MAX_VALUE);

			Carriage.MaxTemplateBurden = BoundedInteger("TemplateCarriageMaxSize",
					"Maximum blocks carried by a template carriage", 1, Carriage.MaxTemplateBurden, Integer.MAX_VALUE);

			Carriage.MaxMemoryBurden = BoundedInteger("MemoryCarriageMaxSize",
					"Maximum blocks carried by a memory carriage", 1, Carriage.MaxMemoryBurden, Integer.MAX_VALUE);

			Carriage.BlacklistBedrock = Boolean("BlacklistBedrock",
					"Carriages refuse to move bedrock (DANGEROUS IF FALSE)", Carriage.BlacklistBedrock);

			Carriage.BlacklistByPiston = Boolean("RespectPistonBlacklist",
					"Carriages refuse to move blocks that pistons cannot move", Carriage.BlacklistByPiston);
		}

		{
			Category = "Carriage Drive";

			CarriageDrive.ContinuousCooldown = BoundedInteger("ContinuousModeCooldown",
					"Cooldown (in ticks) between motions in continuous mode", 0, CarriageDrive.ContinuousCooldown,
					Integer.MAX_VALUE);
		}

		{
			Category = "Carriage Motion";

			CarriageMotion.CapturePlayerEntities = Boolean("MovePlayers", "Should grab players during motion",
					CarriageMotion.CapturePlayerEntities);

			CarriageMotion.CaptureOtherLivingEntities = Boolean("MoveNonPlayerLiving",
					"Should grab non-player living entities during motion", CarriageMotion.CaptureOtherLivingEntities);

			CarriageMotion.CaptureItemEntities = Boolean("MoveItemEnities", "Should grab floating items during motion",
					CarriageMotion.CaptureItemEntities);

			CarriageMotion.CaptureOtherEntities = Boolean("MoveAllOtherEntities",
					"Should grab all other miscellaneous entities during motion", CarriageMotion.CaptureOtherEntities);

			CarriageMotion.TeleportEntities = Boolean("TeleportEntities", "Should translocate grabbed entities",
					CarriageMotion.TeleportEntities);

			CarriagePackage.ObstructedByLiquids = Boolean("LiquidsBlockMovement",
					"Carriage motion/teleportation is obstructed by liquids", CarriagePackage.ObstructedByLiquids);

			CarriagePackage.ObstructedByFragileBlocks = Boolean("SoftTilesBlockMovement",
					"Carriage motion is obstructed by fragile blocks like tall grass",
					CarriagePackage.ObstructedByFragileBlocks);

			CarriageMotion.RenderInFinalPositionDuringLag = Boolean("StopAnimationDuringLag",
					"Animation of motion should stop during severe lag", CarriageMotion.RenderInFinalPositionDuringLag);

			CarriageMotion.MotionDuration = BoundedInteger("MotionDurationInTicks", "Duration of motion in ticks", 1,
					CarriageMotion.MotionDuration, Integer.MAX_VALUE);

			TileEntityMotiveSpectre.Velocity = 1 / ((double) CarriageMotion.MotionDuration);

			CarriageMotion.TeleportationDuration = BoundedInteger("TeleportDurationInTicks",
					"Duration of translocation in ticks", 1, CarriageMotion.TeleportationDuration, Integer.MAX_VALUE);

            // Allow for adding more sounds without bother people's configs.
			CarriageMotion.SoundIndex = BoundedInteger("Sound File Index",
					"Which Sounds to use. 0 is Default, 1-3 are sounds DA3DSOUL made, 10 is silence", 0, CarriageMotion.SoundIndex,
					10);
			switch (CarriageMotion.SoundIndex) {
				case 0:
					CarriageMotion.SoundFile = "hum";
					break;
				case 10:
					CarriageMotion.SoundFile = "mute";
					break;
				default:
					CarriageMotion.SoundFile = "engine_" + CarriageMotion.SoundIndex;
					break;
			}
			
			CarriageMotion.volume=Configuration.getFloat("Sound Volume", Category, CarriageMotion.volume, 0, 9001,"");
		}

		/*
		 * { Category = "Texture Sets";
		 * 
		 * int TextureSetCount = TextureSets.values().length;
		 * 
		 * TextureSets.TextureSet = BoundedInteger("Index of texture set", 0,
		 * TextureSets.TextureSet, TextureSetCount - 1);
		 * 
		 * Registry.TexturePrefix =
		 * TextureSets.values()[TextureSets.TextureSet].name() + "/";
		 * 
		 * String TextureSetDescriptions = "";
		 * 
		 * for (TextureSets TextureSet : TextureSets.values()) { if
		 * (TextureSet.ordinal() > 0) { TextureSetDescriptions += "\n"; }
		 * 
		 * TextureSetDescriptions += TextureSet.ordinal() + " - " +
		 * TextureSet.Description; }
		 * 
		 * Configuration.addCustomCategoryComment(Category,
		 * TextureSetDescriptions); }
		 */

		{
			Category = "Cosmetics";

			Cosmetic.ShowHelpInTooltips = Boolean("ExtendedTooltips",
					"Show descriptions of purposes/uses of blocks/items in tooltips", Cosmetic.ShowHelpInTooltips);

			Cosmetic.renderFallback = Boolean("FailsafeRendering",
					"Use the fallback renderer (try this if Minecraft closes instantly when moving carriages)",
					Cosmetic.renderFallback);

			Cosmetic.maxTags = Integer(
					"PacketMaxSize",
					"Limit on size of carriage to transmit, or -1 for no limit. Decrease if clients get errors like: Unexpected end of ZLIB input stream",
					Cosmetic.maxTags);
		}

		{
			Category = "Dirty Hacks";

			DirtyHacks.UpdateBuildcraftPipes = Boolean("ForceBCPipeUpdate",
					"Attempt to hyper-reinitialize Buildcraft pipes after motion", DirtyHacks.UpdateBuildcraftPipes);

			DirtyHacks.allowRotation = Boolean("EnableRotationCarriage", "Allow rotator carriage",
					DirtyHacks.allowRotation);

            DirtyHacks.experimentalColor = Boolean("EnableExperimentalColor", "Things like grass that have colorizers do 'interesting' things when used as camouflage. Who likes funny color glitches?",
                    DirtyHacks.experimentalColor);
		}

		{
			Category = "Debugging";

			Debug.LogMotionExceptions = Boolean("LogExceptions", "Write carriage-motion errors to client/server log",
					Debug.LogMotionExceptions);

			Debug.MuteMotionExceptions = Boolean("MuteAllMotionErrors", "Mute carriage-motion errors completely",
					Debug.MuteMotionExceptions);

			Debug.verbose = Boolean("VerboseLogging", "Log everything (will fill up your console/logs)", Debug.verbose);

		}

		{
			Category = "Hardmode";

			HardMode.HardmodeActive = Boolean("EnableHardmode", "Use RF to power carriages in addition to redstone",
                    HardMode.HardmodeActive);

            HardMode.PowerConsumptionFactor = Double("EnergyUseScalar", "Power consumption factor", HardMode.PowerConsumptionFactor);

            HardMode.powerCapacity = Integer("MaxEnergyStored", "Power capacity of carriages", HardMode.powerCapacity);

            HardMode.distanceAffectsEnergy = Boolean("distanceAffectsEnergy", "The distance a translocator moves affects the amount of energy required (very expensive)",
                    HardMode.distanceAffectsEnergy);

            HardMode.peakDistance = Integer("peakDistance", "Distance in blocks where peak multiplier is met", HardMode.peakDistance);

            HardMode.maxDistanceMultiplier = Double("maxDistanceMultiplier", "Peak distance energy multiplier", HardMode.maxDistanceMultiplier);

            HardMode.otherDimensionMultiplier = Double("otherDimensionMultiplier", "Additional multiplier for teleporting to another dimension (it stacks with distance)", HardMode.otherDimensionMultiplier);
		}

		Configuration.save();
	}
}
