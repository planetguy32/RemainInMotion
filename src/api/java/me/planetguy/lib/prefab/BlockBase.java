package me.planetguy.lib.prefab;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockBase extends Block implements IPrefabBlock {

	public static String	modIDCache;

	public static void prepare(String modID) {
		modIDCache = modID;
	}

	@Deprecated
	public static IPrefabItem load(Class<? extends BlockBase> clazz, HashMap<String, IPrefabItem> map) {
		BlockBase block;
		try {
			block = clazz.newInstance();
			block.modID = modIDCache;
			GameRegistry.registerBlock(block, ItemBlockBase.class, block.getName());
			map.put(block.getName(), block);
			return block;
		} catch (InstantiationException e) { // Debug: Throw these exceptions
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private final String	name;

	public String			modID;

	protected BlockBase(Material p_i45394_1_, String name) {
		super(p_i45394_1_);
		this.name = name;
		setBlockName(name);
	}

	@Override
	public void registerBlockIcons(IIconRegister ir) {
		blockIcon = ir.registerIcon(modID + ":" + getName());
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

}
