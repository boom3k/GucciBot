package com.boom3k.guccibot.Models;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildChannel;
import discord4j.core.object.entity.User;

public class EventProperties {

    User user;
    Guild guild;
    GuildChannel guildChannel;
    String messageContent;
    Object eventAction;

    public EventProperties(MessageCreateEvent event, String eventAction) {
        this.user = event.getMessage().getAuthor().get();
        this.guild = event.getMessage().getGuild().block();
        this.guildChannel = guild.getChannelById(event.getMessage().getChannelId()).block();
        this.messageContent = event.getMessage().getContent().get();
        this.eventAction = eventAction;
    }

    public void printEventProperties(){
        System.out.println("Message content: " + this.messageContent);
        System.out.println("Message Action: " + this.eventAction);
        System.out.println("User Name:" + this.user.getUsername());
        System.out.println("User ID: " + this.user.getId());
        System.out.println("Guild Name: " + this.guild.getName());
        System.out.println("Guild ID: " + this.guild.getId());
        System.out.println("Channel Name: " + this.guildChannel.getName());
        System.out.println("Channel ID: " + this.guildChannel.getId() +"\n");
    }

    public User getUser() {
        return user;
    }

    public Guild getGuild() {
        return guild;
    }

    public GuildChannel getGuildChannel() {
        return guildChannel;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public Object getEventAction() {
        return eventAction;
    }

    public void setEventAction(Object eventAction) {
        this.eventAction = eventAction;
    }
}
