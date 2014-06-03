package me.planetguy.remaininmotion.core;

import java.lang.reflect.Field;

public class RIMLog {

	public static void t(Object o){
		System.out.println("[RIM]"+o);
	}

	public static void dump(Object o){
		dump(o,2, "");
	}

	private static void dump(Object o, int iteration, String start){
		if(!(iteration>0))return;
		t(start+o);
		if(o!=null){ //Don't dump null's fields
			for(Field f:o.getClass().getDeclaredFields()){
				f.setAccessible(true);
				try{
					Object subObj=f.get(o);
					dump(subObj, iteration-1, start+f.getName()+"      ");
				}catch(Exception e){}

			}
		}
	}

}
