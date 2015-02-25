package me.planetguy.lib.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import cpw.mods.fml.relauncher.IFMLCallHook;

public class ClientServerPackLauncher implements IFMLCallHook {

	String location;
	
	@Override
	public Void call() throws Exception {
		try {
			File liConf=seekConfigFolder(new File(location));
			String text=IOUtils.toString(new FileInputStream(liConf));
			text.split("\n");
		}catch(Exception e) {
			System.out.println("Failure in launchInto coremod! Not pre-starting server!");
		}
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		location=(String) data.get("coremodLocation");
	}
	
	public File seekConfigFolder(File start) {
		File f=start.getParentFile();
		for(File dir:f.listFiles()) {
			if(f.getName().equals("config")) {
				return new File(dir.getAbsolutePath()+File.pathSeparator+"planetguyLib"+File.pathSeparator+"launchInto.txt");
			}
		}
		return seekConfigFolder(f);
	}

}
