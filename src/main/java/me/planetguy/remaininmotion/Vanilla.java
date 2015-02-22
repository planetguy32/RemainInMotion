package me.planetguy.remaininmotion;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public abstract class Vanilla {
	public enum CoalTypes {
		Coal, Charcoal;

		public ItemStack Stack() {
			return (Stack.New(Items.coal, this));
		}
	}

	public enum DyeTypes {
		Black("dyeBlack"), Red("dyeRed"), Green("dyeGreen"), Brown("dyeBrown"), Blue("dyeBlue"), Purple("dyePurple"), Cyan(
				"dyeCyan"), LightGrey("dyeLightGray"), Grey("dyeGray"), Pink("dyePink"), Lime("dyeLime"), Yellow(
				"dyeYellow"), LightBlue("dyeLightBlue"), Magenta("dyeMagenta"), Orange("dyeOrange"), White("dyeWhite");

		public String	Handle;

		private DyeTypes(String Handle) {
			this.Handle = Handle;
		}

		public ItemStack Stack() {
			return (Stack.New(Items.dye, this));
		}
	}
}
