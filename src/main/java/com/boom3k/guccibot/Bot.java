package com.boom3k.guccibot;

import com.google.gson.JsonObject;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.*;
import org.apache.log4j.Logger;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Bot {

    static Logger logger = Logger.getLogger(Bot.class);
    final static JsonObject TOKENFILE = Utilities.getJsonObject(new File("token.json"));

    static void initializeClient() {

        DiscordClient client = new DiscordClientBuilder(TOKENFILE.get("token").getAsString()).build();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println("Logged in as: " + self.getUsername());
                });

        /**On Message Create Event Handler**/
        client.getEventDispatcher().on(MessageCreateEvent.class)
                /**Transform event -> into a messageContent Mono*/
                .flatMap(event -> {
                            User user = event.getMessage().getAuthor().get();
                            Guild guild = event.getMessage().getGuild().block();
                            GuildChannel channel = guild.getChannelById(event.getMessage().getChannelId()).block();

                            if (!event.getMessage().getContent().get().startsWith("!")) {
                                System.out.println("Ignoring message from [" + user.getUsername() + "]" +
                                        " in channel <"+guild.getName()+"."+channel.getName()+">");
                                return Mono.just("");
                            }
                            /**If message was sent by bot, return nothing**/
                            if (event.getMessage().getAuthor().get().isBot()) {
                                System.out.println("** Ignoring message from bot [" +
                                        event.getMessage().getAuthor().get().getUsername()
                                        + "]\n");
                                return Mono.just("");

                            }


                            System.out.println("* Step 1:: Message author == " + user.getUsername());
                            /**Return a mono**/
                            return Mono.justOrEmpty(event.getMessage().getContent())
                                    /**Transform messageContent Mono -> Flux Stream**/
                                    .flatMap(messageContent -> {
                                        System.out.println("** Step 2:: MessageContent == " + messageContent);

                                        /**Return a Flux**/
                                        return Flux
                                                /**set Flux to iterate through BotCommand Entries**/
                                                .fromIterable(BotCommands.getCommands().entrySet())

                                                /**Foreach BotCommand Entry..**/
                                                .filter(currentEntry -> {
                                                    System.out.println("*** Step 3:: Searching for command == " + currentEntry.getKey());
                                                    /**If the messageContent starts with '!{currentEntry.key}'**/
                                                    return messageContent.startsWith('!' + currentEntry.getKey());
                                                })
                                                /**Return the Value of the BotCommands Key**/
                                                .flatMap(entry -> {
                                                    System.out.println("*** Step 3:: messageContent matches [" + entry.getKey() + "] command!");
                                                    String argument = messageContent.replace("!"+ entry.getKey(),"").stripLeading().stripTrailing();
                                                    BotCommands.setArgument(argument);
                                                    /****/
                                                    System.out.println("*** Passing [" + entry.getKey() + ".command] " +
                                                            " to <" + guild.getName() + "." + channel.getName() + ">");

                                                    return entry.getValue().execute(event);
                                                })
                                                /****/
                                                .next();
                                    });
                        }

                )/****/
                .subscribe();

        client.login().block();

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





















