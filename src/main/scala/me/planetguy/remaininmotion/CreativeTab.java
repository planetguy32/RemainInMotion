package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.core.Mod;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTab extends net . minecraft . creativetab . CreativeTabs
{
	public CreativeTab ( )
	{
		super ( Mod . Handle ) ;
	}

	public static CreativeTab Instance ;

	public static void Prepare ( )
	{
		Instance = new CreativeTab ( ) ;
	}

	@Override
	public String getTranslatedTabLabel ( )
	{
		return ( Mod . Title ) ;
	}

	public Item TabIconItemIndex ;

	public static void Initialize ( Item TabIconItemIndex )
	{
		Instance . TabIconItemIndex = TabIconItemIndex ;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return TabIconItemIndex;
	}
}
