package me.planetguy.remaininmotion ;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.TextureUtils;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.MicroMaterialRegistry.IMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.MicroblockClass;
import codechicken.microblock.MicroblockClassRegistry;
import codechicken.microblock.MicroblockClient;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Items
{
	public static ToolItemSet ToolItemSet ;

	public static SimpleItemSet SimpleItemSet ;

	public static net.minecraft.item.Item hollowCarriage;

	public static int hollowCarriageId;

	public static void Initialize ( )
	{
		ToolItemSet = new ToolItemSet ( ) ;

		SimpleItemSet = new SimpleItemSet ( ) ;

		hollowCarriage=new JItemMultiPart(hollowCarriageId){

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
				return SimpleItemSet.getIconFromDamage(2);
			}
			
		    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List l, boolean par4) {
				if ( Configuration . Cosmetic . ShowHelpInTooltips )return;
				l . add ( "Carries blocks directly touching it." ) ;
				l.add("Supports Forge Multipart.");
			}

		};

		LanguageRegistry.addName(hollowCarriage, "Hollow carriage");

		hollowCarriage.setCreativeTab(CreativeTab.Instance);

	}
}
