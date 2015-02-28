package me.planetguy.remaininmotion.core;

import java.io.File;

import net.minecraft.block.Block;
import me.planetguy.lib.PLHelper;
import me.planetguy.remaininmotion.BlacklistManager;
import me.planetguy.remaininmotion.plugins.RemIMPluginsCommon;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = ModRiM.Handle, name = ModRiM.Title, version = ModRiM.Version, dependencies = "required-after:planetguyLib;after:CoFHCore;after:BuildCraft|Transport")
public class ModRiM {
	public static final String	Namespace	= "me.planetguy.remaininmotion.";

	public static final String	Handle		= "JAKJ_RedstoneInMotion";

	public static final String	Title		= "Remain In Motion";

	public static final String	Version		= "2.4.0";

	public static final String	Channel		= "JAKJ_RIM";

	public static PLHelper		plHelper	= new PLHelper(Handle);

	@EventHandler
	public void PreInit(FMLPreInitializationEvent Event) {
		File oldFile = Event.getSuggestedConfigurationFile();
		File newFile = new File(oldFile.getParentFile().getAbsoluteFile() + File.separator + "remainInMotion.cfg");
		if (oldFile.exists()) {
			oldFile.renameTo(newFile);
		}
		(new RiMConfiguration(newFile)).Process();

		Core.HandlePreInit();
		
		RemIMPluginsCommon.instance.preInit();
	}

	@EventHandler
	public void Init(FMLInitializationEvent Event) {

        Core.HandleInit();
		RemIMPluginsCommon.instance.init();
	}

	@EventHandler
	public void PostInit(FMLPostInitializationEvent Event) {
		ClientSetupProxy.Instance.Execute();

		Core.HandlePostInit();
		RemIMPluginsCommon.instance.postInit();
	}

	@EventHandler
	public void ServerStopping(FMLServerStoppingEvent Event) {
		Core.HandleServerStopping();
	}

	@EventHandler
	public void handleIMCMessage(FMLInterModComms.IMCEvent event) {
		for (final FMLInterModComms.IMCMessage message : event.getMessages()) {
			if (message.key.equals("blacklistHard")) {
				String block = message.getStringValue();
				try {
					BlacklistManager.blacklistHard.blacklist(Block.getBlockFromName(block));

				} catch (NumberFormatException e) {
					System.err.println("Recieved bad blacklist request from " + message.getSender() + ": " + block);
				}
			}else if (message.key.equals("blacklistSoft")) {
				String block = message.getStringValue();
				try {
					BlacklistManager.blacklistSoft.blacklist(Block.getBlockFromName(block));

				} catch (NumberFormatException e) {
					System.err.println("Recieved bad blacklist request from " + message.getSender() + ": " + block);
				}
			}else if (message.key.equals("blacklistRotation")) {
				String block = message.getStringValue();
				try {
					BlacklistManager.blacklistRotation.blacklist(Block.getBlockFromName(block));

				} catch (NumberFormatException e) {
					System.err.println("Recieved bad blacklist request from " + message.getSender() + ": " + block);
				}
			}
		}
	}
}
