package com.jamesmonger.kaldenvold.player;

public enum ChatType {
	LOCAL_IC(0, "Local IC"), LOCAL_OOC(1, "Local OOC"), GLOBAL_OOC(2, "Global OOC");
	
	private int numeric;
	private String name;
	
	ChatType(int numeric, String name)
	{
		this.numeric = numeric;
		this.name = name;
	}
	
	public int getNumericValue()
	{
		return numeric;
	}
	
	public String getName()
	{
		return name;
	}
	
	public static ChatType forId(int id)
	{
		for (ChatType t : ChatType.values()) 
		{
			if (t.getNumericValue() == id)
				return t;
		}
		return LOCAL_IC;
	}
}
