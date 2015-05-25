package me.planetguy.remaininmotion.core;

import me.planetguy.remaininmotion.base.TileEntityRiM;
import me.planetguy.remaininmotion.drive.gui.GuiDirectional;
import me.planetguy.remaininmotion.drive.gui.GuiDriveCommon;
import me.planetguy.remaininmotion.drive.gui.GuiTranslocator;
import me.planetguy.remaininmotion.render.CarriageDriveRenderer;
import me.planetguy.remaininmotion.render.CarriageRenderer;
import me.planetguy.remaininmotion.render.MotiveSpectreRenderer;
import me.planetguy.remaininmotion.render.RIMTileEntityRenderer;
import me.planetguy.remaininmotion.render.RotativeSpectreRenderer;
import me.planetguy.remaininmotion.render.TeleportativeSpectreRenderer;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import me.planetguy.remaininmotion.spectre.TileEntityRotativeSpectre;
import me.planetguy.remaininmotion.spectre.TileEntityTeleportativeSpectre;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientSetup extends ClientSetupProxy {
	public void RegisterTileEntityRenderer(RIMTileEntityRenderer Renderer,
			Class<? extends TileEntityRiM>... EntityClasses) {
		for (Class<? extends TileEntityRiM> EntityClass : EntityClasses) {
			ClientRegistry.bindTileEntitySpecialRenderer(EntityClass, Renderer);
		}
	}

	@Override
	public void Execute() {
		if (!FMLCommonHandler.instance().getSide().isClient()) { return; }

		if (!RiMConfiguration.Cosmetic.renderFallback) {
			RegisterTileEntityRenderer(new MotiveSpectreRenderer(), TileEntityMotiveSpectre.class);

			RegisterTileEntityRenderer(new TeleportativeSpectreRenderer(), TileEntityTeleportativeSpectre.class);

			RegisterTileEntityRenderer(new RotativeSpectreRenderer(), TileEntityRotativeSpectre.class);

			new CarriageRenderer();

			new CarriageDriveRenderer();
		}
	}
	
	public Class[] clientClasses(){
		return new Class[]{
				GuiDriveCommon.class,
				GuiTranslocator.class,
				GuiDirectional.class,
		};
	}
	
}
