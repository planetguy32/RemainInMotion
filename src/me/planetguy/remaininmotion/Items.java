package me.planetguy.remaininmotion ;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class Items
{
	public static ToolItemSet ToolItemSet ;

	public static SimpleItemSet SimpleItemSet ;

	public static JItemMultiPart hollowCarriage;

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
			
		};
		
		LanguageRegistry.addName(hollowCarriage, "Hollow carriage");
		
		hollowCarriage.setCreativeTab(CreativeTab.Instance);
	}
}
