package com.boom3k.guccibot.Util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Rest {

    final static String USER_AGENT = "Mozilla/5.0";
    static Logger logger = Logger.getLogger(Rest.class);

    // HTTP GET request
    public static JsonObject get(String url, Map<String, String> parameters) {

        if (parameters != null) {
            //Step through map and get key and values to append to url
            for (int i = 0; i < parameters.keySet().size(); i++) {
                String currentKey = (String) parameters.keySet().toArray()[i];
                url += "?" + currentKey + "=" + parameters.get(currentKey);
            }
        }
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            return getResponse(con);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    static JsonObject getResponse(HttpURLConnection httpURLConnection) {
        String responseMessage = null;
        int responseCode = 0;
        try {
            responseMessage = httpURLConnection.getResponseMessage();
            responseCode = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(responseCode + " [" + responseMessage + "] :: {"+httpURLConnection.getURL()+"}");
        if(responseCode >399){
            return null;
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return (JsonObject) new JsonParser().parse(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
