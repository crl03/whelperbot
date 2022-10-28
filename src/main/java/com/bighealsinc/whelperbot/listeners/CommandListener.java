package com.bighealsinc.whelperbot.listeners;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.channel.GuildChannel;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class CommandListener {
    private HashMap<Long, String> channelList = new HashMap<>();
    private Optional<Snowflake> guildId;

    public Snowflake findChannelId(ChatInputInteractionEvent event, String channelName) {
        guildId = event.getInteraction().getGuildId();
        List<GuildChannel> fluxChannelList = event.getClient().getGuildChannels(Snowflake.of(guildId.get().asLong()))
                .collectList().block();

        for (GuildChannel channel : fluxChannelList) {
            channelList.put(channel.getId().asLong(), channel.getName());
            if (channel.getName().equalsIgnoreCase(channelName)) {
                return channel.getId();
            }
        }
        return null;
    }
}
