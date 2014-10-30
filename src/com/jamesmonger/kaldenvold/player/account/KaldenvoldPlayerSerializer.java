package com.jamesmonger.kaldenvold.player.account;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.craftbukkit.libs.com.google.gson.*;

import com.jamesmonger.kaldenvold.player.KaldenvoldPlayer;
import com.jamesmonger.kaldenvold.race.RaceType;

public class KaldenvoldPlayerSerializer implements
		JsonSerializer<KaldenvoldPlayer> {

	@Override
	public JsonElement serialize(KaldenvoldPlayer kp, Type arg1,
			JsonSerializationContext arg2) {
		final JsonObject obj = new JsonObject();
		obj.add("race", new JsonPrimitive(kp.getRace().getName().toUpperCase()));
		obj.add("understandings", understandings(kp));
		obj.add("chat_type", new JsonPrimitive(kp.getChatType()
				.getNumericValue()));
		return obj;
	}
	
	public JsonObject understandings(KaldenvoldPlayer kp) {
		JsonObject understandings = new JsonObject();

		Iterator<Entry<RaceType, Integer>> it = kp.languageUnderstanding
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<RaceType, Integer> pairs = (Map.Entry<RaceType, Integer>) it
					.next();

			understandings.add(pairs.getKey().name().toUpperCase(),
					new JsonPrimitive(pairs.getValue()));
			it.remove();
		}

		return understandings;
	}
}
