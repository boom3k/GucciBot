package com.boom3k.guccibot.Util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.Map;

public class Rest {

    final static String USER_AGENT = "Mozilla/5.0";

    // HTTP GET request
    public static JsonObject sendGet(String url, Map<String, String> parameters) throws Exception {

        if(parameters != null) {
            //Step through map and get key and values to append to url
            for (int i = 0; i < parameters.keySet().size(); i++) {
                String currentKey = (String) parameters.keySet().toArray()[i];
                url += "?" + currentKey + "=" + parameters.get(currentKey);
            }
        }

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        return getResponse(con);
    }

    static JsonObject getResponse(HttpURLConnection httpURLConnection) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + httpURLConnection.getURL());
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return (JsonObject) new JsonParser().parse(response.toString());
    }
}
