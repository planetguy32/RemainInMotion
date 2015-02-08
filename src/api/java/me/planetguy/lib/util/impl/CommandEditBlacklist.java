package me.planetguy.lib.util.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import me.planetguy.lib.util.BlacklistDynamic;
import me.planetguy.lib.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class CommandEditBlacklist implements ICommand{
	
	//Do not refer to these until game is playing
	public static HashMap<String, BlacklistDynamic> blacklists=new HashMap<String, BlacklistDynamic>();

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		Debug.mark();
		return "plBlacklist";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		Debug.mark();
		return "/plBlacklist (-precise) <blacklistName>\n/plBlacklist list";
	}

	@Override
	public List getCommandAliases() {
		return new ArrayList();
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(sender.canCommandSenderUseCommand(4, "plBlacklist")) {
			if(sender instanceof EntityPlayer) {
				EntityPlayer player=(EntityPlayer) sender;
				ItemStack hand=player.getHeldItem();
				if(hand.getItem() instanceof ItemBlock) {
					Block block=((ItemBlock)hand.getItem()).field_150939_a;
					if(args.length==2) {
						if(args[0].equals("-precise")) {
							BlacklistDynamic bl=blacklists.get(args[1]);
							bl.blacklistIngame(block, hand.getItemDamage());
							sender.addChatMessage(new ChatComponentText("Blacklisted "+block+":"+hand.getItemDamage()));
						}
					}else if(args.length==1) {
						if(args[0].equals("list")) {
							String names="";
							boolean first=true;
							for(String s:blacklists.keySet()) {
								if(!first)
									names+=", ";
								names+=s;
								first=false;
							}
							sender.addChatMessage(new ChatComponentText(names));
						}else {
							BlacklistDynamic bl=blacklists.get(args[0]);
							bl.blacklistIngame(block);
							sender.addChatMessage(new ChatComponentText("Blacklisted "+block));
						}
					}
				}else {
					sender.addChatMessage(new ChatComponentText("You are not holding a block!"));
				}
			}else
				sender.addChatMessage(new ChatComponentText("Only players can blacklist their held item!"));
		}else {
			sender.addChatMessage(new ChatComponentText("You do not have permission to edit blacklists!"));
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender instanceof EntityPlayer && sender.canCommandSenderUseCommand(4,"plBlacklist");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender send,
			String[] args) {
		List tco=new ArrayList();
		if(args.length<0)
			for(String s:blacklists.keySet())
				tco.add(s);
		return tco;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}
