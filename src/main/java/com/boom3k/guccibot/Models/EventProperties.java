package com.boom3k.guccibot.Models;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildChannel;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.apache.log4j.Logger;

import java.util.Date;

public class EventProperties {


    Logger logger = Logger.getLogger(EventProperties.class);
    User user;
    Guild guild;
    GuildChannel guildChannel;
    Message message;
    String messageContent;
    Date timeStamp;
    Object eventAction;

    public EventProperties(MessageCreateEvent event, String eventAction) {
        this.user = event.getMessage().getAuthor().get();
        this.guild = event.getMessage().getGuild().block();
        this.guildChannel = guild.getChannelById(event.getMessage().getChannelId()).block();
        this.message = event.getMessage();
        this.messageContent = message.getContent().get();
        this.timeStamp = Date.from(message.getTimestamp());
        this.eventAction = eventAction;
    }

    public void printEventProperties() {

        String msg = "\n********* Message at " + timeStamp + "* *******";
        msg += "\nMessage content: " + this.messageContent + " (" + this.getEventAction() + ")";
        msg += "\nUser: " + this.user.getUsername() + " <" + this.user.getId() + ">";
        msg += "\nGuild : " + this.guild.getName() + " <" + this.guild.getId() + ">";
        msg += "\nChannel : " + this.guildChannel.getName() + " <" + this.guildChannel.getId() + ">";
        msg += "\n**************************************************\n";
        logger.info(msg);

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
