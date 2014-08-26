package me.planetguy.remaininmotion;

import java.util.ArrayList;
import java.util.List;

import me.planetguy.remaininmotion.api.CarriageMatcher;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.util.Debug;

public class CarriageMatchers {
	
	public static List<CarriageMatcher> matchers=new ArrayList<CarriageMatcher>();
	
	public static void register(CarriageMatcher matcher){
		matchers.add(matcher);
	}
	
	public static boolean matches(net.minecraft.block.Block b, int meta, net.minecraft.tileentity.TileEntity te, CarriagePackage pkg){
		for(CarriageMatcher m:matchers){
			if(m.matches(b, meta, te, pkg.AnchorRecord.block, pkg.AnchorRecord.Meta, pkg.AnchorRecord.Entity)){
				return true;
			}
		}
		return false;
	}
	
	public static Moveable getMover(net.minecraft.block.Block blockslist, int meta, net.minecraft.tileentity.TileEntity te) {
		for(CarriageMatcher m:matchers){
			Moveable mv=m.getCarriage(blockslist, meta, te);
			Debug.dbg("CM "+m+" blockslist "+blockslist+" -> "+mv);
			if(mv!=null)
				return mv;
		}
		return null;
	}
	

}
