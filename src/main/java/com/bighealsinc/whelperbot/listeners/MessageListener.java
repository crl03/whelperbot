package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.commands.Command;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;


public abstract class MessageListener {

    private String message;
    private String command;
    public Mono<Void> processCommand(Message eventMessage) {

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