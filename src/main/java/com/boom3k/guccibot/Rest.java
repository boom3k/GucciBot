package com.boom3k.guccibot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Rest {

        final static String USER_AGENT = "Mozilla/5.0";

        // HTTP GET request
        static void sendGet() throws Exception {

            String discord_Client_ID = Bot.TOKENFILE.get("discord_client_id").getAsString();
            String url = "https://api.twitch.tv/kraken/streams/shroud?client_id=" + discord_Client_ID;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

           JsonObject response = getResponse(con);

           Twitch twitch = new Twitch(response);
           System.out.println(response);

        }

        // HTTP POST request

        static JsonObject getResponse(HttpURLConnection httpURLConnection) throws IOException {
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + httpURLConnection.getURL());
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return (JsonObject) new JsonParser().parse(response.toString()); }


}
