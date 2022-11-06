package com.bighealsinc.whelperbot.listeners;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ModalSubmitEvent extends ModalListener implements EventListener<ModalSubmitInteractionEvent>{
    @Override
    public Class getEventType() {
        return ModalSubmitInteractionEvent.class;
    }

    @Override
    public Mono<Void> execute(ModalSubmitInteractionEvent event) {

        return processModal(event);
    }
}
