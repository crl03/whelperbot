package com.bighealsinc.whelperbot.services;

import discord4j.core.object.entity.channel.TextChannel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageController<T extends TextChannel> {

    public void sendMessage(T event) {

    }

}
