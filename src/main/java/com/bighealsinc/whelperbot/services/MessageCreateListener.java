package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.listeners.EventListener;
import com.bighealsinc.whelperbot.listeners.MessageListener;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageCreateListener extends MessageListener implements EventListener<MessageCreateEvent> {

    @Override
    public Class<MessageCreateEvent> getEventType() {
        System.out.println("Inside getEventType of MessageCreateListener");
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        System.out.println("Message was created.");
        System.out.println(event.getMessage());
        System.out.println(event.getMessage().getContent());
        return processCommand(event.getMessage());
    }
}