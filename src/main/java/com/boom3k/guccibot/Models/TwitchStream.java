package com.boom3k.guccibot.Models;

import com.boom3k.guccibot.Bot.BotClient;
import com.boom3k.guccibot.Util.Rest;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class TwitchStream extends TwitchUser {

    private String game;
    private int viewers;
    private int followers;
    private String streamType;
    private String status;
    private String previewImageUrl;

    public TwitchStream(String userName) {
        super(userName);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", BotClient.TOKENFILE.get("twitch_client_id").getAsString());
        JsonObject jsonObject = null;

        jsonObject = Rest.get("https://api.twitch.tv/kraken/streams/" + userName, parameters);

        if(jsonObject == null){
            return;
        }

        if (jsonObject.get("stream").toString().equalsIgnoreCase("null")) {
            return;
        }

        JsonObject stream = jsonObject.getAsJsonObject("stream");
        JsonObject channel = stream.getAsJsonObject("channel");
        this.game = stream.get("game").getAsString();
        this.viewers = stream.get("viewers").getAsInt();
        this.streamType = stream.get("stream_type").getAsString();
        this.status = channel.get("status").getAsString();
        this.followers = channel.get("followers").getAsInt();
        this.previewImageUrl = stream.getAsJsonObject("preview").get("large").getAsString();
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

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }
}