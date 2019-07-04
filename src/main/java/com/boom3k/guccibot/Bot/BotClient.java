package com.boom3k.guccibot.Bot;

import com.boom3k.guccibot.Models.EventProperties;
import com.boom3k.guccibot.Util.Utilities;
import com.google.gson.JsonObject;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildChannel;
import discord4j.core.object.entity.User;
import org.apache.log4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

public class BotClient {

    static Logger logger = Logger.getLogger(BotClient.class);
    public final static JsonObject TOKENFILE = Utilities.getJsonObject(new File("token.json"));

    public static void initializeClient() {


        DiscordClient client = new DiscordClientBuilder(TOKENFILE.get("token").getAsString()).build();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    logger.info("Logged in as: " + self.getUsername());
                });

        //client.getEventDispatcher().on().subscribe();

        /**On Message Create Event Handler**/
        client.getEventDispatcher().on(MessageCreateEvent.class)
                /**Transform event -> into a messageContent Mono*/
                .flatMap(event -> {
                    EventProperties eventPropeties = new EventProperties(event, "Event initialized");

                    /**If message doesn't start with a command, ignore**/
                    if (!event.getMessage().getContent().get().startsWith("!")) {
                        logger.info(eventPropeties.getUser().getUsername() + ": "
                                + eventPropeties.getMessageContent());
                        return ignore();
                    }

                    /**If message was sent by bot, ignore**/
                    if (eventPropeties.getUser().isBot()) {
                        logger.info("** Ignoring message from bot [" + eventPropeties.getUser().getUsername() + "]");
                        return ignore();
                    }

                    /**Return a mono**/
                    return Mono.justOrEmpty(event.getMessage().getContent())
                            /**Transform messageContent Mono -> Flux Stream**/
                            .flatMap(messageContent -> {
                                /**Return a Flux**/
                                return Flux
                                        /**set Flux to iterate through BotCommand Entries**/
                                        .fromIterable(BotCommands.getCommands().entrySet())

                                        /**Foreach BotCommand Entry..**/
                                        .filter(currentEntry -> {
                                            /**If the messageContent starts with '!{currentEntry.key}'**/
                                            return messageContent.startsWith('!' + currentEntry.getKey());
                                        })
                                        /**Return the Value of the BotCommands Key**/
                                        .flatMap(entry -> {
                                            String argument = messageContent.replace("!" + entry.getKey(), "").stripLeading().stripTrailing();
                                            BotCommands.setArgument(argument);
                                            eventPropeties.setEventAction("[Command: " + entry.getKey() + "] " +
                                                    "[Argument(s): " + argument + "]");
                                            eventPropeties.printEventProperties();
                                            return entry.getValue().execute(event);
                                        })
                                        /****/
                                        .next();
                            });
                })/****/
                .subscribe();
                /**:*/
        client.login().block();

    }

    static Mono ignore() {
        return Mono.just("");
    }

    static void messageCreateEventToJson(MessageCreateEvent event) {
        JsonObject jsonObject = new JsonObject();
        User user = event.getMessage().getAuthor().get();
        Guild guild = event.getMessage().getGuild().block();
        GuildChannel channel = guild.getChannelById(event.getMessage().getChannelId()).block();
        String messageString = event.getMessage().getContent().get();

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





















