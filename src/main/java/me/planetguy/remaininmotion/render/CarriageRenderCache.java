package me.planetguy.remaininmotion.render;

import java.util.TreeMap;

import me.planetguy.lib.util.Reflection;
import me.planetguy.remaininmotion.BlockPosition;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.BlockRecordSet;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public abstract class CarriageRenderCache {
	public static TreeMap<BlockPosition, RenderRecord>	Cache	= new TreeMap<BlockPosition, RenderRecord>();

	public static void Render(RenderBlocks blockRenderer, BlockRecordSet Blocks, BlockRecordSet TileEntities, int Pass) {

		blockRenderer.renderAllFaces = true;
		RenderHelper.disableStandardItemLighting();

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
					blockRenderer.renderBlockByRenderType(Record.block, Record.X, Record.Y, Record.Z);
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

				if ((Boolean) Reflection.get(net.minecraft.client.renderer.Tessellator.class, Tessellator.instance,
						"isDrawing")) {
					try {
						Tessellator.instance.draw();
					} catch (Throwable Throwable) {
						Throwable.printStackTrace();
					}
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
