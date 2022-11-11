package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.config.BotConfiguration;
import com.bighealsinc.whelperbot.entities.RaidSchedules;
import com.bighealsinc.whelperbot.entities.User;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.beans.factory.annotation.Autowired;
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


    public Mono<Void> listCommands(ChatInputInteractionEvent event) {
        List<ApplicationCommandRequest> commandList = botConfiguration.getCommandList();

        return event.reply()
                .withEphemeral(true)
                .withContent(buildCommandList(commandList));
    }

    public String buildCommandList(List<ApplicationCommandRequest> commandList) {
        StringBuilder message = new StringBuilder();

        message.append("Available Commands:\n");

        for (ApplicationCommandRequest command : commandList) {
            message.append("/").append(command.name()).append(":\n\t\t").append(command.description().get()).append("\n\n");
        }

        return message.toString();
    }

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

            //TODO get username through dbHelpers
            String formattedDateTime = raid.getRaidSchedulesPK().getRaidDateTime().format(formatter);
            User tempUser = dbHelpers.getUser(raid.getRaidSchedulesPK().getUserId());
            String organizer = tempUser.getDiscordUserName();
            if (raid.isActive()) {
                areActiveRaids = true;
                message.append(formattedDateTime).append("\n")
                        .append("\t\tOrganizer:\t  ").append(organizer).append("\n")
                        .append("\t\tGame:\t\t\t ").append(raid.getGame()).append("\n")
                        .append("\t\tServer:\t\t\t").append(raid.getGameServer()).append("\n");
            }
        }

        if (!areActiveRaids) message.append("None");

        return message.toString();
    }
}
