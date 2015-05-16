package me.planetguy.remaininmotion.core;

import java.util.HashMap;

import me.planetguy.lib.prefab.BlockBase;
import me.planetguy.lib.prefab.BlockContainerBase;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.carriage.BlockCarriage;
import me.planetguy.remaininmotion.carriage.BlockSimpleFrame;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive;
import me.planetguy.remaininmotion.spectre.BlockRailSpecter;
import me.planetguy.remaininmotion.spectre.BlockSpectre;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public abstract class RIMBlocks {
	public static BlockCarriage			Carriage;

	public static BlockCarriageDrive	CarriageDrive;

	public static BlockSpectre			Spectre;
	public static BlockRailSpecter		RailSpectre;

	public static Block					air	= Blocks.air;
	
	public static Block plainFrame;

	public static void Initialize() {
		BlockRiM.initLegacyClassMap();

		plainFrame=(Block) ModRiM.plHelper.loadContainer(BlockSimpleFrame.class, new HashMap());
		
		Carriage = new BlockCarriage();

		CarriageDrive = new BlockCarriageDrive();

		Spectre = new BlockSpectre();

		RailSpectre = new BlockRailSpecter();
	}
}
