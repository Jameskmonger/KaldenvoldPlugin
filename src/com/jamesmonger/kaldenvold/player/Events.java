package com.jamesmonger.kaldenvold.player;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.potion.PotionEffectType;

import com.jamesmonger.kaldenvold.KaldenvoldPlugin;

public class Events implements Listener
{
	KaldenvoldPlugin plugin;

	public Events(KaldenvoldPlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void playerChat(AsyncPlayerChatEvent event)
	{

		final Player player = event.getPlayer();
		final String msg = event.getMessage();

		Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin,
				new Runnable()
				{
					public void run()
					{
						plugin.handleChat(player, msg);
					}
				});
		event.setCancelled(true);
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) throws IOException
	{
		KaldenvoldPlugin.accountManager.loadAccount(event.getPlayer());
		
		Utils.giveRaceAbility(this.plugin, event.getPlayer());
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event) throws IOException
	{
		Player p = event.getPlayer();
		
		KaldenvoldPlugin.accountManager.saveAccount(p);
		
		KaldenvoldPlayer k_player = com.jamesmonger.kaldenvold.KaldenvoldPlugin.playerList.get(p);
		
		if (k_player.getRace().getName().equals("hilrin"))
		{
			Utils.cancelRaceEffect(p, k_player, PotionEffectType.SPEED);
		}
		
		if (k_player.raceAbility != -1)
		{
			Bukkit.getScheduler().cancelTask(k_player.raceAbility);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerRespawn(PlayerRespawnEvent event)
	{
		Player p = event.getPlayer();
		
		KaldenvoldPlayer k_player = com.jamesmonger.kaldenvold.KaldenvoldPlugin.playerList.get(p);
		
		if (k_player.getRace().getName().equalsIgnoreCase("none"))
		{
			Location spawn = k_player.getRace().getHome(Bukkit.getWorld("world"));
			event.setRespawnLocation(spawn);
			
			p.sendMessage("You must select a race.");
			return;
		}
		
		Location essBed = plugin.essentials.getUser(p).getBedSpawnLocation();
		if (essBed != null)
		{
			event.setRespawnLocation(essBed);
			return;
		}
		
		Location mcBed = p.getBedSpawnLocation();
		if (mcBed != null)
		{
			event.setRespawnLocation(mcBed);
			return;
		}
		
		event.setRespawnLocation(k_player.getRace().getHome(Bukkit.getWorld("world")));
		return;
	}

	@EventHandler
    public void playerHungerChange(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        
        int before = p.getFoodLevel();
        int after = e.getFoodLevel();
        
        final KaldenvoldPlayer k_player = KaldenvoldPlugin.playerList
				.get(p);
        if (k_player.getRace().getName().equalsIgnoreCase("norin"))
        {
        	if (before > after)
        	{
        		Random randomGenerator = new Random();
        		int chance = randomGenerator.nextInt(100);
        		
        		if (chance > 40)
        		{
        			e.setCancelled(true);
        		}
        	}
        }
    }  
	
	@EventHandler
	public void playerBreakBlock(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Material playerHandItem = player.getItemInHand().getType();
		Material brokenBlockType = event.getBlock().getType();
		
		if (player.getGameMode() != GameMode.CREATIVE)
		{
			if (Utils.requiresWoodOrAbovePickaxe(brokenBlockType) && !Utils.isWoodOrAbovePickaxe(playerHandItem))
			{
				player.sendMessage(ChatColor.GRAY + "You must use a pickaxe to break this.");
				event.setCancelled(true);
			}
			if (Utils.requiresStoneOrAbovePickaxe(brokenBlockType) && !Utils.isStoneOrAbovePickaxe(playerHandItem))
			{
				player.sendMessage(ChatColor.GRAY + "You must use a pickaxe made of at least stone to break this.");
				event.setCancelled(true);
			}
			if (Utils.requiresGoldOrAbovePickaxe(brokenBlockType) && !Utils.isGoldOrAbovePickaxe(playerHandItem))
			{
				player.sendMessage(ChatColor.GRAY + "You must use a pickaxe made of at least gold to break this.");
				event.setCancelled(true);
			}
			if (Utils.requiresIronOrAbovePickaxe(brokenBlockType) && !Utils.isIronOrAbovePickaxe(playerHandItem))
			{
				player.sendMessage(ChatColor.GRAY + "You must use a pickaxe made of at least iron to break this.");
				event.setCancelled(true);
			}
			if (Utils.requiresDiamondPickaxe(brokenBlockType) && !Utils.isDiamondPickaxe(playerHandItem))
			{
				player.sendMessage(ChatColor.GRAY + "You must use a pickaxe of at least diamond to break this.");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPlaceSign(SignChangeEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		String[] text = event.getLines();

		Material type = block.getType();
		if ((type == Material.SIGN) || (type == Material.SIGN_POST))
		{
			if (text[0].replaceAll("(?i)ยง[0-F]", "").toLowerCase()
					.equals("[action]"))
			{
				if (!player.isOp()
						&& !player.hasPermission("kaldenvold.admin.actionsign"))
				{
					player.sendMessage(ChatColor.GRAY
							+ "You don't have the required permission to place action signs.");
					event.setCancelled(true);
					return;
				}
				if ((text[1].isEmpty()))
				{
					player.sendMessage(ChatColor.RED
							+ "Line 2: action");
					event.setCancelled(true);
					return;
				}
				event.setLine(0, "ง4[Action]");
				return;
			}
			
			if (text[0].replaceAll("(?i)ยง[0-F]", "").toLowerCase()
					.equals("[race]"))
			{
				if (!player.isOp()
						&& !player.hasPermission("kaldenvold.admin.racesign"))
				{
					player.sendMessage(ChatColor.GRAY
							+ "You don't have the required permission to place race signs.");
					event.setCancelled(true);
					return;
				}
				if ((text[1].isEmpty()))
				{
					player.sendMessage(ChatColor.RED
							+ "Line 2: race, Line 4: current race requirement");
					event.setCancelled(true);
					return;
				}

				if (KaldenvoldPlugin.raceList
						.containsKey(text[1].toLowerCase()))
				{
					event.setLine(0, "ง4[Race]");

					if (text[3].equals("none"))
					{
						player.sendMessage("Race selection sign successfully created (from raceless to "
								+ text[1] + ")");
					}
					else if (text[3].length() == 0)
					{
						player.sendMessage("Race selection sign successfully created (from any race to "
								+ text[1] + ")");
					}
					else
					{
						player.sendMessage("Race selection sign successfully created (from "
								+ text[3] + " to " + text[1] + ")");
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "'" + text[1]
							+ "' is not a valid race.");
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.getEntityType() == EntityType.PLAYER)
		{
			Player player = (Player) event.getEntity();
			KaldenvoldPlayer k_player = com.jamesmonger.kaldenvold.KaldenvoldPlugin.playerList.get(player);
						
			String raceName = k_player.getRace().getName();
			if (raceName.equals("fohriil"))
			{
				if (event.getCause() == DamageCause.FALL)
				{
					double newDamage = (event.getDamage() - 6);
					if (newDamage < 0)
						newDamage = 0;
					
					event.setDamage(newDamage);
				}
			}
			
			if (raceName.equals("sharin"))
			{
				if (event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FIRE ||
						event.getCause() == DamageCause.FIRE_TICK)
				{
					event.setDamage(event.getDamage() * 0.6);
				}
			}
			
			if (raceName.equals("orc"))
			{
				if (event.getCause() == DamageCause.POISON || event.getCause() == DamageCause.MAGIC)
				{
					event.setDamage(event.getDamage() * 1.2);
				}
				
				if (event.getCause() == DamageCause.THORNS || event.getCause() == DamageCause.ENTITY_ATTACK
						|| event.getCause() == DamageCause.PROJECTILE || event.getCause() == DamageCause.CONTACT)
				{
					event.setDamage(event.getDamage() * 0.9);
					
					Random randomGenerator = new Random();
	        		int chance = randomGenerator.nextInt(100);
	        		
	        		if (chance > 60)
	        		{
	        			event.setDamage(0.0);
	        		}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block block = event.getClickedBlock();
			if ((block.getType() == Material.SIGN_POST)
					|| (block.getType() == Material.WALL_SIGN))
			{
				Sign sign = (Sign) block.getState();
				final Player player = event.getPlayer();
				final KaldenvoldPlayer k_player = KaldenvoldPlugin.playerList
						.get(player);
				if (sign.getLine(0).equals("ง4[Action]"))
				{
					if (sign.getLine(1).equals("Leave Temple"))
					{
						if (k_player.getRace().getName().equalsIgnoreCase("none"))
						{
							player.sendMessage(ChatColor.GRAY + "You must select a race to leave the temple.");
							return;
						}
						Location home = k_player.getRace().getHome(player.getWorld());
						home.setYaw(k_player.getRace().getHomeRotation());
						player.teleport(home);
						player.sendMessage(ChatColor.GRAY + "A tingling sensation comes over you as you are taken from the temple.");
						
						Utils.giveRaceAbility(this.plugin, player);
					}
					return;
				}
				
				if (sign.getLine(0).equals("ง4[Race]"))
				{
					if (player.getInventory().getItemInHand().getType() != Material.AIR)
					{
						player.sendMessage("Your hand must be empty to use a race sign.");
						event.setCancelled(true);
						return;
					}

					if (k_player.getRace().getName()
							.equalsIgnoreCase(sign.getLine(1).toLowerCase()))
					{
						player.sendMessage("You are already a "
								+ sign.getLine(1) + ".");
						event.setCancelled(true);
						return;
					}

					if (!k_player.getRace().getName()
							.equalsIgnoreCase(sign.getLine(3).toLowerCase())
							&& sign.getLine(3).toLowerCase().length() > 0)
					{
						player.sendMessage("You must be a " + sign.getLine(3)
								+ " to use this sign.");
						event.setCancelled(true);
						return;
					}

					if (k_player.awaitingRaceClick != null
							&& k_player.awaitingRaceClick.equalsIgnoreCase(sign
									.getLine(1).toLowerCase()))
					{
						k_player.setRace(sign.getLine(1).toLowerCase());
						player.sendMessage("You are now a " + sign.getLine(1)
								+ ".");
					}
					else
					{
						k_player.awaitingRaceClick = sign.getLine(1)
								.toLowerCase();
						player.sendMessage("Are you sure you want to be a "
								+ sign.getLine(1) + "? Click again to confirm.");

						if (k_player.pendingEvent != -1)
							this.plugin.getServer().getScheduler().cancelTask(k_player.pendingEvent);
						
						k_player.pendingEvent = this.plugin.getServer().getScheduler()
								.scheduleSyncDelayedTask(plugin, new Runnable()
								{
									@Override
									public void run()
									{
										if (!k_player
												.getRace()
												.getName()
												.equals(k_player.awaitingRaceClick))
										{
											player.sendMessage(ChatColor.GRAY
													+ "Pending race change request expired.");
										}
										k_player.awaitingRaceClick = new String();
										k_player.pendingEvent = -1;
									}
								}, 100L);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{
		if ((event instanceof PlayerDeathEvent))
		{
			PlayerDeathEvent e = (PlayerDeathEvent) event;
			e.setDeathMessage(null);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		if ((player.getLastDamageCause() instanceof EntityDamageByEntityEvent))
		{
			EntityDamageByEntityEvent deathEvent = (EntityDamageByEntityEvent) player
					.getLastDamageCause();

			Entity killer = deathEvent.getDamager();
			if ((killer instanceof Player))
			{
				DeathMessage.constructDeathMessage(player, "player", killer);
				return;
			}
			if (((killer instanceof Creeper)) || ((killer instanceof Enderman))
					|| ((killer instanceof Blaze))
					|| ((killer instanceof CaveSpider))
					|| ((killer instanceof Ghast))
					|| ((killer instanceof IronGolem))
					|| ((killer instanceof MagmaCube))
					|| ((killer instanceof PigZombie))
					|| ((killer instanceof Slime))
					|| ((killer instanceof Spider))
					|| ((killer instanceof Silverfish))
					|| ((killer instanceof Witch))
					|| ((killer instanceof Wolf))
					|| ((killer instanceof Zombie)))
			{
				DeathMessage.constructDeathMessage(player, "monster", killer);
				return;
			}
			if ((killer instanceof WitherSkull))
			{
				DeathMessage
						.constructDeathMessage(player, "witherhead", killer);
				return;
			}
			if ((killer instanceof TNTPrimed))
			{
				DeathMessage.constructDeathMessage(player, "explosion", killer);
				return;
			}
			if (((killer instanceof Snowball))
					|| ((killer instanceof EnderPearl))
					|| ((killer instanceof Egg)))
			{
				DeathMessage.constructDeathMessage(player, "thrown", killer);
				return;
			}
			if ((killer instanceof FallingBlock))
			{
				DeathMessage.constructDeathMessage(player, "block", killer);
				return;
			}
			if ((killer instanceof Arrow))
			{
				Arrow arrow = (Arrow) deathEvent.getDamager();
				if (((arrow.getShooter() instanceof Skeleton))
						|| ((arrow.getShooter() instanceof Player)))
				{
					DeathMessage.constructDeathMessage(player, "arrow",
							arrow.getShooter());
					return;
				}
			}
			DeathMessage.constructDeathMessage(player, "unknown", killer);
		}
		else
		{
			EntityDamageEvent.DamageCause cause = player.getLastDamageCause()
					.getCause();

			DeathMessage.constructDeathMessage(player, cause.toString(), null);
			return;
		}
	}

	@EventHandler
	public void onPlayerEnterVehicle(VehicleEnterEvent event)
	{
		Player player = (Player) event.getEntered();
		if (event.getVehicle().getType() == EntityType.HORSE)
		{
			if (!player.hasPermission("kaldenvold.horse.mount"))
			{
				player.sendMessage("You try to climb on the horse, but stumble and fall off.");
				event.setCancelled(true);
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args)
	{
		return plugin.commandHandler.handleCommand(sender, cmd, label, args);
	}

	public static String strJoin(String[] aArr, String sSep)
	{
		StringBuilder sbStr = new StringBuilder();
		for (int i = 0, il = aArr.length; i < il; i++)
		{
			if (i > 0)
				sbStr.append(sSep);
			sbStr.append(aArr[i]);
		}
		return sbStr.toString();
	}
}
