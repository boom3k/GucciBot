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

public class Bot {

    static Logger logger = Logger.getLogger(Bot.class);
    final static JsonObject TOKENFILE = getTokenFile();

    static void initializeClient() {
        DiscordClient client = new DiscordClientBuilder(TOKENFILE.get("token").getAsString()).build();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println("Logged in as: " + self.getUsername());
                });

        respond(client, "!dog", "https://www.mail-signatures.com/wp-content/uploads/2019/02/How-to-find-direct-link-to-image_Blog-Picture.png");

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

    static void respond(DiscordClient client, String incoming, String response) {
        client.getEventDispatcher()
                .on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().orElse("").equalsIgnoreCase(incoming))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(response))
                .subscribe();
    }

    static void pingToPong(DiscordClient client) {
        client.getEventDispatcher()
                /**This listens for all MessageCreateEvents that come in to the bot.*/
                .on(MessageCreateEvent.class)

                /**This turns all MessageCreate events into the messages that were sent.
                 * The :: syntax is just shorthand for map(event -> event.getMessage()).*/
                .map(MessageCreateEvent::getMessage)

                /**This filters out all members that are bots, so that we only get events from users.*/
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))

                /**This filters out any messages that don't equal the content !ping.*/
                .filter(message -> message.getContent().orElse("").equalsIgnoreCase("!ping"))

                /**This turns it into the channel the message came from.*/
                .flatMap(Message::getChannel)

                /**This creates the message with the content Pong!*/
                .flatMap(channel -> channel.createMessage("Pong!"))

                /**And this tells it to execute!*/
                .subscribe();

    }

    static void logEverything(DiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class) // This listens for all events that are of MessageCreateEvent
                .subscribe(event ->
                        event.getMessage()
                                .getContent()
                                .ifPresent(c ->
                                        logger.info(event.getMessage().getAuthor().get().getUsername() + ": " + c)));// "subscribe" is the method you need to call to actually make sure that it's doing something.

    }


}





















