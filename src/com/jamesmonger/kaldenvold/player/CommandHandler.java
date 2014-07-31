package com.jamesmonger.kaldenvold.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.jamesmonger.kaldenvold.KaldenvoldPlugin;
import com.jamesmonger.kaldenvold.race.Race;

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
			}
			
			Player _target = plugin.getServer().getPlayer(args[0]);
			
			PermissionUser target = PermissionsEx.getUser(_target);
			
			target.addGroup("accepted");
			
			_target.sendMessage(ChatColor.GRAY + "You have been accepted to " + ChatColor.WHITE + "Kaldenvold" + ChatColor.GRAY + " by " + ChatColor.WHITE + sender.getName() + ChatColor.GRAY + "!");
			_target.sendMessage(ChatColor.GRAY + "You have been accepted " + ChatColor.WHITE + _target.getName() + ChatColor.GRAY + " to " + ChatColor.WHITE + "Kaldenvold" + ChatColor.GRAY + "!");
		}
		if (cmd.getName().equalsIgnoreCase("ooc")
				|| cmd.getName().equalsIgnoreCase("o"))
		{
			KaldenvoldPlayer k_sender = KaldenvoldPlugin.playerList.get(sender);
			
			if (k_sender.getRace().getName().equalsIgnoreCase("none"))
			{
				((Player) sender).sendMessage(ChatColor.GRAY + "You must choose a race to speak in character.");
				k_sender.ooc = true;
				return true;
			}
			if (k_sender.ooc == true)
			{
				sender.sendMessage(ChatColor.GRAY + "You are now speaking in the IC channel.");
				k_sender.ooc = false;
				return true;
			}
			if (k_sender.ooc == false)
			{
				sender.sendMessage(ChatColor.GRAY + "You are now speaking in the OOC channel.");
				k_sender.ooc = true;
				return true;
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
