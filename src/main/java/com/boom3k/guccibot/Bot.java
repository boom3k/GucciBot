package com.boom3k.guccibot;

import com.google.gson.JsonObject;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bot {

    Logger logger = Logger.getLogger(Bot.class);

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        System.setProperty("appname", Bot.class.getName());
        System.setProperty("current.date.time", dateFormat.format(new Date()));
    }

    public static void main(String[] args) throws FileNotFoundException {

        DiscordClient bot = initializeClient();



    }

    static DiscordClient initializeClient() throws FileNotFoundException {
        JsonObject tokenJson = Utilities.getJsonObject(new File("token.json"));
        DiscordClientBuilder builder = new DiscordClientBuilder(tokenJson.get("token").getAsString());
        DiscordClient client = builder.build();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println("Logged in as: " + self.getUsername());
                });

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().orElse("").equalsIgnoreCase("!ping"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Pong!"))
                .subscribe();

        client.login().block();
        return client;
    }


}





















