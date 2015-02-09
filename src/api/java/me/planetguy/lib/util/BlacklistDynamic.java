package me.planetguy.lib.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.planetguy.lib.util.impl.CommandEditBlacklist;
import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * A Blacklist that can be edited in-game. 
 * @author planetguy
 *
 */
public class BlacklistDynamic extends Blacklist {
	
	private final Configuration conf;
	private final Property prop;
	private final String name;
	
	public BlacklistDynamic(Configuration conf, Property prop, String name) {
		this.conf=conf;
		this.prop=prop;
		this.name=name;
		
		CommandEditBlacklist.blacklists.put(name, this);
		
		setupBlacklist();
	}
	
	private void setupBlacklist() {
		String Blacklist = prop.getString();

		if (!Blacklist.equals("")) {
			for (String BlacklistItem : Blacklist.split(",")) {
				String[] BlacklistItemElements = BlacklistItem.split(":");

				try {
					if (BlacklistItemElements.length == 1) {
						this.blacklist(Block.getBlockFromName(BlacklistItemElements[0]));

						continue;
					}

					if (BlacklistItemElements.length == 2) {
						this.blacklist(Block.getBlockFromName(BlacklistItemElements[0]),
								Integer.parseInt(BlacklistItemElements[1]));

						continue;
					}
				} catch (Throwable Throwable) {
					Throwable.printStackTrace();
				}

				Debug.dbg("Invalid object: "+BlacklistItem);
			}
		}
	}
	
	public void blacklistIngame(Block block) {
		super.blacklist(block);
		conf.load();
		String old=prop.getString();
		if(old.length() == 0)
			old=Block.blockRegistry.getNameForObject(block);
		else
			old += "," + Block.blockRegistry.getNameForObject(block);
		prop.set(old);
		conf.save();
	}

	public void blacklistIngame(Block block, int Meta) {
		super.blacklist(block, Meta);
		conf.load();
		String old=prop.getString();
		if(old.length() == 0)
			old=Block.blockRegistry.getNameForObject(block)+":"+Meta;
		else
			old += "," + Block.blockRegistry.getNameForObject(block)+":"+Meta;
		prop.set(old);
		conf.save();
	}
	
}
