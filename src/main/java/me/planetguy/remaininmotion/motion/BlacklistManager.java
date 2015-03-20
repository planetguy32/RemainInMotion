package me.planetguy.remaininmotion.motion;

import me.planetguy.lib.util.Blacklist;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BlacklistManager {

	public static Blacklist	blacklistHard;
	public static Blacklist	blacklistSoft;
	public static Blacklist	blacklistRotation;

	public static void Initialize() {
		blacklistSoft.blacklist(RIMBlocks.Spectre);

		blacklistSoft.blacklist(Blocks.air);

		if (RiMConfiguration.Carriage.BlacklistBedrock) {
			blacklistSoft.blacklist(Blocks.bedrock);
		}

		if (RiMConfiguration.Carriage.BlacklistByPiston) {
			blacklistSoft.blacklist(Blocks.obsidian);

			for (Object objBlock : Block.blockRegistry) {
				Block block = (Block) objBlock;

				if (block == null) {
					continue;
				}

				try {
					if (block.getBlockHardness(null, 0, 0, 0) < 0) {
						blacklistSoft.blacklist(block);

						continue;
					}
				} catch (NullPointerException npe) {
					blacklistSoft.blacklist(block);
				}

				if (block.getMobilityFlag() == 2) {
					blacklistSoft.blacklist(block);

					continue;
				}
			}
		}
	}

	public static boolean lookup(Blacklist bl, BlockRecord record) {
		return bl.lookup(record.World, record.X, record.Y, record.Z);
	}

}
