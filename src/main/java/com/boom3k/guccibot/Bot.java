package com.boom3k.guccibot;

import com.google.gson.JsonObject;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import org.apache.log4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Bot {

    static Logger logger = Logger.getLogger(Bot.class);
    final static JsonObject TOKENFILE = getTokenFile();
    private static final Map<String, Command> commands = new HashMap<>();

    static {



        Command command;
        commands.put("this", event -> event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage("dick!"))
                .then());

        commands.put("dog", event -> event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage("Streaming!"))
                .then());
    }


    static void initializeClient() {

        DiscordClient client = new DiscordClientBuilder(TOKENFILE.get("token").getAsString()).build();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println("Logged in as: " + self.getUsername());
                });

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(event ->
                        Mono.justOrEmpty(event.getMessage().getContent())
                                .flatMap(content -> Flux.fromIterable(commands.entrySet())
                                        .filter(entry -> content.startsWith('!' + entry.getKey()))//Command set as "!"
                                        .flatMap(entry -> entry.getValue().execute(event))
                                        .next())).subscribe();

        client.login().block();

    }


    static JsonObject getTokenFile() {
        JsonObject tokenJson = null;
        try {
            tokenJson = Utilities.getJsonObject(new File("token.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tokenJson;
    }

    static void logEverything(DiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class) // This listens for all events that are of MessageCreateEvent
                .subscribe(event ->
                        event.getMessage()
                                .getContent()
                                .ifPresent(c ->
                                        logger.info(event.getMessage().getAuthor().get().getUsername() + ": " + c)
                                )
                );// "subscribe" is the method you need to call to actually make sure that it's doing something.

    }

}





















