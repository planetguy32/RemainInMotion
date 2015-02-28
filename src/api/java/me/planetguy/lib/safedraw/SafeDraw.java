package me.planetguy.lib.safedraw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import me.planetguy.lib.util.Reflection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

//Mess of copypasta
//TODO test this code
public class SafeDraw {

	public static void init() {
		//run protectors on separate threads?
		Thread[] t1=new Thread[] {
				new Thread( new Runnable() {public void run() { protectISBRHs();}}),
				new Thread( new Runnable() {public void run() { protectEntityRenderers();}}),
				new Thread( new Runnable() {public void run() { protectTESRs();}}),
		};
		for(Thread t:t1)
			t.start();
		for(Thread t:t1)
			try {
				t.join();
			} catch (InterruptedException e) {
			}
	}

	public static void protectISBRHs() {
		IdentityHashMap<ISimpleBlockRenderingHandler, ISimpleBlockRenderingHandler> cachedISBRHs=new IdentityHashMap<ISimpleBlockRenderingHandler, ISimpleBlockRenderingHandler>();
		Map<Integer, ISimpleBlockRenderingHandler> blockRenderers = (Map<Integer, ISimpleBlockRenderingHandler>) Reflection.get(RenderingRegistry.class, RenderingRegistry.instance(), "blockRenderers");
		for(Integer i:blockRenderers.keySet()) {
			final ISimpleBlockRenderingHandler handler=blockRenderers.get(i);
			if(!cachedISBRHs.containsKey(handler)) {
				cachedISBRHs.put(handler, new ISimpleBlockRenderingHandler() {

					//delegate that catches all exceptions, to avoid client crashes
					@Override
					public void renderInventoryBlock(Block block, int metadata,
							int modelId, RenderBlocks renderer) {
						try {
							handler.renderInventoryBlock(block, metadata, modelId, renderer);
						}catch(Throwable ignored) {}
					}

					@Override
					public boolean renderWorldBlock(IBlockAccess world, int x,
							int y, int z, Block block, int modelId,
							RenderBlocks renderer) {
						try {
							return handler.renderWorldBlock(world, x, y, z, block, modelId, renderer);
						}catch(Throwable ignored) {}
						return false;
					}

					@Override
					public boolean shouldRender3DInInventory(int modelId) {
						try {
							return handler.shouldRender3DInInventory(modelId);
						}catch(Throwable ignored) {}
						return false;
					}

					@Override
					public int getRenderId() {
						try {
							return handler.getRenderId();
						}catch(Throwable ignored) {}
						return 0;
					}

				});
			}
			blockRenderers.remove(i);
			blockRenderers.put(i, cachedISBRHs.get(handler));
		}
	}

	public static void protectEntityRenderers() {
		IdentityHashMap<Render, Render> cache=new IdentityHashMap<Render, Render>();
		Map<Class<? extends Entity>, Render> map=(Map<Class<? extends Entity>, Render>)RenderManager.instance.entityRenderMap;
		try{
			final Method m=Render.class.getDeclaredMethod("getEntityTexture", Entity.class);
			m.setAccessible(true);

			for(Class<? extends Entity> eClass:map.keySet()) {
				if(!cache.containsKey(map.get(eClass))) {
					final Render r=map.get(eClass);

					cache.put(r, new Render() {

						@Override
						public void doRender(Entity p_76986_1_, double p_76986_2_,
								double p_76986_4_, double p_76986_6_,
								float p_76986_8_, float p_76986_9_) {
							r.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
						}

						@Override
						protected ResourceLocation getEntityTexture(
								Entity e) {
							try {
								return (ResourceLocation) m.invoke(r, e);
							} catch (Exception ex) {
								return new ResourceLocation("planetguyLib:invalid");
							}
						}

					});
				}
				map.remove(eClass);
				map.put(eClass, cache.get(eClass));
			}
		}catch(Exception e) {}
	}
	
	public static void protectTESRs() {
		IdentityHashMap<TileEntitySpecialRenderer, TileEntitySpecialRenderer> cache=new IdentityHashMap<TileEntitySpecialRenderer, TileEntitySpecialRenderer>();
		Map<Class, TileEntitySpecialRenderer> map=TileEntityRendererDispatcher.instance.mapSpecialRenderers;
		for(Class c:map.keySet()) {
			final TileEntitySpecialRenderer tesr=map.get(c);
			if(!cache.containsKey(tesr)) {
				cache.put(tesr, new TileEntitySpecialRenderer() {

					@Override
					public void renderTileEntityAt(TileEntity p_147500_1_,
							double p_147500_2_, double p_147500_4_,
							double p_147500_6_, float p_147500_8_) {
						tesr.renderTileEntityAt(p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
					}
					
					@Override
				    public void func_147497_a(TileEntityRendererDispatcher p_147497_1_)
				    {
				        tesr.func_147497_a(p_147497_1_);
				    }

					@Override
				    public void func_147496_a(World p_147496_1_) {
				    	tesr.func_147496_a(p_147496_1_);
				    }
				    
					@Override
				    public FontRenderer func_147498_b()
				    {
				        return tesr.func_147498_b();
				    }

				});
			}
			map.remove(c);
			map.put(c, cache.get(tesr));
		}
	}
	
}
