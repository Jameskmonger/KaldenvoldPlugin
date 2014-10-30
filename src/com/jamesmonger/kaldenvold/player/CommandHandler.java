package com.jamesmonger.kaldenvold.player;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.jamesmonger.kaldenvold.KaldenvoldPlugin;

public class CommandHandler
{
	KaldenvoldPlugin plugin;

	public CommandHandler(KaldenvoldPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	public boolean handleCommand(final CommandSender sender, Command cmd, String label,
			String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("acceptplayer"))
		{
			if(args.length != 1)
			{
				sender.sendMessage("Usage: /acceptplayer [name]");
				return true;
			}
			
			Player _target = plugin.getServer().getPlayer(args[0]);
			
			PermissionUser target = PermissionsEx.getUser(_target);
			
			target.addGroup("accepted");
			
			_target.sendMessage(ChatColor.GRAY + "You have been accepted to " + ChatColor.WHITE + "Kaldenvold" + ChatColor.GRAY + " by " + ChatColor.WHITE + sender.getName() + ChatColor.GRAY + "!");
			sender.sendMessage(ChatColor.GRAY + "You have been accepted " + ChatColor.WHITE + _target.getName() + ChatColor.GRAY + " to " + ChatColor.WHITE + "Kaldenvold" + ChatColor.GRAY + "!");
			return true;
		}
		if (cmd.getName().equals("me"))
		{
			if (args.length == 0)
			{
				sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.WHITE + "/me [action]");
				return true;
			}
			
			String action = StringUtils.join(args, " ");
			plugin.handleAction((Player) sender, action);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("togglegooc"))
		{
			if (sender.hasPermission("kaldenvold.admin.globalooc") || sender.isOp())
			{
				if (KaldenvoldPlugin.globalOOC)
				{
					KaldenvoldPlugin.globalOOC = false;
					sender.sendMessage(ChatColor.GRAY + "Global OOC disabled.");
					
					for (Player p : plugin.getServer().getOnlinePlayers())
					{
						if (p.isOp())
							continue;
						
						if (p.hasPermission("kaldenvold.chat.globalooc"))
							continue;
						
						KaldenvoldPlayer kp = KaldenvoldPlugin.playerList.get(p);
						if (kp.getChatType() == ChatType.GLOBAL_OOC)
						{
							kp.setChatType(ChatType.LOCAL_OOC);
							p.sendMessage(ChatColor.GRAY + "Global OOC disabled, you are now talking in the " + ChatColor.WHITE + "Local OOC" + ChatColor.GRAY + " channel");
						}
					}
					return true;
				}
				KaldenvoldPlugin.globalOOC = true;
				sender.sendMessage(ChatColor.GRAY + "Global OOC enabled.");
				return true;
			}
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("ooc")
				|| cmd.getName().equalsIgnoreCase("o"))
		{
			sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/channel" + ChatColor.GRAY + " instead");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("channel")
				|| cmd.getName().equalsIgnoreCase("ch"))
		{
			boolean canUseGlobal = false;
			if (sender.hasPermission("kaldenvold.chat.globalooc") || sender.isOp())
				canUseGlobal = true;
			if (KaldenvoldPlugin.globalOOC)
				canUseGlobal = true;
			
			if(args.length != 1)
			{
				sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.WHITE + " /<ch>annel [channel]");
				if (canUseGlobal)
					sender.sendMessage(ChatColor.GRAY + "Channels: " + ChatColor.WHITE + " 0 [Local IC], 1 [Local OOC], 2 [Global OOC]");
				else
					sender.sendMessage(ChatColor.GRAY + "Channels: " + ChatColor.WHITE + " 0 [Local IC], 1 [Local OOC]");
				return true;
			}
			
			int selectedChannel = -1;
			
