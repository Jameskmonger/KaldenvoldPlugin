package com.jamesmonger.kaldenvold.player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.jamesmonger.kaldenvold.KaldenvoldPlugin;
import com.jamesmonger.kaldenvold.race.RaceTypes;

public class AccountManager
{
	KaldenvoldPlugin plugin;

	public AccountManager(KaldenvoldPlugin plugin)
	{
		this.plugin = plugin;
	}

	public void LoadAccount(Player p)
	{
		try
		{
			File file = new File(plugin.getDataFolder() + "/users/",
					p.getName() + ".txt");
			if (!file.isFile())
			{
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				fos.flush();
				fos.close();

				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write("race:none");
				bw.newLine();
				bw.write("humanU:0");
				bw.newLine();
				bw.write("elfU:0");
				bw.newLine();
				bw.write("orcU:0");
				bw.newLine();
				bw.write("dwarfU:0");
				bw.newLine();
				bw.write("rheylinU:0");
				bw.flush();
				bw.close();
			}

			BufferedReader br = new BufferedReader(new FileReader(file));
			String l;
			KaldenvoldPlayer k_player = new KaldenvoldPlayer(p);
			KaldenvoldPlugin.playerList.put(p, k_player);
			
			k_player.pendingEvent = -1;
			
			while ((l = br.readLine()) != null)
			{
				String[] tokens = l.split(":", 2);
				if (tokens[0].equals("race"))
				{
					if (tokens.length != 2)
						continue;
					if (tokens[1] != null)
					{
						k_player.setRace(tokens[1]);
					}
					else
					{
						k_player.setRace("none");
					}
				}
				if (tokens[0].equals("humanU"))
				{
					if (tokens.length != 2)
						continue;

					try
					{
						int understanding = Integer.parseInt(tokens[1]);
						k_player.setUnderstanding(RaceTypes.HUMAN,
								understanding);
					}
					catch (Exception e)
					{
						k_player.setUnderstanding(RaceTypes.HUMAN, 0);
					}
				}
				if (tokens[0].equals("elfU"))
				{
					if (tokens.length != 2)
						continue;

					try
					{
						int understanding = Integer.parseInt(tokens[1]);
						k_player.setUnderstanding(RaceTypes.ELF, understanding);
					}
					catch (Exception e)
					{
						k_player.setUnderstanding(RaceTypes.ELF, 0);
					}
				}
				if (tokens[0].equals("orcU"))
				{
					if (tokens.length != 2)
						continue;

					try
					{
						int understanding = Integer.parseInt(tokens[1]);
						k_player.setUnderstanding(RaceTypes.ORC, understanding);
					}
					catch (Exception e)
					{
						k_player.setUnderstanding(RaceTypes.ORC, 0);
					}
				}
				if (tokens[0].equals("dwarfU"))
				{
					if (tokens.length != 2)
						continue;

					try
					{
						int understanding = Integer.parseInt(tokens[1]);
						k_player.setUnderstanding(RaceTypes.DWARF,
								understanding);
					}
					catch (Exception e)
					{
						k_player.setUnderstanding(RaceTypes.DWARF, 0);
					}
				}
				if (tokens[0].equals("rheylinU"))
				{
					if (tokens.length != 2)
						continue;

					try
					{
						int understanding = Integer.parseInt(tokens[1]);
						k_player.setUnderstanding(RaceTypes.RHEYLIN,
								understanding);
					}
					catch (Exception e)
					{
						k_player.setUnderstanding(RaceTypes.RHEYLIN, 0);
					}
				}
			}
			br.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		LoadPermissions(p);
	}

	public void SaveAccount(Player p)
	{
		KaldenvoldPlayer p_kaldenvold = KaldenvoldPlugin.playerList.get(p);
		try
		{
			File file = new File(plugin.getDataFolder() + "/users/",
					p.getName() + ".txt");
			if (!file.exists())
			{
				FileOutputStream fos = new FileOutputStream(file);
				fos.flush();
				fos.close();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("race:" + p_kaldenvold.getRace().getName());
			bw.newLine();
			bw.write("humanU:" + p_kaldenvold.getUnderstanding(RaceTypes.HUMAN));
			bw.newLine();
			bw.write("elfU:" + p_kaldenvold.getUnderstanding(RaceTypes.ELF));
			bw.newLine();
			bw.write("orcU:" + p_kaldenvold.getUnderstanding(RaceTypes.ORC));
			bw.newLine();
			bw.write("dwarfU:" + p_kaldenvold.getUnderstanding(RaceTypes.DWARF));
			bw.newLine();
			bw.write("rheylinU:"
					+ p_kaldenvold.getUnderstanding(RaceTypes.RHEYLIN));
			bw.flush();
			bw.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public void LoadPermissions(Player p)
	{
		PermissionUser user = PermissionsEx.getUser(p);
		KaldenvoldPlayer p_kaldenvold = KaldenvoldPlugin.playerList.get(p);
		for(String permission : p_kaldenvold.getRace().getPermissions())
		{
			user.addPermission(permission);
		}
	}
}
