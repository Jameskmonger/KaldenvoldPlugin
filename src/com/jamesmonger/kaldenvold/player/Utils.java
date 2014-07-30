package com.jamesmonger.kaldenvold.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Utils {
	static final Material[] WOOD_PICKAXE_OR_ABOVE = { Material.STONE,
			Material.COBBLESTONE, Material.IRON_ORE, Material.COAL_ORE,
			Material.SANDSTONE, Material.MOSSY_COBBLESTONE,
			Material.COBBLESTONE_STAIRS, Material.SANDSTONE_STAIRS,
			Material.COBBLE_WALL, Material.ICE, Material.PACKED_ICE,
			Material.STAINED_CLAY };

	static final Material[] STONE_PICKAXE_OR_ABOVE = { Material.LAPIS_BLOCK,
			Material.GOLD_ORE, Material.REDSTONE_ORE, Material.SMOOTH_BRICK,
			Material.NETHER_BRICK, Material.NETHER_BRICK_STAIRS,
			Material.COAL_BLOCK, Material.QUARTZ_ORE, Material.QUARTZ_STAIRS,
			Material.BRICK, Material.HARD_CLAY, Material.BRICK_STAIRS,
			Material.SMOOTH_STAIRS };

	static final Material[] GOLD_PICKAXE_OR_ABOVE = { Material.LAPIS_ORE,
			Material.GOLD_BLOCK, Material.DIAMOND_ORE, Material.EMERALD_ORE,
			Material.EMERALD_BLOCK, Material.QUARTZ_BLOCK };

	static final Material[] IRON_PICKAXE_OR_ABOVE = { Material.IRON_BLOCK,
			Material.OBSIDIAN, Material.IRON_DOOR_BLOCK,
			Material.REDSTONE_BLOCK, Material.IRON_FENCE };

	static final Material[] DIAMOND_PICKAXE = { Material.DIAMOND_BLOCK };

	public static boolean requiresWoodOrAbovePickaxe(Material material) {
		for (Material m : WOOD_PICKAXE_OR_ABOVE) {
			if (m.equals(material)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean requiresStoneOrAbovePickaxe(Material material) {
		for (Material m : STONE_PICKAXE_OR_ABOVE) {
			if (m.equals(material)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean requiresGoldOrAbovePickaxe(Material material) {
		for (Material m : GOLD_PICKAXE_OR_ABOVE) {
			if (m.equals(material)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean requiresIronOrAbovePickaxe(Material material) {
		for (Material m : IRON_PICKAXE_OR_ABOVE) {
			if (m.equals(material)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean requiresDiamondPickaxe(Material material) {
		for (Material m : DIAMOND_PICKAXE) {
			if (m.equals(material)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isWoodOrAbovePickaxe(Material material) {
		if (material == Material.WOOD_PICKAXE) {
			return true;
		}
		if (material == Material.STONE_PICKAXE) {
			return true;
		}
		if (material == Material.GOLD_PICKAXE) {
			return true;
		}
		if (material == Material.IRON_PICKAXE) {
			return true;
		}
		if (material == Material.DIAMOND_PICKAXE) {
			return true;
		}
		return false;
	}

	public static boolean isStoneOrAbovePickaxe(Material material) {
		if (material == Material.STONE_PICKAXE) {
			return true;
		}
		if (material == Material.GOLD_PICKAXE) {
			return true;
		}
		if (material == Material.IRON_PICKAXE) {
			return true;
		}
		if (material == Material.DIAMOND_PICKAXE) {
			return true;
		}
		return false;
	}

	public static boolean isGoldOrAbovePickaxe(Material material) {
		if (material == Material.GOLD_PICKAXE) {
			return true;
		}
		if (material == Material.IRON_PICKAXE) {
			return true;
		}
		if (material == Material.DIAMOND_PICKAXE) {
			return true;
		}
		return false;
	}

	public static boolean isIronOrAbovePickaxe(Material material) {
		if (material == Material.IRON_PICKAXE) {
			return true;
		}
		if (material == Material.DIAMOND_PICKAXE) {
			return true;
		}
		return false;
	}

	public static boolean isDiamondPickaxe(Material material) {
		if (material == Material.DIAMOND_PICKAXE) {
			return true;
		}
		return false;
	}
	
	public static void giveRaceAbility(Plugin plugin, final Player player)
	{
		KaldenvoldPlayer k_player = com.jamesmonger.kaldenvold.KaldenvoldPlugin.playerList.get(player);
		
		String raceName = k_player.getRace().getName();
		if (raceName.equals("rheylin"))
		{
			startRaceEffect(plugin, player, k_player, PotionEffectType.WATER_BREATHING, 999999999);
		}
		else if (raceName.equals("fohriil"))
		{
			startRaceEffect(plugin, player, k_player, PotionEffectType.JUMP, 2);
		}
	}
	
	public static void startRaceEffect(Plugin plugin, final Player player, KaldenvoldPlayer k_player, final PotionEffectType type, final int intensity)
	{
		if (k_player.raceAbility != -1)
		{
			Bukkit.getScheduler().cancelTask(k_player.raceAbility);
		}
		
		k_player.raceAbility = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
			new Runnable()
			{
				public void run()
				{
					player.removePotionEffect(type);
					player.addPotionEffect(new PotionEffect(type, 999999999, intensity, true));
				}
			}, 0, 300
		);
	}
	
	public static void cancelRaceEffect(Player player, KaldenvoldPlayer k_player, final PotionEffectType type)
	{
		if (k_player.raceAbility != -1)
		{
			Bukkit.getScheduler().cancelTask(k_player.raceAbility);
		}
		
		player.removePotionEffect(type);
		k_player.raceAbility = -1;
	}
}