package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.entities.UserGuilds;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.TextInput;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public abstract class ModalListener {

    @Autowired
    DbHelpers dbHelpers;

    public Mono<Void> processModal(ModalSubmitInteractionEvent event) {
        Mono<Void> result = null;

        if ("raid".equals(event.getCustomId())) {
            result = processRaid(event);
        }
        return result;
    }

    private static Mono<Void> processRaid(ModalSubmitInteractionEvent event) {
        long userDiscordId = event.getMessage().get().getUserData().id().asLong();
        String userDiscordName = event.getMessage().get().getUserData().username();
        long discordGuildId = event.getMessage().get().getGuildId().get().asLong();
        String gameName = "";
        String serverName = "";
        String date = "";
        String time = "";

        for (TextInput component : event.getComponents(TextInput.class)) {
            switch (component.getCustomId()) {
                case "game-name": gameName = component.getValue().toString(); break;
                case "server-name": serverName = component.getValue().toString(); break;
                case "date": date = component.getValue().toString(); break;
                case "time": time = component.getValue().toString(); break;
            }
        }

        //TODO call DbHelpers command getRaidSchedules

        LocalDateTime dateTime = LocalDateTime.parse(date + " " + time);

        return event.reply();
    }
}
