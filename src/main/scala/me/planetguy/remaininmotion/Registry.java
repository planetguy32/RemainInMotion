package me.planetguy.remaininmotion ;

import me.planetguy.remaininmotion.core.Mod;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public abstract class Registry
{
	public static void RegisterCustomRecipe ( net . minecraft . item . crafting . IRecipe Recipe )
	{
		cpw . mods . fml . common . registry . GameRegistry . addRecipe ( Recipe ) ;
	}
	
	public static void registerRecipeClass(Class clazz){
		RecipeSorter.register(Mod.Handle+clazz.getSimpleName(), clazz, Category.SHAPELESS, "");
	}
	
	public static void registerClassRecipe(Class<? extends IRecipe> clazz){
		registerRecipeClass(clazz);
		try {
			RegisterCustomRecipe(clazz.newInstance());
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
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

	public static void RegisterEventHandler ( Object EventHandler )
	{
		net . minecraftforge . common . MinecraftForge . EVENT_BUS . register ( EventHandler ) ;
	}

	public static String TexturePrefix = "" ;

	public static net . minecraft . util . IIcon RegisterIcon ( net . minecraft . client . renderer . texture . IIconRegister IconRegister , String Handle )
	{
		return ( IconRegister . registerIcon ( Mod . Handle + ":" + TexturePrefix + Handle ) ) ;
	}
}
