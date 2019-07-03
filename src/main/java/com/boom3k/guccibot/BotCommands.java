package com.boom3k.guccibot;

import java.util.HashMap;
import java.util.Map;

public class BotCommands {
    static final Map<String, Command> commands = new HashMap<>();
    static String argument;

    public static Map<String, Command> getCommands() {
        return commands;
    }

    public static String getArgument() {
        return argument;
    }

    public static void setArgument(String argument) {
        BotCommands.argument = argument;
    }

    static {
        commands.put("this", event -> event.getMessage().getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage("dick!")).then());

        commands.put("dog", event -> event.getMessage().getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage("Streaming!")).then());

        commands.put("cat", event -> event.getMessage().getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage("meow")).then());


        commands.put("twitch", event -> {
            return event.getMessage().getChannel()
                    .flatMap(messageChannel -> {
                        if(argument == null){
                            return messageChannel.createMessage("!twitch argument cannot be empty");
                        }
                        TwitchStream twitchStream = new TwitchStream(argument);
                        String message = "";
                        message += twitchStream.getUserName();
                        if (twitchStream.getStreamType() != null) {
                            message += "\nGame: " + twitchStream.getGame();
                            message += "\nCurrent Viewers: " + twitchStream.getViewers();
                            message += "\nStatus: " + twitchStream.getStreamType();
                            message += "\nStream Title: " + twitchStream.getStatus();
                            message+="\nhttps://www.twitch.tv/" + twitchStream.getUserName();
                        } else {
                            message += " is currently offline.";
                        }

                        return messageChannel.createMessage(message);
                    }).then();
        });
    }
}
