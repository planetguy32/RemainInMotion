package me.planetguy.remaininmotion.core;

import me.planetguy.remaininmotion.carriage.BlockCarriage;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive;
import me.planetguy.remaininmotion.spectre.BlockSpectre;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public abstract class RIMBlocks {
	public static BlockCarriage			Carriage;

	public static BlockCarriageDrive	CarriageDrive;

	public static BlockSpectre				Spectre;

	public static Block					air	= Blocks.air;

	public static void Initialize() {
		Carriage = new BlockCarriage();

		CarriageDrive = new BlockCarriageDrive();

		Spectre = new BlockSpectre();

	}
}
