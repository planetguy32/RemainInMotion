package me.planetguy.remaininmotion.core;

import me.planetguy.lib.PLHelper;
import me.planetguy.remaininmotion.base.SimpleItemSet;
import me.planetguy.remaininmotion.base.ToolItemSet;

public abstract class RiMItems {
	public static ToolItemSet	ToolItemSet;

	public static SimpleItemSet	SimpleItemSet;

	public static void Initialize(PLHelper helper) {
		ToolItemSet = (ToolItemSet) helper.loadItem(ToolItemSet.class, ModRiM.content);

		SimpleItemSet = (SimpleItemSet) helper.loadItem(SimpleItemSet.class, ModRiM.content);

	}
}
