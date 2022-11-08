package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.listeners.CommandListener;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Weather extends CommandListener implements Command {
    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {

        return event.presentModal(InteractionPresentModalSpec.builder()
                .title("Enter Zip Code")
                .customId("weather")
                .addComponent(
                        ActionRow.of(TextInput.small("zipcode", "5 Digit Zip Code:", 5, 5))
                ).build());
    }
}
