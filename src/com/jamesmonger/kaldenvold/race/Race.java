package com.jamesmonger.kaldenvold.race;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import com.jamesmonger.kaldenvold.*;

public class Race
{
	String literalName;
	String displayName;
	RaceType type;
	ArrayList<String> permissions;
	KaldenvoldPlugin plugin;
	
	double homeX;
	double homeY;
	double homeZ;
	float homeRot;

	public Race(KaldenvoldPlugin plugin, String literalName, String displayName, RaceType type, double homeX, double homeY, double homeZ, float homeRot)
	{
		this.plugin = plugin;
		this.literalName = literalName;
		this.displayName = displayName;
		this.type = type;
		
		this.homeX = homeX;
		this.homeY = homeY;
		this.homeZ = homeZ;
		this.homeRot = homeRot;
		
		permissions = new ArrayList<String>();
		
		loadPermissions();

		KaldenvoldPlugin.raceList.put(literalName, this);
	}
	
	public ArrayList<String> getPermissions()
	{
		return permissions;
	}

	private void loadPermissions()
	{
		try
		{
			File file = new File(plugin.getDataFolder() + "/permissions/",
					literalName + ".txt");
			if (!file.isFile())
			{
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				fos.flush();
				fos.close();
			}

			BufferedReader br = new BufferedReader(new FileReader(file));
			String l;
			
			while ((l = br.readLine()) != null)
			{
				permissions.add(l);
			}
			
			br.close();
		}
		catch (Exception e) {}
	}

	public boolean sharesLanguage(Race other)
	{
		if (this.type == other.type)
			return true;

		if ((this.type == RaceType.RHEYLIN && other.type == RaceType.SENTINEL)
				|| (this.type == RaceType.SENTINEL && other.type == RaceType.RHEYLIN))
			return true;

		return false;
	}
	
	public Location getHome(World world)
	{
		return new Location(world, homeX, homeY, homeZ);
	}
	
	public float getHomeRotation()
	{
		return homeRot;
	}

	public String getDisplayName()
	{
		return this.displayName;
	}

	public String getName()
	{
		return this.literalName;
	}

	public RaceType getType()
	{
		return this.type;
	}

	public ChatColor getColor()
	{
		switch (this.type)
		{
			case HUMAN:
				return ChatColor.YELLOW;

			case ELF:
				return ChatColor.BLUE;

			case ORC:
				return ChatColor.DARK_GREEN;

			case DWARF:
				return ChatColor.GOLD;

			case SENTINEL:
			case RHEYLIN:
				return ChatColor.AQUA;

			default:
				return ChatColor.WHITE;
		}
	}
}
