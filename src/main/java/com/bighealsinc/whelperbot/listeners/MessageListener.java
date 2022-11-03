package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.commands.Command;
import com.bighealsinc.whelperbot.entities.User;
import com.bighealsinc.whelperbot.services.*;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;


public abstract class MessageListener {

    private UserService userService;
    private GuildService guildService;
    private UserGuildsService userGuildsService;
    private String message;
    private String command;
    public Mono<Void> processCommand(Message eventMessage) {
        long userDiscordId = eventMessage.getAuthor().get().getId().asLong();
        long discordGuildId = eventMessage.getGuildId().get().asLong();



        // query db table user_guilds to see if entry exists


        // query db table users with userDiscordId to find user's id in the database

        // if user doesn't exist add them

        // query db table guilds with discordGuildId to find guild's id in the database

        // if guild doesn't exist add it and add user to guild

        // find UserGuilds with info form previous queries

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