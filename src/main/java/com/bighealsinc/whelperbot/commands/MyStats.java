package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.entities.UserGuilds;
import com.bighealsinc.whelperbot.listeners.CommandListener;
import com.bighealsinc.whelperbot.listeners.DbHelpers;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MyStats extends CommandListener implements Command {

    @Autowired
    private DbHelpers dbHelpers;
    @Override
    public String getName() {
        return "mystats";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        long userDiscordId = event.getInteraction().getUser().getId().asLong();
        String userDiscordName = event.getInteraction().getUser().getUsername();
        long discordGuildId = event.getInteraction().getGuildId().get().asLong();

        UserGuilds tempUserGuild = dbHelpers.getUserGuild(userDiscordId, userDiscordName, discordGuildId);

        String message = "Personal Stats:\n"
                + "\t\tMessages Sent:\t\t" + tempUserGuild.getMessages()
                + "\n\t\tReactions Added:\t " + tempUserGuild.getReactions()
                + "\n\t\tRaids Scheduled:\t  " + tempUserGuild.getScheduledRaids();
        return event.reply()
                .withEphemeral(true)
                .withContent(message);
    }
}
