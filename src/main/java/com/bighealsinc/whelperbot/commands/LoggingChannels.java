package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.listeners.CommandListener;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.channel.Category;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.util.EntityUtil;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class LoggingChannels extends CommandListener implements Command {

    @Override
    public String getName() {
        return "createloggingchannels";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        Snowflake readOnlyChannels = findChannelId(event, "Read Only Channels");
        Snowflake memberJoin = findChannelId(event, "member-join");
        Snowflake memberDeparture = findChannelId(event, "member-departures");
        if (readOnlyChannels == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Please run command createreadonlycategory first.");
        }
        if (memberJoin == null) {
            event.getClient().getGuildById(Snowflake.of(1028801665492602880L))
                    .flatMap(guild -> guild.createTextChannel("member-join")
                                    .withParentId(readOnlyChannels)
//                            .withPermissionOverwrites(PermissionOverwrite.forRole(
//                                    guild.getEveryoneRole().block().getId(),
//                                    PermissionSet.none(),
//                                    PermissionSet.of(Permission.SEND_MESSAGES, Permission.ADD_REACTIONS)))
                    ).subscribe();
        }

        if (memberDeparture == null) {
            event.getClient().getGuildById(Snowflake.of(1028801665492602880L))
                    .flatMap(guild -> guild.createTextChannel("member-departures")
                                    .withParentId(readOnlyChannels)
//                            .withPermissionOverwrites(PermissionOverwrite.forRole(
//                                    guild.getEveryoneRole().block().getId(),
//                                    PermissionSet.none(),
//                                    PermissionSet.of(Permission.SEND_MESSAGES, Permission.ADD_REACTIONS)))
                    ).subscribe();

        }

        return event.reply()
                .withEphemeral(true)
                .withContent("Channels have been created.");
    }
}
