package com.jamesmonger.kaldenvold.player;

import org.bukkit.entity.Player;

import com.jamesmonger.kaldenvold.*;
import com.jamesmonger.kaldenvold.race.Race;
import com.jamesmonger.kaldenvold.race.RaceTypes;

public class KaldenvoldPlayer
{
	Race race;
	Player player;

	int humanUnderstanding = 0;
	int elfUnderstanding = 0;
	int orcUnderstanding = 0;
	int dwarfUnderstanding = 0;
	int rheylinUnderstanding = 0;
	
	public boolean ooc = false;
	public boolean autoTranslate = false;

	public String awaitingRaceClick = null;
	public int pendingEvent;
	public int raceAbility = -1;
	
	public boolean canUseAbility = true;

	public KaldenvoldPlayer(Player player)
	{
		this.player = player;
	}

	public KaldenvoldPlayer(Player player, String race)
	{
		this.player = player;

		this.race = KaldenvoldPlugin.raceList.get(race);
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Race getRace()
	{
		return this.race;
	}

	public void setRace(Race race)
	{
		this.race = race;
	}

	public void setRace(String race)
	{
		this.race = KaldenvoldPlugin.raceList.get(race);
	}

	public void setUnderstanding(RaceTypes type, int understanding)
	{
		switch (type)
		{
			case HUMAN:
				humanUnderstanding = understanding;
			break;
			case ELF:
				elfUnderstanding = understanding;
			break;
			case ORC:
				orcUnderstanding = understanding;
			break;
			case DWARF:
				dwarfUnderstanding = understanding;
			break;
			case RHEYLIN:
				rheylinUnderstanding = understanding;
			break;
			case SENTINEL:
				rheylinUnderstanding = understanding;
			break;
		}
	}

	public int getUnderstanding(RaceTypes type)
	{
		switch (type)
		{
			case NONE:
				return -1;
			case HUMAN:
				return humanUnderstanding;
			case ELF:
				return elfUnderstanding;
			case ORC:
				return orcUnderstanding;
			case DWARF:
				return dwarfUnderstanding;
			case RHEYLIN:
				return rheylinUnderstanding;
			case SENTINEL:
				return rheylinUnderstanding;
		}
		return 0;
	}

	public void increaseUnderstanding(RaceTypes type, int amount)
	{
		this.setUnderstanding(type, (getUnderstanding(type) + 1));
	}
}
