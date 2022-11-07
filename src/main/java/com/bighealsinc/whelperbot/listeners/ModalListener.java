package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.entities.RaidSchedulesPK;
import com.bighealsinc.whelperbot.entities.UserGuilds;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.TextInput;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class ModalListener {

    @Autowired
    private DbHelpers dbHelpers;

    public Mono<Void> processModal(ModalSubmitInteractionEvent event) {
        Mono<Void> result = null;

        if ("raid".equals(event.getCustomId())) {
            result = processRaid(event);
        }
        return result;
    }

    private Mono<Void> processRaid(ModalSubmitInteractionEvent event) {
        long userDiscordId = event.getInteraction().getUser().getId().asLong();
        String userDiscordName = event.getInteraction().getUser().getUsername();
        long discordGuildId = event.getInteraction().getGuildId().get().asLong();
        String gameName = "";
        String serverName = "";
        String date = "";
        String time = "";

        for (TextInput component : event.getComponents(TextInput.class)) {
            switch (component.getCustomId()) {
                case "game-name": gameName = component.getValue().get(); break;
                case "server-name": serverName = component.getValue().get(); break;
                case "date": date = component.getValue().get(); break;
                case "time": time = component.getValue().get(); break;
            }
        }

        System.out.println("game-name: " + gameName);
        System.out.println("server-name: " + serverName);
        System.out.println("date: " + date);
        System.out.println("time:" + time);

        StringBuilder combinedDateTime = new StringBuilder();
        String[] formatDate = date.split("/");
        String[] formatTime = time.split("[:\s]");
        combinedDateTime.append("20");
        combinedDateTime.append(formatDate[2]);
        combinedDateTime.append("-");
        combinedDateTime.append(formatDate[0]);
        combinedDateTime.append("-");
        combinedDateTime.append(formatDate[1]);
        combinedDateTime.append("T");
        if (formatTime[2].equalsIgnoreCase("pm") && !formatTime[0].equalsIgnoreCase("12")) {
            String hours = String.valueOf((12 + Integer.parseInt(formatTime[0])));
            combinedDateTime.append(hours);
        } else {
            combinedDateTime.append(formatTime[0]);
        }
        combinedDateTime.append(":");
        combinedDateTime.append(formatTime[1]);
        combinedDateTime.append(":00");

        System.out.println("Combined Date and Time: " + combinedDateTime);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime dateTime = LocalDateTime.parse(combinedDateTime, formatter);


        RaidSchedulesPK tempUserRaidSchedulePK = dbHelpers.createUserRaidSchedulePK(userDiscordId, userDiscordName, discordGuildId, dateTime);

        dbHelpers.saveNewUserRaidSchedule(tempUserRaidSchedulePK, gameName, serverName);

        UserGuilds userGuild = dbHelpers.getUserGuild(userDiscordId, userDiscordName, discordGuildId);

        dbHelpers.incrementRaidScheduleCount(userGuild);

        return event.reply()
                .withEphemeral(true)
                .withContent("Raid has been scheduled.");

    }
}
