package com.bighealsinc.whelperbot.services;

import discord4j.core.GatewayDiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WhelperBot {

    @Autowired
    private final MessageCreateListener messageCreateListener;
    @Autowired
    private final MessageUpdateListener messageUpdateListener;
    @Autowired
    private static GatewayDiscordClient client;


    public static GatewayDiscordClient getClient() {
        return client;
    }

    public MessageCreateListener getMessageCreateListener() {
        return messageCreateListener;
    }

    public MessageUpdateListener getMessageUpdateListener() {
        return messageUpdateListener;
    }
}
