package me.planetguy.remaininmotion.core;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public abstract class Config {
	public Configuration	Configuration;

	public String			Category;

	public Config(File File) {
		Configuration = new Configuration(File, true);
	}

	public String String(String Name, String Default) {
		return (Configuration.get(Category, Name, Default).getString());
	}

	public String String(String Name, String comment, String Default) {
		Property a = Configuration.get(Category, Name, Default);
		a.comment = comment;
		return a.getString();
	}

	public boolean Boolean(String Name, boolean Default) {
		return (Configuration.get(Category, Name, Default).getBoolean(Default));
	}

	public boolean Boolean(String Name, String comment, boolean Default) {
		Property a = Configuration.get(Category, Name, Default);
		a.comment = comment;
		return a.getBoolean(Default);
	}

	public int Integer(String Name, int Default) {
		return (Configuration.get(Category, Name, Default).getInt(Default));
	}

	public int Integer(String Name, String comment, int Default) {
		Property a = Configuration.get(Category, Name, Default);
		a.comment = comment;
		return a.getInt(Default);
	}

	public double Double(String Name, double Default) {
		return (Configuration.get(Category, Name, Default).getDouble(Default));
	}

	public double Double(String Name, String comment, double Default) {
		Property a = Configuration.get(Category, Name, Default);
		a.comment = comment;
		return a.getDouble(Default);
	}

	public int BoundedInteger(String Name, int Min, int Default, int Max) {
		int Value = Integer(Name, Default);

		if (Value < Min) {
			new RuntimeException(Name + " must be at least " + Min).printStackTrace();

			return (Default);
		}

		if (Value > Max) {
			new RuntimeException(Name + " must be at most " + Max).printStackTrace();

			return (Default);
		}

		return (Value);
	}

	public int BoundedInteger(String Name, String comment, int Min, int Default, int Max) {
		int Value = Integer(Name, comment, Default);

		if (Value < Min) {
			new RuntimeException(Name + " must be at least " + Min).printStackTrace();

			return (Default);
		}

		if (Value > Max) {
			new RuntimeException(Name + " must be at most " + Max).printStackTrace();

			return (Default);
		}

		return (Value);
	}
}
