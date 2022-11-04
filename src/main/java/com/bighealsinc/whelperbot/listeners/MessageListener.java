package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.commands.Command;
import com.bighealsinc.whelperbot.entities.User;
import com.bighealsinc.whelperbot.entities.UserGuilds;
import com.bighealsinc.whelperbot.services.*;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;


public abstract class MessageListener {

    @Autowired
    private DbHelpers dbHelpers;

    @Autowired
    private UserGuildsService userGuildsService;
    private String message;
    private String command;
    public Mono<Void> processCommand(Message eventMessage) {
        if (!eventMessage.getAuthor().get().isBot()) {
            long userDiscordId = eventMessage.getUserData().id().asLong();
            long discordGuildId = eventMessage.getGuildId().get().asLong();
            String userDiscordName = eventMessage.getUserData().username();
            System.out.println("Inside MessageListener processCommand");
            System.out.println("userDiscordId: " + userDiscordId);
            System.out.println("userDiscordName: " + userDiscordName);
            System.out.println("discordGuildId: " + discordGuildId);

            UserGuilds userGuild = dbHelpers.getUserGuild(userDiscordId, userDiscordName, discordGuildId);

            // move this into db helpers
            userGuild.setMessages(userGuild.getMessages() + 1);
            userGuildsService.save(userGuild);
        }


        // update appropriate fields, for now messages and reactions


//            return Mono.just(eventMessage)
//                    .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
//                .filter(message -> message.getContent().equalsIgnoreCase("!todo"))
//                    .flatMap(Message::getChannel)
//                    .flatMap(channel -> channel.createMessage(message))
//                    .then();
//        command = commands.find(eventMessage);
//        switch (command) {
//            case "!todo":
//                todo.execute();
//                message = todo.getMessage();
//                break;
//        }

        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase(command))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(message))
                .then();


    }

}