package me.planetguy.remaininmotion.core;

import me.planetguy.remaininmotion.base.TileEntityRiM;
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

		/*
		 * IItemRenderer renderer=new IItemRenderer(){
		 * 
		 * @Override public boolean handleRenderType(ItemStack item,
		 * ItemRenderType type) { return true; }
		 * 
		 * @Override public boolean shouldUseRenderHelper(ItemRenderType type,
		 * ItemStack item, ItemRendererHelper helper) { return false; }
		 * 
		 * @Override public void renderItem(ItemRenderType t, ItemStack item,
		 * Object... data) { GL11.glPushMatrix(); if(t == ItemRenderType.ENTITY)
		 * GL11.glScaled(0.5, 0.5, 0.5); if(t == ItemRenderType.INVENTORY || t
		 * == ItemRenderType.ENTITY) GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		 * 
		 * IMicroMaterial material =
		 * MicroMaterialRegistry.getMaterial("planks"); MicroblockClass mcrClass
		 * = MicroblockClassRegistry.getMicroClass(item.getItemDamage());
		 * if(material==null || mcrClass == null) return;
		 * 
		 * CCRenderState.reset(); TextureUtils.bindAtlas(0);
		 * CCRenderState.useNormals(true); CCRenderState.useModelColours(true);
		 * CCRenderState.pullLightmap(); CCRenderState.startDrawing(7);
		 * FMPCarriage part=(FMPCarriage) Items.hollowCarriage.newPart(item,
		 * Minecraft.getMinecraft().thePlayer,
		 * Minecraft.getMinecraft().thePlayer.worldObj, null, 0, null);
		 * part.renderStatic(new Vector3(0.5, 0.5,
		 * 0.5).subtract(part.getBounds().center()), null, 0);
		 * CCRenderState.draw(); GL11.glPopMatrix(); }
		 * 
		 * };
		 * 
		 * MinecraftForgeClient.registerItemRenderer(Items.hollowCarriageId,
		 * renderer);
		 */
	}
}
