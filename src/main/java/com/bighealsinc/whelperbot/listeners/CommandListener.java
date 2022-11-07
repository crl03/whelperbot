package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.entities.RaidSchedules;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.channel.GuildChannel;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public abstract class CommandListener {

    @Autowired
    private DbHelpers dbHelpers;

    public Snowflake findChannelId(ChatInputInteractionEvent event, String channelName) {
        HashMap<Long, String> channelList = new HashMap<>();
        Optional<Snowflake> guildId;
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

    public Mono<Void> listActiveRaids(ChatInputInteractionEvent event) {
        long userDiscordId = event.getInteraction().getUser().getId().asLong();
        String userDiscordName = event.getInteraction().getUser().getUsername();
        long discordGuildId = event.getInteraction().getGuildId().get().asLong();

        List<RaidSchedules> raidSchedulesList = dbHelpers.getGuildRaidSchedules(userDiscordId, userDiscordName, discordGuildId);

        boolean areActiveRaids = false;
        StringBuilder message = new StringBuilder();
        message.append("Active Raids:\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a")
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault());
        for (RaidSchedules raid : raidSchedulesList) {
            System.out.println("Raid:");
            System.out.println(raid.getRaidSchedulesPK().getRaidDateTime().toString());
            if (raid.getRaidSchedulesPK().getRaidDateTime().isBefore(LocalDateTime.now())) {
                raid.setActive(false);
                dbHelpers.updateRaidSchedule(raid);
            }

            String formattedDateTime = raid.getRaidSchedulesPK().getRaidDateTime().format(formatter);
            if (raid.isActive()) {
                areActiveRaids = true;
                message.append(formattedDateTime).append("\n")
                        .append("\t\tGame:\t ").append(raid.getGame()).append("\n")
                        .append("\t\tServer:\t").append(raid.getGameServer()).append("\n");
            }
        }

        if (!areActiveRaids) message.append("None");

        return event.reply()
                .withEphemeral(true)
                .withContent(message.toString());
    }
}