			try
			{
				selectedChannel = Integer.parseInt(args[0]);
			}
			catch (Exception e)
			{
				sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.WHITE + " /<ch>annel [channel]");
				if (canUseGlobal)
					sender.sendMessage(ChatColor.GRAY + "Channels: " + ChatColor.WHITE + " 0 [Local IC], 1 [Local OOC], 2 [Global OOC]");
				else
					sender.sendMessage(ChatColor.GRAY + "Channels: " + ChatColor.WHITE + " 0 [Local IC], 1 [Local OOC]");				
			}
			
			if (selectedChannel == -1)
			{
				if (canUseGlobal)
					sender.sendMessage(ChatColor.GRAY + "Channels: " + ChatColor.WHITE + " 0 [Local IC], 1 [Local OOC], 2 [Global OOC]");
				else
					sender.sendMessage(ChatColor.GRAY + "Channels: " + ChatColor.WHITE + " 0 [Local IC], 1 [Local OOC]");
				return true;
			}
			
			if (selectedChannel == ChatType.GLOBAL_OOC.getNumericValue() && !canUseGlobal)
			{
				sender.sendMessage(ChatColor.GRAY + "You cannot use the Global OOC channel.");
			}
			
			for (ChatType type : ChatType.values())
			{
				if (type.getNumericValue() == selectedChannel)
				{
					sender.sendMessage(ChatColor.GRAY + "You are now talking in the " + type.getName() + " channel.");
					KaldenvoldPlugin.playerList.get(sender).setChatType(type);
				}
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("autotranslate"))
		{
			if (!sender.hasPermission("kaldenvold.admin.autotranslate") && !sender.isOp())
			{
				sender.sendMessage(ChatColor.GRAY + "You don't have permission to do that.");
				return true;
			}
			
			KaldenvoldPlayer k_sender = KaldenvoldPlugin.playerList.get(sender);
			if (k_sender.autoTranslate == false)
			{
				sender.sendMessage(ChatColor.GRAY + "Your ears grow and you can understand foreign languages.");
				k_sender.autoTranslate = true;
				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.GRAY + "Your ears shrink back down to their normal size.");
				k_sender.autoTranslate = false;
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("ability"))
		{
			final KaldenvoldPlayer k_sender = KaldenvoldPlugin.playerList.get(sender);
			if (k_sender.getRace().getName().equals("sentinel"))
			{
				if (k_sender.raceAbility == -1)
				{
					Utils.startRaceEffect(this.plugin, (Player) sender, k_sender, PotionEffectType.NIGHT_VISION, 0);
					sender.sendMessage(ChatColor.GRAY + "Your eyes brighten and the world becomes clearer.");
					return true;
				}
				else
				{
					Utils.cancelRaceEffect((Player) sender, k_sender, PotionEffectType.NIGHT_VISION);
					sender.sendMessage(ChatColor.GRAY + "Your eyes dim slightly.");
					return true;
				}
			}
			
			if (k_sender.getRace().getName().equals("hilrin"))
			{				
				if (k_sender.raceAbility == -1)
				{
					if (k_sender.canUseAbility == false)
					{
						sender.sendMessage(ChatColor.GRAY + "You are too tired.");
						return true;
					}
					
					Utils.startRaceEffect(this.plugin, (Player) sender, k_sender, PotionEffectType.SPEED, 1);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin,
							new Runnable() {

								@Override
								public void run() {
									Utils.cancelRaceEffect((Player) sender, k_sender, PotionEffectType.SPEED);
									sender.sendMessage(ChatColor.GRAY + "You become tired.");
								}
						
					}, (20L * 15));
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin,
							new Runnable() {

								@Override
								public void run() {
									sender.sendMessage(ChatColor.GRAY + "You have recovered, and can summon your strength again.");
									k_sender.canUseAbility = true;
								}
						
					}, (20L * 60));
					
					k_sender.canUseAbility = false;
					return true;
				}
				else
				{
					sender.sendMessage(ChatColor.GRAY + "Your ability is already in full flow.");
					return true;
				}
			}
			sender.sendMessage(ChatColor.GRAY + "Your ability cannot be controlled. It is the very essence of your being.");
		}
		return false;
	}
}
