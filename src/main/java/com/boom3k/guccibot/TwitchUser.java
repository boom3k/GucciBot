package com.boom3k.guccibot;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class TwitchUser {
    private String userName;
    private String bio;

    public TwitchUser(String userName) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", Bot.getTokenFile().get("twitch_client_id").getAsString());
        JsonObject jsonObject = null;
        try {
            jsonObject = Rest.sendGet("https://api.twitch.tv/kraken/users/" + userName, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.userName = jsonObject.get("name").getAsString();
        this.bio = jsonObject.get("bio").getAsString();
    }

    public String getUserName() {
        return userName;
    }

    public String getBio() {
        return bio;
    }


}
