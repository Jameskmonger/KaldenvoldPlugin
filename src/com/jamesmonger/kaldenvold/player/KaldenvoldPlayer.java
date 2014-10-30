package com.jamesmonger.kaldenvold.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.jamesmonger.kaldenvold.*;
import com.jamesmonger.kaldenvold.race.Race;
import com.jamesmonger.kaldenvold.race.RaceType;

public class KaldenvoldPlayer
{
	private Race race;
	Player player;
	
	public Map<RaceType, Integer> languageUnderstanding;
	
	private ChatType chatType;
	
	public boolean autoTranslate = false;

	public String awaitingRaceClick = null;
	public int pendingEvent;
	public int raceAbility = -1;
	
	public boolean canUseAbility = true;
	
	public KaldenvoldPlayer()
	{
		languageUnderstanding = new HashMap<RaceType, Integer>();
		
		this.chatType = ChatType.LOCAL_IC;
	}

	public KaldenvoldPlayer(Player player)
	{
		languageUnderstanding = new HashMap<RaceType, Integer>();
		
		this.player = player;
		this.chatType = ChatType.LOCAL_IC;
	}

	public KaldenvoldPlayer(Player player, String race)
	{
		languageUnderstanding = new HashMap<RaceType, Integer>();
		
		this.player = player;
		this.race = KaldenvoldPlugin.raceList.get(race);
		this.chatType = ChatType.LOCAL_IC;
		
		for (RaceType r : RaceType.values())
			this.setUnderstanding(r, 0);
		
		this.setUnderstanding(RaceType.HUMAN, 423);
		this.setUnderstanding(RaceType.ELF, 50);
	}

	public Player getPlayer()
	{
		return this.player;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
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

	public void setUnderstanding(RaceType type, int understanding)
	{
		if (languageUnderstanding.containsKey(type))
			languageUnderstanding.remove(type);
		
		languageUnderstanding.put(type, understanding);
	}

	public int getUnderstanding(RaceType type)
	{
		if (type == RaceType.NONE)
			return -1;
		
		return languageUnderstanding.get(type);
	}

	public void increaseUnderstanding(RaceType type, int amount)
	{
		this.setUnderstanding(type, (getUnderstanding(type) + 1));
	}
	
	public void setChatType(ChatType type)
	{
		this.chatType = type;
	}
	
	public ChatType getChatType()
	{
		return this.chatType;
	}
}