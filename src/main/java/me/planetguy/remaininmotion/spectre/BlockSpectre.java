package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSpectre extends BlockRiM {
	public BlockSpectre() {
		super(Blocks.bedrock, ItemSpectre.class, TileEntityMotiveSpectre.class, null, TileEntityTeleportativeSpectre.class, TileEntityTransduplicativeSpectre.class, TileEntityRotativeSpectre.class);
		RenderId = -1;
	}

	public enum Types {
		Motive, Supportive, Teleportative, Transduplicative, Rotative;
	}

	@Override
	public boolean onBlockActivated(World World, int X, int Y, int Z, EntityPlayer Player, int Side, float HitX,
			float HitY, float HitZ) {
		if (World.isRemote) { return (false); }

		if (World.getBlockMetadata(X, Y, Z) != Types.Supportive.ordinal()) { return (false); }

		if (!ToolItemSet.IsScrewdriverOrEquivalent(Player.inventory.getCurrentItem())) { return (false); }

		WorldUtil.ClearBlock(World, X, Y, Z);

		return (true);
	}

	@Override
	public IIcon getIcon(int a, int b) {
		return Blocks.planks.getIcon(0, 0);
	}

	@Override
	public boolean isOpaqueCube() {
		// System.out.println("Render fallback (IOC): "+Configuration.Cosmetic.renderFallback);
		return (RiMConfiguration.Cosmetic.renderFallback);
	}

	@Override
	public boolean renderAsNormalBlock() {
		// System.out.println("Render fallback (RANB): "+Configuration.Cosmetic.renderFallback);
		return (RiMConfiguration.Cosmetic.renderFallback);
	}
}
