package me.planetguy.remaininmotion ;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public abstract class Registry
{
	public static void RegisterCustomRecipe ( net . minecraft . item . crafting . IRecipe Recipe )
	{
		cpw . mods . fml . common . registry . GameRegistry . addRecipe ( Recipe ) ;
	}

	public static void RegisterShapelessRecipe ( net . minecraft . item . ItemStack Result , Object ... Pattern )
	{
		cpw . mods . fml . common . registry . GameRegistry . addShapelessRecipe ( Result , Pattern ) ;
	}

	public static void RegisterShapelessDictionaryRecipe ( net . minecraft . item . ItemStack Result , Object ... Pattern )
	{
		RegisterCustomRecipe ( new net . minecraftforge . oredict . ShapelessOreRecipe ( Result , Pattern ) ) ;
	}

	public static void RegisterShapedRecipe ( net . minecraft . item . ItemStack Result , Object ... Pattern )
	{
		cpw . mods . fml . common . registry . GameRegistry . addShapedRecipe ( Result , Pattern ) ;
	}

	public static void RegisterShapedDictionaryRecipe ( net . minecraft . item . ItemStack Result , Object ... Pattern )
	{
		RegisterCustomRecipe ( new net . minecraftforge . oredict . ShapedOreRecipe ( Result , Pattern ) ) ;
	}

	public static void RegisterSmeltingRecipe ( net . minecraft . item . ItemStack Output , net . minecraft . item . ItemStack Input )
	{
		RegisterSmeltingRecipe ( Output , Input , 0 ) ;
	}

	public static void RegisterSmeltingRecipe ( net . minecraft . item . ItemStack Output , net . minecraft . item . ItemStack Input , float Xp )
	{
		net . minecraft . item . crafting . FurnaceRecipes . smelting ( ) . func_151394_a ( Input, Output , Xp ) ;
	}

	public static void RegisterEventHandler ( Object EventHandler )
	{
		net . minecraftforge . common . MinecraftForge . EVENT_BUS . register ( EventHandler ) ;
	}

	public static String TexturePrefix = "" ;

	public static IIcon RegisterIcon ( IIconRegister IconRegister , String Handle )
	{
		return ( IconRegister . registerIcon ( Mod . Handle + ":" + TexturePrefix + Handle ) ) ;
	}
}
