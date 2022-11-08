package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.listeners.CommandListener;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ListRaids extends CommandListener implements Command {

    @Override
    public String getName() {
        return "listraids";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        return listActiveRaids(event);
    }

    @Scheduled(cron = "0 0/15 * * * ?")
    public void printToRaidReminders() {

        postRaidsToChannel();

    }

}
