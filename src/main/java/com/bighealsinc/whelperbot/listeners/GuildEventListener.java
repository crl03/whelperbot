package com.bighealsinc.whelperbot.listeners;


import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.event.domain.guild.MemberLeaveEvent;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Date;
import java.util.List;

public abstract class GuildEventListener {

    GatewayDiscordClient client;
    private long guildId = 0;
    private long channelId = 0;

    public void guildMemberJoin(MemberJoinEvent event, String memberJoinChannel) {
        client = event.getClient();
        guildId = event.getGuildId().asLong();
        channelId = getChannelId(client, guildId, memberJoinChannel);

        String message = "Member:\t" + event.getMember().getDisplayName() + "\n"
                        + "Joined:\t\t" + new Date()
                        + "\n";

        sendMemberMovementMessage(client, message);

    }

    public void guildMemberDepart(MemberLeaveEvent event, String memberJoinChannel) {
        client = event.getClient();
        guildId = event.getGuildId().asLong();
        channelId = getChannelId(client, guildId, memberJoinChannel);

        String message = "Member: \t" + event.getUser().getUsername() + "\n"
                        + "Departed:\t" + new Date()
                        + "\n";

        sendMemberMovementMessage(client, message);

    }

    private void sendMemberMovementMessage(GatewayDiscordClient client, String message) {
        client.getChannelById(Snowflake.of(channelId))
                .ofType(MessageChannel.class)
                .flatMap(channel -> channel.createMessage(message))
                .subscribe();
    }

    private long getChannelId(GatewayDiscordClient client, long guildId, String memberJoinChannel) {
        List<GuildChannel> fluxChannelList = client.getGuildChannels(Snowflake.of(guildId))
                .collectList().block();

        for (GuildChannel channel : fluxChannelList) {
            if (channel.getName().equalsIgnoreCase(memberJoinChannel)) {
                channelId = channel.getId().asLong();
            }
        }
        return channelId;
    }
}
