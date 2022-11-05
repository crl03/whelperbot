package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.entities.UserGuilds;
import discord4j.core.event.domain.message.ReactionAddEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReactionAddListener implements EventListener<ReactionAddEvent>   {

    @Autowired
    private DbHelpers dbHelpers;


    @Override
    public Class<ReactionAddEvent> getEventType() {
        System.out.println("Inside getEventType of ReactionAddListener.");
        return ReactionAddEvent.class;
    }

    @Override
    public Mono<Void> execute(ReactionAddEvent event) {
        System.out.println("Reaction was added.");
        System.out.println(event.getMessage());
        System.out.println(event.getEmoji());

        long userDiscordId = event.getUserId().asLong();
        String userDiscordName = event.getMember().get().getUsername();
        long discordGuildId = event.getGuildId().get().asLong();
        System.out.println(userDiscordId + " " + userDiscordName + " " + discordGuildId);

        UserGuilds tempUserGuilds = dbHelpers.getUserGuild(userDiscordId, userDiscordName, discordGuildId);

        dbHelpers.incrementReactionCount(tempUserGuilds);
        
        return Mono.empty();
    }
}
