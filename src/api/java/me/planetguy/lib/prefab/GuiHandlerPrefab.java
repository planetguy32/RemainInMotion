package me.planetguy.lib.prefab;

import java.lang.reflect.Constructor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandlerPrefab implements IGuiHandler {

	public static void create(Object owner, Class<ContainerPrefab>[] containers, Class<GuiPrefab>[] clientGuis) {
		try{
			NetworkRegistry.INSTANCE.registerGuiHandler(owner, new GuiHandlerPrefab(containers, clientGuis));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	private final Constructor[] commonConstructors;
	private final Constructor[] clientConstructors;
	
	private GuiHandlerPrefab(Class[] containers, Class[] guis) throws Exception{
		commonConstructors=new Constructor[containers.length];
		for(int i=0; i<containers.length; i++){
			commonConstructors[i]=containers[i].getConstructor(InventoryPlayer.class, TileEntity.class);
		}
		clientConstructors=new Constructor[containers.length];
		for(int i=0; i<guis.length; i++){
			clientConstructors[i]=guis[i].getConstructor(InventoryPlayer.class, TileEntity.class);
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		try{
			return commonConstructors[ID].newInstance(player.inventory, world.getTileEntity(x, y, z));
		}catch(Exception e){
			
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		try{
			return clientConstructors[ID].newInstance(player.inventory, world.getTileEntity(x, y, z));
		}catch(Exception e){
			
		}
		return null;
	}

}
