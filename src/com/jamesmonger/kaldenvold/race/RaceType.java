package com.jamesmonger.kaldenvold.race;

public enum RaceType
{
	NONE, HUMAN, ELF, ORC, DWARF, RHEYLIN, SENTINEL;
	
	public static RaceType forName(String name) {
		for (RaceType r : RaceType.values()) {
			if (r.name().toLowerCase().equals(name.toLowerCase()))
				return r;
		}
		return NONE;
	}
}
