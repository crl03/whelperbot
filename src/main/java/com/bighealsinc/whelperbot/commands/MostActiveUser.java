package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.entities.User;
import com.bighealsinc.whelperbot.entities.UserGuilds;
import com.bighealsinc.whelperbot.listeners.CommandListener;
import com.bighealsinc.whelperbot.listeners.DbHelpers;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class MostActiveUser extends CommandListener implements Command {

    @Autowired
    private DbHelpers dbHelpers;

    @Override
    public String getName() {
        return "mostactive";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        long discordGuildId = event.getInteraction().getGuildId().get().asLong();
        StringBuilder mostActive = new StringBuilder("Not enough activity yet.");
        int activity = 0;
        int previous = 0;

        List<UserGuilds> tempUserList = dbHelpers.getAllUsersGuild(discordGuildId);

        for (UserGuilds user : tempUserList) {
            previous = activity;
            activity = user.getMessages() + user.getReactions() + user.getScheduledRaids();

            if (activity > 0) {
                if (activity > previous) {
                    mostActive = new StringBuilder(user.getUser().getDiscordUserName());
                } else if (activity == previous) {
                    mostActive.append(", ").append(user.getUser().getDiscordUserName());
                }
            }
        }


        return event.reply()
                .withEphemeral(true)
                .withContent("Most active on the server:\n" + mostActive);
    }
}
