package com.jamesmonger.kaldenvold.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class DeathMessage
{
	public static void constructDeathMessage(Player deadplayer, String type,
			Entity killer)
	{
		switch (type.toLowerCase())
		{
			case "player":
				if (((Player) killer).isSneaking())
				{
					sendDeathMessage(deadplayer,
							"was killed by an unknown assailant");
				}
				else
				{
					sendDeathMessage(deadplayer, "was slain by",
							(Player) killer, "with their", ((Player) killer)
									.getInventory().getItemInHand());
				}
			break;
			case "arrow":
				if (killer instanceof Skeleton)
				{
					sendDeathMessage(deadplayer,
							"had a bone to pick with the wrong skeleton");
				}
				if (killer instanceof Player)
				{
					if (((Player) killer).isSneaking())
					{
						sendDeathMessage(deadplayer,
								"was hit with arrows from an unknown assailant");
					}
					else
					{
						if (getItemName(
								((Player) killer).getInventory()
										.getItemInHand()).equalsIgnoreCase(
								"bow"))
						{
							sendDeathMessage(deadplayer,
									"was riddled with arrows by",
									(Player) killer);
						}
						else
						{
							sendDeathMessage(deadplayer,
									"was riddled with arrows by",
									(Player) killer, "with their",
									((Player) killer).getInventory()
											.getItemInHand());
						}
					}
				}
			break;
			case "witherhead":
				sendDeathMessage(deadplayer,
						"came head-to-head-to-head with a Wither");
			break;
			case "wither":
				sendDeathMessage(deadplayer,
						"came head-to-head-to-head with a Wither");
			break;
			case "block":
				sendDeathMessage(deadplayer, "was buried alive");
			break;
			case "explosion":
				if (killer instanceof TNTPrimed)
				{
					sendDeathMessage(deadplayer, "stepped on TNT");
				}
			break;
			case "monster":
				if (killer instanceof Creeper)
				{
					sendDeathMessage(deadplayer, "hugged a Creeper");
				}
				if (killer instanceof Enderman)
				{
					sendDeathMessage(deadplayer, "came up short to an Enderman");
				}
				if (killer instanceof Blaze)
				{
					sendDeathMessage(deadplayer, "got burnt by a Blaze");
				}
				if (killer instanceof CaveSpider)
				{
					sendDeathMessage(deadplayer,
							"became enemies with a Cave Spider");
				}
				if (killer instanceof Ghast)
				{
					sendDeathMessage(deadplayer, "met a Ghast-ly end");
				}
				if (killer instanceof IronGolem)
				{
					sendDeathMessage(deadplayer, "was killed by an Iron Golem");
				}
				if (killer instanceof MagmaCube)
				{
					sendDeathMessage(deadplayer, "was toasted by a magma cube");
				}
				if (killer instanceof PigZombie)
				{
					sendDeathMessage(deadplayer,
							"picked on the wrong Zombie Pigman");
				}
				if (killer instanceof Slime)
				{
					sendDeathMessage(deadplayer, "met a Slime-y end");
				}
				if (killer instanceof Spider)
				{
					if (killer instanceof CaveSpider)
					{
					}
					else
					{
						sendDeathMessage(deadplayer, "didn't become Spiderman");
					}
				}
				if (killer instanceof Silverfish)
				{
					sendDeathMessage(deadplayer, "mined the wrong stone");
				}
				if (killer instanceof Witch)
				{
					sendDeathMessage(deadplayer, "tried to invade a hut");
				}
				if (killer instanceof Wolf)
				{
					sendDeathMessage(deadplayer,
							"wasn't the boy who cried wolf");
				}
				if (killer instanceof Zombie)
				{
					if (killer instanceof PigZombie)
					{
					}
					else
					{
						sendDeathMessage(deadplayer,
								"gave up their braaaainnnsssss...");
					}
				}
			break;
			case "thrown":
				if (killer instanceof Snowball)
				{
					sendDeathMessage(deadplayer,
							"was slain by a deadly snowball");
				}
				if (killer instanceof Egg)
				{
					sendDeathMessage(deadplayer, "was slain by a deadly egg");
				}
				if (killer instanceof EnderPearl)
				{
					sendDeathMessage(deadplayer,
							"was slain by a deadly ender pearl");
				}
			break;
			case "block_explosion":
				sendDeathMessage(deadplayer, "blew up");
			break;
			case "contact":
				sendDeathMessage(deadplayer, "touched a painful block");
			break;
			case "drowning":
				sendDeathMessage(deadplayer, "forgot to swim");
			break;
			case "entity_attack":
				sendDeathMessage(deadplayer, "tried to break up a fight");
			break;
			case "entity_explosion":
				sendDeathMessage(deadplayer, "blew up");
			break;
			case "fall":
				sendDeathMessage(deadplayer, "tried to fly");
			break;
			case "falling_block":
				sendDeathMessage(deadplayer, "got crushed");
			break;
			case "fire":
				sendDeathMessage(deadplayer, "played with fire");
			break;
			case "fire_tick":
				sendDeathMessage(deadplayer, "played with fire");
			break;
			case "lava":
				sendDeathMessage(deadplayer, "tried to swim in lava");
			break;
			case "lightning":
				sendDeathMessage(deadplayer, "was struck by lightning");
			break;
			case "unknown":
				sendDeathMessage(deadplayer, "was a victim of the paranormal");
			break;
			case "melting":
				sendDeathMessage(deadplayer, "was hurt by a snowman");
			break;
			case "poison":
				sendDeathMessage(deadplayer, "was poisoned");
			break;
			case "projectile":
				sendDeathMessage(deadplayer, "was hit from above");
			break;
			case "starvation":
				sendDeathMessage(deadplayer, "forgot to eat");
			break;
			case "suffocation":
				sendDeathMessage(deadplayer, "couldn't breathe");
			break;
			case "suicide":
				sendDeathMessage(deadplayer, "took their own life");
			break;
			case "void":
				sendDeathMessage(deadplayer, "fell into the void");
			break;
		}
	}

	private static void sendDeathMessage(Player player, String message,
			Player killer, String message2, ItemStack itemInHand)
	{
		Bukkit.getServer().broadcastMessage(
				ChatColor.DARK_AQUA
						+ ChatColor.stripColor(player.getDisplayName())
						+ ChatColor.RED + " " + message + " "
						+ ChatColor.DARK_AQUA
						+ ChatColor.stripColor(killer.getDisplayName())
						+ ChatColor.RED + " " + message2 + ChatColor.DARK_AQUA
						+ " " + getItemName(itemInHand));
	}

	private static void sendDeathMessage(Player player, String message)
	{
		Bukkit.getServer().broadcastMessage(
				ChatColor.DARK_AQUA
						+ ChatColor.stripColor(player.getDisplayName())
						+ ChatColor.RED + " " + message);
	}

	private static void sendDeathMessage(Player player, String message,
			Player killer)
	{
		Bukkit.getServer().broadcastMessage(
				ChatColor.DARK_AQUA
						+ ChatColor.stripColor(player.getDisplayName())
						+ ChatColor.RED + " " + message + " "
						+ ChatColor.DARK_AQUA
						+ ChatColor.stripColor(killer.getDisplayName()));
	}

	public static String getItemName(ItemStack is)
	{
		if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName())
		{
			return is.getItemMeta().getDisplayName();
		}
		if (is.getType().name().equalsIgnoreCase("air"))
		{
			return "fists";
		}

		String[] parts = is.getType().name().split("_");

		for (int i = 0; i < parts.length; i++)
		{
			parts[i] = parts[i].substring(0, 1).toUpperCase()
					+ (parts[i].substring(1)).toLowerCase() + " ";
		}

		return concatAll(parts);
	}

	public static String concatAll(String[] s)
	{
		String result = "";
		if (s.length > 0)
		{
			result = s[0]; // start with the first element
			for (int i = 1; i < s.length; i++)
			{
				result = result + s[i];
			}
		}
		return result;
	}
}
