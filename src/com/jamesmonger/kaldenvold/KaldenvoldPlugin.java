package com.jamesmonger.kaldenvold;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.jamesmonger.kaldenvold.language.Scrambler;
import com.jamesmonger.kaldenvold.player.AccountManager;
import com.jamesmonger.kaldenvold.player.CommandHandler;
import com.jamesmonger.kaldenvold.player.Events;
import com.jamesmonger.kaldenvold.player.KaldenvoldPlayer;
import com.jamesmonger.kaldenvold.player.Utils;
import com.jamesmonger.kaldenvold.race.*;

public class KaldenvoldPlugin extends JavaPlugin
{
	public static HashMap<String, Race> raceList = new HashMap<String, Race>();
	public static HashMap<Player, KaldenvoldPlayer> playerList = new HashMap<Player, KaldenvoldPlayer>();
	public static AccountManager accountManager;
	public CommandHandler commandHandler;
	public Events eventInstance = new Events(this);
	
	String ADMIN_TAG = (ChatColor.GREEN + "[A]");
	String MODERATOR_TAG = (ChatColor.DARK_AQUA + "[M]");
	String HELPER_TAG = (ChatColor.BLUE + "[H]");
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(eventInstance, this);
		accountManager = new AccountManager(this);
		commandHandler = new CommandHandler(this);
		new Race(this, "none", "none", RaceTypes.NONE, 896.639, 24.0, 316.493, 90.2f);
		new Race(this, "norin", "Norin", RaceTypes.HUMAN, -302.500, 64.0, -362.500, 44.635f);
		new Race(this, "hilrin", "Hilrin", RaceTypes.HUMAN, -873.500, 81.0, 448.500, 62.335f);
		new Race(this, "sharin", "Sharin", RaceTypes.HUMAN, 631.500, 63.0, -839.500, 45.835f);
		new Race(this, "aayriil", "Aayriil", RaceTypes.ELF, -912.500, 63.0, 1244.500, 59.635f);
		new Race(this, "fohriil", "Fohriil", RaceTypes.ELF, -912.500, 63.0, 1244.500, 166.885f);
		new Race(this, "orc", "Orc", RaceTypes.ORC, -672.500, 63.0, 858.500, -88.865f);
		new Race(this, "dwarf", "Dwarf", RaceTypes.DWARF, -1135.259, 65.000, -1125.986, 77.935f);
		new Race(this, "rheylin", "Rheylin", RaceTypes.RHEYLIN, 876.017, 27.0, 981.817, 91.740f);
		new Race(this, "sentinel", "Sentinel", RaceTypes.SENTINEL, 826.961, 65.0, 535.409, -90.965f);

		for (Player player : this.getServer().getOnlinePlayers())
		{
			KaldenvoldPlugin.accountManager.LoadAccount(player);
			
			Utils.giveRaceAbility(this, player);
		}
	}

	public void onDisable()
	{
		for (Player player : this.getServer().getOnlinePlayers())
		{
			KaldenvoldPlugin.accountManager.SaveAccount(player);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args)
	{
		return eventInstance.onCommand(sender, cmd, label, args);
	}
	
	public Race getRaceByLiteralName(String name)
	{
		Iterator<Entry<String, Race>> it = raceList.entrySet().iterator();
		
		while (it.hasNext())
		{
			Entry<String, Race> pairs = (Entry<String, Race>) it.next();
			
			if (pairs.getValue().getName().equals(name))
			{
				return pairs.getValue();
			}
			it.remove();
		}
		
		return null;
	}

	public void SendChatToAll(Player chatter, String msg)
	{
		String finalisedMessage = null;
		String chatterName = ChatColor.stripColor(chatter.getDisplayName());
		KaldenvoldPlayer sender = KaldenvoldPlugin.playerList.get(chatter);
		
		if (sender.getRace().getName().equalsIgnoreCase("none"))
		{
			if(sender.ooc == false)
			{
				chatter.sendMessage(ChatColor.GRAY + "You must choose a race to speak in character.");
				sender.ooc = true;
			}
		}
		
		if (sender.ooc == false)
		{
			Location chatterLocation = chatter.getLocation();
	
			int playersHeard = 0;
			for (Player player : this.getServer().getOnlinePlayers())
			{
				KaldenvoldPlayer k_player = KaldenvoldPlugin.playerList.get(player);
				if (player.getLocation().distance(chatterLocation) <= 25.0)
				{
					if (player != chatter)
					{
						playersHeard++;
	
						if (k_player.getRace().getType() != RaceTypes.NONE && k_player.autoTranslate == false)
						{
							if (!sender.getRace()
									.sharesLanguage(k_player.getRace()))
							{
								k_player.increaseUnderstanding(sender.getRace()
										.getType(), 1);
							}
						}
					}
	
					if (sender.getRace().sharesLanguage(k_player.getRace())
							|| player == chatter
							|| k_player.getRace().getType() == RaceTypes.NONE
							|| k_player.autoTranslate == true)
					{
						finalisedMessage = msg;
						player.sendMessage(sender.getRace().getColor() + "["
								+ sender.getRace().getDisplayName() + "] "
								+ ChatColor.WHITE + chatterName + ": "
								+ finalisedMessage);
					}
					else
					{
						int highestUnderstanding = k_player.getUnderstanding(sender.getRace().getType());
						KaldenvoldPlayer language = k_player;
						
						int senderUnderstanding = sender.getUnderstanding(k_player.getRace().getType());
						if (senderUnderstanding > highestUnderstanding)
						{
							highestUnderstanding = senderUnderstanding;
							language = sender;
						}
						
						int color = 2;
						String foreignString = ChatColor.RED  + "[Foreign";
						if (highestUnderstanding >= 700)
						{
							color = 1;
							foreignString = ChatColor.YELLOW + "[Foreign";
						}
						if (highestUnderstanding >= 1000)
						{
							foreignString = "";
						}
						
						if (!foreignString.equals(""))
						{
							if (language == k_player)
							{
								foreignString = foreignString + ChatColor.GREEN + "*";
							}
							if (color == 1)
							{
								foreignString = foreignString + ChatColor.YELLOW;
							}
							else if (color == 2)
							{
								foreignString = foreignString + ChatColor.RED;
							}
							
							foreignString = foreignString + "] ";
						}
						
						if (highestUnderstanding > 700)
							highestUnderstanding = 700;
						finalisedMessage = foreignString + ChatColor.WHITE + Scrambler.scrambleString(msg, highestUnderstanding);
						
						player.sendMessage(sender.getRace().getColor() + "["
								+ sender.getRace().getDisplayName() + "] "
								+ ChatColor.WHITE + chatterName + ": "
								+ finalisedMessage);
					}
				}
			}
	
			if (playersHeard == 0)
			{
				chatter.sendMessage(ChatColor.YELLOW + "Nobody can hear you!");
			}
		}
		else
		{
			String tag = getRankTag(chatter);
			if (tag.length() > 0)
			{
				tag = (tag + " ");
			}
			getServer().broadcastMessage(ChatColor.GRAY + "[OOC] " + tag + ChatColor.WHITE + chatterName + ": " + msg);
		}
	}

	public String getRankTag(Player player)
	{
		PermissionUser user = PermissionsEx.getUser(player);
		if (user.inGroup("admin"))
			return ADMIN_TAG;
		
		if (user.inGroup("moderator"))
			return MODERATOR_TAG;
		
		if (user.inGroup("helper"))
			return HELPER_TAG;
		
		return "";
	}
}
