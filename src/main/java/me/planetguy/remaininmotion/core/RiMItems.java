package me.planetguy.remaininmotion.core;

import me.planetguy.remaininmotion.base.SimpleItemSet;
import me.planetguy.remaininmotion.base.ToolItemSet;

public abstract class RiMItems {
	public static ToolItemSet	ToolItemSet;

	public static SimpleItemSet	SimpleItemSet;

	public static void Initialize() {
		ToolItemSet = new ToolItemSet();

		SimpleItemSet = new SimpleItemSet();

	}
}
