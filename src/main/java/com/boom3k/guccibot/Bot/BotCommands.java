package com.boom3k.guccibot.Bot;

import com.boom3k.guccibot.Models.TwitchStream;
import com.boom3k.guccibot.Util.Utilities;

import java.util.HashMap;
import java.util.Map;

public class BotCommands {
    static final Map<String, Command> commands = new HashMap<>();
    static String argument;

    private static final BotCommands INSTANCE = new BotCommands();

    BotCommands() {

        commands.put("about", event -> event.getMessage().getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(
                        "This Bot was developed by Boom3k.\n" +
                        "Source code can be found at https://github.com/boom3k/guccibot")).then());

        commands.put("twitch", event -> {
            return event.getMessage().getChannel()
                    .flatMap(messageChannel -> {
                        String message = "";
                        if (argument == null || argument == "") {
                            return messageChannel.createMessage(message + "argument cannot be empty");
                        }

                        if (Utilities.containsIllegalChars(argument)) {
                            return messageChannel.createMessage(message + "Provided username contains illegal characters, please try again.");
                        }

                        TwitchStream twitchStream = new TwitchStream(argument);
                        if(twitchStream.getUserName() == null){
                            message += "Twitch user ["+argument+"] not found. Please try again!";
                            return messageChannel.createMessage(message);
                        }
                        message += twitchStream.getUserName();

                        if (twitchStream.getStreamType() == null) {
                            message += " is currently offline.";
                            return messageChannel.createMessage(message);
                        }

                        message += "\nGame: " + twitchStream.getGame();
                        message += "\nCurrent Viewers: " + twitchStream.getViewers();
                        message += "\nStatus: " + twitchStream.getStreamType();
                        message += "\nStream Title: " + twitchStream.getStatus();
                        message += "\nhttps://www.twitch.tv/" + twitchStream.getUserName();
                        return messageChannel.createMessage(message);
                    }).then();
        });

        commands.put("commands", event -> {
            final String[] message = {"Available commands"};
            commands.entrySet().forEach(stringCommandEntry -> {
                message[0] += " :: " + stringCommandEntry.getKey();
            });
            return event.getMessage().
                    getChannel()
                    .flatMap(messageChannel ->
                            messageChannel.createMessage(message[0]).then());
        });


    }

    public static BotCommands getInstance() {
        return INSTANCE;
    }

    public static Map<String, Command> getCommands() {
        return commands;
    }

    public static String getArgument() {
        return argument;
    }

    public static void setArgument(String argument) {
        BotCommands.argument = argument;
    }


}
