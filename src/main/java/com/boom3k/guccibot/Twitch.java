package com.boom3k.guccibot;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Twitch {

    String userName;
    String game;
    int followers;
    String status;
    String streamType;
    int viewers;

    Twitch(JsonObject data) {
        try {
            JsonObject stream = data.getAsJsonObject("stream");
            JsonObject channel = stream.getAsJsonObject("channel");
            this.userName = channel.get("name").getAsString();
            this.game = channel.get("game").getAsString();
            this.followers = channel.get("followers").getAsInt();
            this.status = channel.get("status").getAsString();
            this.streamType = stream.get("stream_type").getAsString();
            this.viewers = stream.get("viewers").getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getUserName() {
        return userName;
    }

    public String getGame() {
        return game;
    }

    public int getFollowers() {
        return followers;
    }

    public String getStatus() {
        return status;
    }

    public String getStreamType() {
        return streamType;
    }

    public int getViewers() {
        return viewers;
    }

}