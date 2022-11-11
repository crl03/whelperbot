package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.listeners.CommandListener;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Help extends CommandListener implements Command {


    @Override
    public String getName() {
        return "help";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        return listCommands(event);
    }
}
