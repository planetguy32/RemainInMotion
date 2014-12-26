package me.planetguy.remaininmotion.fmp;

import java.util.List;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.core.Mod;
import me.planetguy.remaininmotion.core.RiMItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;

final class ItemCarriageFMP extends JItemMultiPart {
	ItemCarriageFMP() {
		super();
	}

	@Override
	public TMultiPart newPart(ItemStack arg0, EntityPlayer arg1, World arg2, BlockCoord arg3, int arg4, Vector3 arg5) {
		return MultiPartRegistry.createPart("FMPCarriage", false);
	}

	@Override
	public String getUnlocalizedName() {
		return Mod.Handle + ":" + "FMPCarriage";
	}

	@Override
	public IIcon getIconFromDamage(int dmg) {
		return RiMItems.SimpleItemSet.getIconFromDamage(2);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List l, boolean par4) {
		if (Configuration.Cosmetic.ShowHelpInTooltips) { return; }
		for (String s : Lang.translate(this.getUnlocalizedName() + ".tooltip").split("\\n")) {
			l.add(s);
		}
	}
}