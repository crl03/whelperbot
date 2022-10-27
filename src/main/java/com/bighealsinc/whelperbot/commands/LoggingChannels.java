package com.bighealsinc.whelperbot.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.channel.Category;
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
public class LoggingChannels implements Command {

    @Override
    public String getName() {
        return "createloggingchannels";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {

        HashMap<Long, String> channelList = new HashMap<>();
        List<GuildChannel> fluxChannelList = event.getClient().getGuildChannels(Snowflake.of(1028801665492602880L))
                        .collectList().block();

        for (GuildChannel channel : fluxChannelList) {
            channelList.put(channel.getId().asLong(), channel.getName());
        }
//***********************************
//        if (!channelList.containsValue("Read Only Channels")) {
//            event.getClient().getGuildById(Snowflake.of(1028801665492602880L))
//                    .flatMap(guild -> guild.createCategory("Read Only Channels")
//                            .withPermissionOverwrites(PermissionOverwrite.forRole(
//                                    guild.getEveryoneRole().block().getId(),
//                                    PermissionSet.none(),
//                                    PermissionSet.of(Permission.SEND_MESSAGES, Permission.ADD_REACTIONS)
//                            )))
//                    .subscribe();
//        }
//
//        long readOnlyChannelsId = 0;
//        event.getClient().getGuildChannels(Snowflake.of(1028801665492602880L))
//                .filter(channel -> channel.getName().equalsIgnoreCase("Read Only Channels"))
//                .filter(channel -> channel.getId().asLong() == readOnlyChannelsId)
//                .subscribe();
//***********************************

        if (!channelList.containsValue("member-join")) {
            event.getClient().getGuildById(Snowflake.of(1028801665492602880L))
                    .flatMap(guild -> guild.createTextChannel("member-join")
                            .withPermissionOverwrites(PermissionOverwrite.forRole(
                                    guild.getEveryoneRole().block().getId(),
                                    PermissionSet.none(),
                                    PermissionSet.of(Permission.SEND_MESSAGES, Permission.ADD_REACTIONS)
                            ))).subscribe();
        }



        if (!channelList.containsValue("member-departures")) {
            event.getClient().getGuildById(Snowflake.of(1028801665492602880L))
                    .flatMap(guild -> guild.createTextChannel("member-departures")
                            .withPermissionOverwrites(PermissionOverwrite.forRole(
                                    guild.getEveryoneRole().block().getId(),
                                    PermissionSet.none(),
                                    PermissionSet.of(Permission.SEND_MESSAGES, Permission.ADD_REACTIONS)
                            ))).subscribe();

        }

        return event.reply()
                .withEphemeral(true)
                .withContent("Channels have been created.");
    }
}
