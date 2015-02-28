package me.planetguy.remaininmotion.core;

import cpw.mods.fml.common.Loader;
import me.planetguy.remaininmotion.BlacklistManager;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.NativeCarriageMatcher;
import me.planetguy.remaininmotion.api.FrameCarriageMatcher;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.carriage.TileEntityFrameCarriage;
import me.planetguy.remaininmotion.core.interop.ModInteraction;
import me.planetguy.remaininmotion.crafting.Recipes;
import me.planetguy.remaininmotion.drive.TileEntityCarriageTranslocator;
import me.planetguy.remaininmotion.network.PacketManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

public abstract class Core {
	
	public static void HandlePreInit() {
        ModInteraction.Establish();

        CreativeTab.Prepare();

        RIMBlocks.Initialize();

        RiMItems.Initialize();

        CreativeTab.Initialize(Item.getItemFromBlock(RIMBlocks.Carriage));
    }

	public static void HandleInit() {


		RiMRegistry.registerFrameCarriageMatcher(new FrameCarriageMatcher() {
			@Override
			public boolean isFrameCarriage(Block block1, int meta1, TileEntity entity1) {
				return entity1 instanceof TileEntityFrameCarriage;
			}
		});

		RiMRegistry.registerMatcher(new NativeCarriageMatcher());

		PacketManager.init();


    }

	public static void HandlePostInit() {
		Recipes.Register();

		BlacklistManager.Initialize();
	}

	public static void HandleServerStopping() {
		try {
			TileEntityCarriageTranslocator.ActiveTranslocatorSets.clear();
		} catch (Error e) {
			// e.printStackTrace();
		}
	}

	public static Class	CarriageControllerEntity;

}
