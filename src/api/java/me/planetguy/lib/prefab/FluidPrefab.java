package me.planetguy.lib.prefab;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class FluidPrefab extends Fluid implements IPrefabItem{
	
	public String namespace;

	public ItemBucket bucket;
	
	public BlockFluidBase block;
	
	public FluidPrefab(String name){
		super(name);
	}
	
	public void setupOtherTypes(){
		block=makeBlock();
		registerBlock(block);
		bucket=makeBucket();
		registerBucket(bucket);
		BucketHandler.register(block, bucket);
	}
	
	public BlockFluidBase makeBlock(){
		return new BlockFluidFinite(this, Material.water);
	}
	
	public void registerBlock(BlockFluidBase block){
		String blockName=unlocalizedName+"_block";
		block.setBlockName(namespace+":"+blockName);
		GameRegistry.registerBlock(block, blockName);
	}
	
	public ItemBucket makeBucket(){
		return new ItemBucket(block);
	}
	
	public void registerBucket(ItemBucket bucket){
		bucket.setUnlocalizedName(unlocalizedName+"_bucket");
		GameRegistry.registerItem(bucket, unlocalizedName+"_bucket");
		FluidContainerRegistry.registerFluidContainer(this, new ItemStack(bucket), new ItemStack(Items.bucket));
	}

	@Override
	public void loadCrafting() {
	}

	@Override
	public Object setCreativeTab(CreativeTabs tab) {
		bucket.setCreativeTab(tab);
		return this;
	}
	
	public static class BucketHandler {
		
		public static boolean initialized=false;
		
		public static void register(Block fluid, Item bucket){
			if(!initialized)
				MinecraftForge.EVENT_BUS.register(INSTANCE);
			initialized=true;
			INSTANCE.buckets.put(fluid, bucket);
		}
		
        public static BucketHandler INSTANCE = new BucketHandler();
        public Map<Block, Item> buckets = new HashMap<Block, Item>();

        private BucketHandler() {
        }

        @SubscribeEvent
        public void onBucketFill(FillBucketEvent event) {

                ItemStack result = fillCustomBucket(event.world, event.target);

                if (result == null)
                        return;

                event.result = result;
                event.setResult(Result.ALLOW);
        }

        private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) {

                Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);

                Item bucket = buckets.get(block);
                if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) {
                        world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
                        return new ItemStack(bucket);
                } else
                        return null;

        }
}

}
