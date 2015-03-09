package me.planetguy.remaininmotion.plugins.buildcraft;

import net.minecraft.item.ItemStack;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.plugins.RemIMPluginsCommon;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public class BCFacadePlugin {

	public static void tryLoad() {
		if (Loader.isModLoaded("BuildCraft|Transport")) {

			try {
				//reflection check if IFacadePluggable exists - if this throws, our
				//hook is missing so don't even bother loading
                //Loader.isModLoaded checks that BC is installed, but we still need this in case people have an old version
				Class.forName("buildcraft.api.transport.pluggable.IFacadePluggable");

				Debug.dbg("Buildcraft special facade: loading");

				RiMRegistry.registerFrameCarriageMatcher(new SpecialFacadeCarriageMatcher());

				RiMRegistry.registerCloseableFactory(new SpecialFacadeCloseableFactory());

				FMLInterModComms.sendMessage("BuildCraft|Transport", "add-facade", 
						new ItemStack(RemIMPluginsCommon.getFrameBlock()));

			} catch (Exception e) {

				Debug.dbg("Buildcraft special facade: not loading, missing API component");

			}

		} else {
			Debug.dbg("Buildcraft special facade: not loading, no BC");
		}
	}

}
