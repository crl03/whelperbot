package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.commands.Command;
import com.bighealsinc.whelperbot.listeners.EventListener;
import com.bighealsinc.whelperbot.listeners.MessageListener;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Service
public class ChatInputInteractionEventListener extends MessageListener implements EventListener<ChatInputInteractionEvent> {

    private final Collection<Command> commands;

    public ChatInputInteractionEventListener(List<Command> slashCommands) {
        commands = slashCommands;
    }
    @Override
    public Class<ChatInputInteractionEvent> getEventType() {
        System.out.println("Inside getEventType ChatInputInteractionEventListener");
        return ChatInputInteractionEvent.class;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        System.out.println("Executing slash command: " + event.getCommandName());
        return Flux.fromIterable(commands)
                .filter(command -> command.getName().equals(event.getCommandName()))
                .next()
                .flatMap(command -> command.execute(event));
    }
}
