package com.bighealsinc.whelperbot;

import com.bighealsinc.whelperbot.config.BotConfiguration;
import com.bighealsinc.whelperbot.listeners.EventListener;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class WhelperbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhelperbotApplication.class, args);

	}

}
