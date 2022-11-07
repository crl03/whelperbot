package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.listeners.CommandListener;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
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
}
