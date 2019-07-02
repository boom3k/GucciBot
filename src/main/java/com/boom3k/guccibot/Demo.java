package com.boom3k.guccibot;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;

public class Demo {

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

}
