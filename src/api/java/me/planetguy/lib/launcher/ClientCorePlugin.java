package me.planetguy.lib.launcher;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class ClientCorePlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return "me.planetguy.lib.launcher.ClientServerPackLauncher";
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
