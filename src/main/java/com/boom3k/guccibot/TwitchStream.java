package com.boom3k.guccibot;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class TwitchStream extends TwitchUser {

    private String game;
    private int viewers;
    private int followers;
    private String streamType;
    private String status;

    TwitchStream(String userName) {
        super(userName);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", Bot.TOKENFILE.get("twitch_client_id").getAsString());
        JsonObject jsonObject = null;

        try {
            jsonObject = Rest.sendGet("https://api.twitch.tv/kraken/streams/" + userName, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.get("stream").toString().equalsIgnoreCase("null")) {
            System.out.println(userName + " is not currently streaming!");
            return;
        }

        JsonObject stream = jsonObject.getAsJsonObject("stream");
        JsonObject channel = stream.getAsJsonObject("channel");
        this.game = stream.get("game").getAsString();
        this.viewers = stream.get("viewers").getAsInt();
        this.streamType = stream.get("stream_type").getAsString();
        this.status = channel.get("status").getAsString();
        this.followers = channel.get("followers").getAsInt();
    }

    public String getGame() {
        return game;
    }

    public int getViewers() {
        return viewers;
    }

    public String getStreamType() {
        return streamType;
    }

    public String getStatus() {
        return status;
    }

    public int getFollowers() {
        return followers;
    }
}