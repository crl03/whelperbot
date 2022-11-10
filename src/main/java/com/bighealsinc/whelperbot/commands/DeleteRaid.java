package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.listeners.CommandListener;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DeleteRaid extends CommandListener implements Command {
    @Override
    public String getName() {
        return "deleteraid";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {

        return event.presentModal(InteractionPresentModalSpec.builder()
                .title("Delete Raid")
                .customId("deleteraid")
                .addComponent(
                        ActionRow.of(TextInput.small("date", "Date (MM/DD/YY)", 8, 8))
                ).addComponent(
                        ActionRow.of(TextInput.small("time", "Time (HH:MM AM/PM)", 8, 8))
                ).build());
    }
}
