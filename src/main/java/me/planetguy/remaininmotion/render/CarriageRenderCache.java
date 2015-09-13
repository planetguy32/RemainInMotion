package me.planetguy.remaininmotion.render;

import java.util.TreeMap;

import cpw.mods.fml.client.FMLClientHandler;
import me.planetguy.lib.util.Reflection;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.api.event.PostRenderDuringMovementEvent;
import me.planetguy.remaininmotion.api.event.PreRenderDuringMovementEvent;
import me.planetguy.remaininmotion.core.interop.EventPool;
import me.planetguy.remaininmotion.util.position.BlockPosition;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.position.BlockRecordSet;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

public abstract class CarriageRenderCache {
	public static TreeMap<BlockPosition, RenderRecord>	Cache	= new TreeMap<BlockPosition, RenderRecord>();

	public static void Render(RenderBlocks blockRenderer, BlockRecordSet Blocks, BlockRecordSet TileEntities, int Pass) {

		blockRenderer.renderAllFaces = true;
		RenderHelper.disableStandardItemLighting();

		if (Pass != 0) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}

		GL11.glEnable(GL11.GL_CULL_FACE);

		if (FMLClientHandler.instance().getClient().isAmbientOcclusionEnabled()) {
			GL11.glShadeModel(GL11.GL_SMOOTH);
		} else {
			GL11.glShadeModel(GL11.GL_FLAT);
		}

		{
			Render.ResetBoundTexture();

			Render.BindBlockTexture();

			Render.PushMatrix();

			Tessellator.instance.startDrawingQuads();

			for (BlockRecord Record : Blocks) {
				try {
					if (!Record.block.canRenderInPass(Pass)) {
						continue;
					}
				} catch (Throwable Throwable) {
					Throwable.printStackTrace();

					continue;
				}

				try {
					if(!EventPool.postPreRenderDuringMovementEvent(blockRenderer, Record.X, Record.Y, Record.Z, Record.entity, Pass))
						blockRenderer.renderBlockByRenderType(Record.block, Record.X, Record.Y, Record.Z);
					EventPool.postPostRenderDuringMovementEvent(blockRenderer, Record.X, Record.Y, Record.Z, Record.entity, Pass);
				} catch (Throwable Throwable) {
					Throwable.printStackTrace();
				}
			}

			try {
				Tessellator.instance.draw();
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}

			Render.PopMatrix();
		}

		RenderHelper.enableStandardItemLighting();

		{
			Render.PushMatrix();

			Render.Translate(TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerY,
					TileEntityRendererDispatcher.staticPlayerZ);

			for (BlockRecord Record : TileEntities) {
				if (!Record.entity.shouldRenderInPass(Pass)) {
					continue;
				}

				Render.ResetBoundTexture();

				Render.PushMatrix();

				try {
					TileEntityRendererDispatcher.instance.renderTileEntity(Record.entity, 0);
				} catch (Throwable Throwable) {
					Throwable.printStackTrace();
				}

				try {
					Tessellator.instance.draw();
				} catch (IllegalStateException ignored) {
				} catch (Throwable t){
					t.printStackTrace();
				}

				Render.PopMatrix();
			}

			Render.PopMatrix();

			try {
				Tessellator.instance.draw();
			} catch (Throwable Throwable) {}

			Render.ResetBoundTexture();
		}
		blockRenderer.renderAllFaces = false;
	}

	public static void Assemble(BlockRecordSet Blocks, BlockRecordSet TileEntities, World World, BlockPosition Key) {
		if (Cache.containsKey(Key)) { return; }

		RenderBlocks blockRenderer = new RenderBlocks(World);

		RenderRecord renderRecord = new RenderRecord();

		renderRecord.PrimaryPassDisplayList = Render.InitializeDisplayList();

		Render(blockRenderer, Blocks, TileEntities, 0);

		Render.FinalizeDisplayList();

		renderRecord.SecondaryPassDisplayList = Render.InitializeDisplayList();

		Render(blockRenderer, Blocks, TileEntities, 1);

		Render.FinalizeDisplayList();

		Cache.put(Key, renderRecord);
	}

	public static Integer lookupDisplayList(BlockPosition Key) {
		int Pass = MinecraftForgeClient.getRenderPass();

		RenderRecord renderRecord = Cache.get(Key);

		if (renderRecord == null) { return (null); }

		if (Pass == 0) { return (renderRecord.PrimaryPassDisplayList); }

		if (Pass == 1) { return (renderRecord.SecondaryPassDisplayList); }

		return (null);
	}

	public static void Release(BlockPosition Key) {
		if (Key == null) { return; }

		RenderRecord RenderRecord = Cache.remove(Key);

		if (RenderRecord != null) {
			Render.ReleaseDisplayList(RenderRecord.PrimaryPassDisplayList);

			Render.ReleaseDisplayList(RenderRecord.SecondaryPassDisplayList);
		}
	}
}
