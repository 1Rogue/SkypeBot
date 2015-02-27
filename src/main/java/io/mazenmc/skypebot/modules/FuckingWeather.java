package io.mazenmc.skypebot.modules;

import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.skype.ChatMessage;

public class FuckingWeather implements Module {

	//Imperial because fuck Celsius right?
	private static String url = "http://api.openweathermap.org/data/2.5/weather?units=imperial&q=";

	@Command(name = "fuckingweather")
	public static void cmdFuckingWeather(ChatMessage chat, String location) {
		try {
			Resource.sendMessage(chat, getWeather(location));
		} catch (Exception e) {
			Resource.sendMessage(chat, "THE FUCKING WEATHER MODULE FAILED FUCK!");
			e.printStackTrace();
		}
	}

	public static String getWeather(String location) throws JSONException, Exception {
		if (location == null) {
			throw new IllegalArgumentException("location cannot be null");
		}
		String call = (url + location).replace(' ', '+');
		JSONObject json = new JSONObject(sendGet(call));

		if (json == null || json.getInt("cod") != 200) {
			return "I CAN'T GET THE FUCKING WEATHER!";
		}

		double temp = json.getJSONObject("main").getDouble("temp");
		String city = json.getString("name");
		if (city == null || city.isEmpty()) {
			city = location;
		}
		double metric = temp - 32 / 1.8000;
		return String.format("THE FUCKING WEATHER IN %s IS %.2fF | %.2fC!", city.toUpperCase(), temp, metric);
	}

	public static String sendGet(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

}
