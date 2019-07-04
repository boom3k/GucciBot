package com.boom3k.guccibot.Models;

import com.boom3k.guccibot.Bot.BotClient;
import com.boom3k.guccibot.Util.Rest;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public abstract class TwitchUser {

    private String userName;

    public TwitchUser(String userName) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", BotClient.TOKENFILE.get("twitch_client_id").getAsString());
        JsonObject jsonObject = Rest.get("https://api.twitch.tv/kraken/users/" + userName, parameters);
        if(jsonObject == null){
            return;
        }
        this.userName = jsonObject.get("name").getAsString();
    }

    public String getUserName() {
        return userName;
    }

}
