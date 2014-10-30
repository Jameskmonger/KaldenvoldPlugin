package com.jamesmonger.kaldenvold.player.account;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.jamesmonger.kaldenvold.KaldenvoldPlugin;
import com.jamesmonger.kaldenvold.player.ChatType;
import com.jamesmonger.kaldenvold.player.KaldenvoldPlayer;

public class AccountManager {
	KaldenvoldPlugin plugin;

	public AccountManager(KaldenvoldPlugin plugin) {
		this.plugin = plugin;
	}

	public void loadAccount(Player p) throws IOException {
		KaldenvoldPlayer kp = new KaldenvoldPlayer(p, "none");
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(KaldenvoldPlayer.class,
				new KaldenvoldPlayerDeserializer());
		Gson gson = gsonBuilder.create();

		try (Reader reader = new InputStreamReader(new FileInputStream(
				plugin.getDataFolder() + "/users/" + p.getName() + ".json"))) {

			KaldenvoldPlayer _kp = gson
					.fromJson(reader, KaldenvoldPlayer.class);

			if (_kp != null)
				kp = _kp;
			
			KaldenvoldPlugin.playerList.put(p, kp);
		} catch (FileNotFoundException e) {
			createAccount(p);
			kp = KaldenvoldPlugin.playerList.get(p);
		}

		if (kp == null)
			p.kickPlayer("Error loading player - please try later.");

		loadPermissions(p);

		if (kp.getChatType() == ChatType.GLOBAL_OOC) {
			if (!KaldenvoldPlugin.globalOOC && !p.isOp()
					&& !p.hasPermission("kaldenvold.chat.globalooc")) {
				kp.setChatType(ChatType.LOCAL_OOC);
				p.sendMessage(ChatColor.GRAY
						+ "Global OOC disabled, you are now talking in the "
						+ ChatColor.WHITE + "Local OOC" + ChatColor.GRAY
						+ " channel");
			}
		}
	}

	public void saveAccount(Player p) throws IOException {
		KaldenvoldPlayer kp = KaldenvoldPlugin.playerList.get(p);
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(KaldenvoldPlayer.class,
						new KaldenvoldPlayerSerializer()).create();
		try (Writer writer = new FileWriter(plugin.getDataFolder() + "/users/"
				+ p.getName() + ".json")) {
			writer.write(gson.toJson(kp));
			writer.close();
		}
	}

	public void createAccount(Player p) throws IOException {
		KaldenvoldPlugin.playerList.put(p, new KaldenvoldPlayer(p, "NONE"));
		saveAccount(p);
	}

	public void loadPermissions(Player p) {
		PermissionUser user = PermissionsEx.getUser(p);
		KaldenvoldPlayer p_kaldenvold = KaldenvoldPlugin.playerList.get(p);
		for (String permission : p_kaldenvold.getRace().getPermissions()) {
			user.addPermission(permission);
		}
	}
}
