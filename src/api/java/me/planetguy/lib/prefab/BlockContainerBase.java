package me.planetguy.lib.prefab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockContainerBase extends BlockContainer implements IPrefabBlock {

	@Deprecated
	public static IPrefabItem load(Class<? extends BlockContainerBase> clazz, HashMap<String, IPrefabItem> map) {
		BlockContainerBase block;
		try {
			block = clazz.newInstance();
			block.modid = BlockBase.modIDCache;
			for (Class<? extends TileEntity> c : block.teClasses) {
				GameRegistry.registerTileEntity(c, block.modid + ".te." + c.getSimpleName());
			}
			GameRegistry.registerBlock(block, ItemBlockBase.class, block.getName());
			map.put(block.getName(), block);
			return block;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private final String	name;

	public String			modid;

	public Class[]			teClasses;

	public BlockContainerBase(Material p_i45394_1_, String name, Class<? extends TileEntity>... tileEntities) {
		super(p_i45394_1_);
		this.name = name;
		setBlockName(name);
		teClasses = tileEntities;
	}

	@Override
	public void registerBlockIcons(IIconRegister ir) {
		blockIcon = ir.registerIcon(modid + ":" + getName());
	}

	@Override
	public int countTooltipLines() {
		return 2;
	}

	@Override
	public void loadCrafting() {}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		try {
			return (TileEntity) teClasses[meta].newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player){
    	return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
    }
    
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
    	ArrayList<ItemStack> stacks= new ArrayList<ItemStack>();
    	stacks.add(new ItemStack(this, 1, metadata));
    	return stacks;
    }
    
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{	
		for (int var4 = 0; var4 < teClasses.length; ++var4)
		{
			par3List.add(new ItemStack(this, 1, var4));
		}
	}

	
}
