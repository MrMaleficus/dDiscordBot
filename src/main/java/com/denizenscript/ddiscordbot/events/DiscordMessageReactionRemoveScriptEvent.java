package com.denizenscript.ddiscordbot.events;

import com.denizenscript.ddiscordbot.DiscordScriptEvent;
import com.denizenscript.ddiscordbot.objects.*;
import com.denizenscript.denizencore.objects.ObjectTag;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class DiscordMessageReactionRemoveScriptEvent extends DiscordScriptEvent {
    public static DiscordMessageReactionRemoveScriptEvent instance;

    // <--[event]
    // @Events
    // discord message reaction removed
    //
    // @Regex ^on discord message reaction removed
    //
    // @Switch for:<bot> to only process the event for a specified Discord bot.
    // @Switch channel:<channel_id> to only process the event when it occurs in a specified Discord channel.
    // @Switch group:<group_id> to only process the event for a specified Discord group.
    //
    // @Triggers when a Discord user removes a reaction from a message.
    //
    // @Plugin dDiscordBot
    //
    // @Context
    // <context.bot> returns the relevant Discord bot object.
    // <context.channel> returns the channel.
    // <context.group> returns the group.
    // <context.message> returns the message.
    // <context.user> returns the user that removed the reaction.
    // <context.reaction> returns the old reaction.
    //
    // -->

    public MessageReactionRemoveEvent getEvent() {
        return (MessageReactionRemoveEvent) event;
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("discord message reaction removed");
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.checkSwitch("channel", getEvent().getChannel().getId())) {
            return false;
        }
        if (getEvent().isFromGuild() && !path.checkSwitch("group", getEvent().getGuild().getId())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("channel")) {
            return new DiscordChannelTag(botID, getEvent().getChannel());
        }
        else if (name.equals("group")) {
            if (getEvent().isFromGuild()) {
                return new DiscordGroupTag(botID, getEvent().getGuild());
            }
        }
        else if (name.equals("message")) {
            return new DiscordMessageTag(botID, getEvent().getChannel().getIdLong(), getEvent().getMessageIdLong());
        }
        else if (name.equals("reaction")) {
            return new DiscordReactionTag(botID, getEvent().getChannel().getIdLong(), getEvent().getMessageIdLong(), getEvent().getReaction());
        }
        else if (name.equals("user")) {
            return new DiscordUserTag(botID, getEvent().getUserIdLong());
        }
        return super.getContext(name);
    }

    @Override
    public String getName() {
        return "DiscordMessageReactionRemoved";
    }
}
