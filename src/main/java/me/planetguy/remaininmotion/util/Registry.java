package me.planetguy.remaininmotion.util;

import me.planetguy.remaininmotion.core.ModRiM;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class Registry {
	public static void RegisterCustomRecipe(IRecipe Recipe) {
		GameRegistry.addRecipe(Recipe);
	}

	public static void registerRecipeClass(Class clazz) {
		RecipeSorter.register(ModRiM.Handle + clazz.getSimpleName(), clazz, Category.SHAPELESS, "");
	}

	public static void registerClassRecipe(Class<? extends IRecipe> clazz) {
		registerRecipeClass(clazz);
		try {
			RegisterCustomRecipe(clazz.newInstance());
		} catch (InstantiationException e) {} catch (IllegalAccessException e) {}
	}

	public static void RegisterShapelessRecipe(ItemStack Result, Object... Pattern) {
		GameRegistry.addShapelessRecipe(Result, Pattern);
	}

	public static void RegisterShapelessDictionaryRecipe(ItemStack Result, Object... Pattern) {
		RegisterCustomRecipe(new ShapelessOreRecipe(Result, Pattern));
	}

	public static void RegisterShapedRecipe(ItemStack Result, Object... Pattern) {
		GameRegistry.addShapedRecipe(Result, Pattern);
	}

	public static void RegisterShapedDictionaryRecipe(ItemStack Result, Object... Pattern) {
		RegisterCustomRecipe(new ShapedOreRecipe(Result, Pattern));
	}

	public static void RegisterEventHandler(Object EventHandler) {
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(EventHandler);
	}

	public static String	TexturePrefix	= "";

	public static IIcon RegisterIcon(IIconRegister IconRegister, String Handle) {
		return (IconRegister.registerIcon(ModRiM.Handle + ":" + TexturePrefix + Handle));
	}
}
