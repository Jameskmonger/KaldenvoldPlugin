package com.jamesmonger.kaldenvold.player.account;

import java.lang.reflect.Type;
import java.util.Map;

import org.bukkit.craftbukkit.libs.com.google.gson.*;

import com.jamesmonger.kaldenvold.KaldenvoldPlugin;
import com.jamesmonger.kaldenvold.player.ChatType;
import com.jamesmonger.kaldenvold.player.KaldenvoldPlayer;
import com.jamesmonger.kaldenvold.race.Race;
import com.jamesmonger.kaldenvold.race.RaceType;

public class KaldenvoldPlayerDeserializer implements JsonDeserializer<KaldenvoldPlayer> {

	@Override
	public KaldenvoldPlayer deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		final JsonObject obj = json.getAsJsonObject();
		
		KaldenvoldPlayer kp = new KaldenvoldPlayer();
		
		String raceString = obj.get("race").getAsString().toLowerCase();
		Race race = KaldenvoldPlugin.raceList.get(raceString);
		kp.setRace(race);
		
		
		
		JsonObject understandings = obj.get("understandings").getAsJsonObject();
		
		for (Map.Entry<String, JsonElement> entry : understandings.entrySet()) {
			RaceType racetype = RaceType.forName(entry.getKey());
			kp.setUnderstanding(racetype, entry.getValue().getAsInt());
		}
		
		kp.setChatType(ChatType.forId(obj.get("chat_type").getAsInt()));
		
		return kp;
	}
}