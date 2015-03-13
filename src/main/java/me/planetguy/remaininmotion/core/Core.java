package me.planetguy.remaininmotion.core;

import me.planetguy.remaininmotion.motion.BlacklistManager;
import me.planetguy.remaininmotion.motion.NativeCarriageMatcher;
import me.planetguy.remaininmotion.api.FrameCarriageMatcher;
import me.planetguy.remaininmotion.api.ICloseable;
import me.planetguy.remaininmotion.api.ICloseableFactory;
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

        // TODO Move this somewhere it makes sense. I'm just sick of it crashing.
        RiMRegistry.registerCloseableFactory(new ICloseableFactory() {
            @Override
            public ICloseable retrieve(TileEntity te) {
                return (ICloseable) te;
            }

            @Override
            public Class validClass() {
                return TileEntityFrameCarriage.class;
            }
        });
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
