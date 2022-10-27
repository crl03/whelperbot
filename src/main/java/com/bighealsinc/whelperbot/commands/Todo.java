package com.bighealsinc.whelperbot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Todo implements Command {

    @Override
    public String getName() {
        return "todo";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        return event.reply()
                .withEphemeral(true)
                .withContent("Your ToDo list:\n- work on your bot\n- eat dinner\n- read all the documentation");
    }
}
