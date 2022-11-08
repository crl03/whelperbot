package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.config.BotConfiguration;
import com.bighealsinc.whelperbot.entities.RaidSchedules;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class CommandListener {

    @Autowired
    private DbHelpers dbHelpers;

    @Autowired
    private BotConfiguration botConfiguration;


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

    public long findChannelIdByGuildId( long guildId, String channelName) {
        GatewayDiscordClient client = botConfiguration.getGatewayDiscordClient();
        List<GuildChannel> fluxChannelList = client.getGuildChannels(Snowflake.of(guildId))
                .collectList().block();

        for (GuildChannel channel : fluxChannelList) {
            if (channel.getName().equalsIgnoreCase(channelName)) {
                return channel.getId().asLong();
            }
        }
        return -1;
    }

    public Mono<Void> listActiveRaids(ChatInputInteractionEvent event) {
        long userDiscordId = event.getInteraction().getUser().getId().asLong();
        String userDiscordName = event.getInteraction().getUser().getUsername();
        long discordGuildId = event.getInteraction().getGuildId().get().asLong();
        String startMessage = "Active Raids:\n";

        List<RaidSchedules> raidSchedulesList;
        raidSchedulesList = dbHelpers.getGuildRaidSchedules(userDiscordId, userDiscordName, discordGuildId);

        String message = composeRaidScheduleMessage(raidSchedulesList, startMessage);

        return event.reply()
                .withEphemeral(true)
                .withContent(message);
    }

    public void postRaidsToChannel() {
        GatewayDiscordClient client = botConfiguration.getGatewayDiscordClient();
        List<RaidSchedules> allRaids = dbHelpers.getAllRaidSchedules();
        List<RaidSchedules> guildRaids = new ArrayList<>();
        long guildId = 0;
        long channelId = 0;
        Duration between;
        String startMessage = "Upcoming Raids:\n";

        for (RaidSchedules raid : allRaids) {
            if (raid.isActive() && raid.getGuild().getGuildId() != guildId) {
                if (!guildRaids.isEmpty()) {
                String message = composeRaidScheduleMessage(guildRaids, startMessage);
                client.getChannelById(Snowflake.of(channelId))
                        .ofType(MessageChannel.class)
                        .flatMap(channel -> channel.createMessage(message))
                        .subscribe();
                guildRaids = new ArrayList<>();
                }
                guildId = raid.getGuild().getGuildId();
                channelId = findChannelIdByGuildId(guildId, "raid-reminders");
            }
            between = Duration.between(LocalDateTime.now(), raid.getRaidSchedulesPK().getRaidDateTime());
            if (raid.isActive() && between.toMinutes() >= 0 && between.toMinutes() <= 30) {
                System.out.println("ADDING: " +raid.getGame() + " " + raid.getRaidSchedulesPK().getRaidDateTime());
                guildRaids.add(raid);
            }

        }

        if (!guildRaids.isEmpty()) {
            String message = composeRaidScheduleMessage(guildRaids, startMessage);
            client.getChannelById(Snowflake.of(channelId))
                    .ofType(MessageChannel.class)
                    .flatMap(channel -> channel.createMessage(message))
                    .subscribe();
        }


    }

    private String composeRaidScheduleMessage(List<RaidSchedules> raidSchedulesList, String startMessage) {
        boolean areActiveRaids = false;
        StringBuilder message = new StringBuilder();
        message.append(startMessage);
        Duration between;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a")
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault());
        for (RaidSchedules raid : raidSchedulesList) {
        between = Duration.between(LocalDateTime.now(), raid.getRaidSchedulesPK().getRaidDateTime());
            if (raid.isActive() && between.toMinutes() < 0) {
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

        return message.toString();
    }
}
