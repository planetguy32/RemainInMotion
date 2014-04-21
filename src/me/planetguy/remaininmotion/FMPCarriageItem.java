package me.planetguy.remaininmotion;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;

final class FMPCarriageItem extends JItemMultiPart {
	FMPCarriageItem(int id) {
		super(id);
	}

	@Override
	public TMultiPart newPart(ItemStack arg0, EntityPlayer arg1,
			World arg2, BlockCoord arg3, int arg4, Vector3 arg5) {
		return MultiPartRegistry.createPart("FMPCarriage", false);
	}

	@Override
	public String getUnlocalizedName(){
		return Mod.Handle+":"+"FMPCarriage";
	}

	public String getItemDisplayName(ItemStack stack){
		return "Multipart Carriage";
	}

	public Icon getIconFromDamage(int dmg){
		return Items.SimpleItemSet.getIconFromDamage(2);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List l, boolean par4) {
		if ( Configuration . Cosmetic . ShowHelpInTooltips )return;
		l . add ( "Carries blocks directly touching it." ) ;
		l.add("Supports Forge Multipart.");
	}
}