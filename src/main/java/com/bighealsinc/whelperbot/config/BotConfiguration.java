package com.bighealsinc.whelperbot.config;

import com.bighealsinc.whelperbot.listeners.EventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.gateway.intent.IntentSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BotConfiguration {
    @Value("${token}")
    private String token;

    private List<ApplicationCommandRequest> list;

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
        GatewayDiscordClient client = DiscordClientBuilder.create(token)
                .build()
                .gateway()
                .setEnabledIntents(IntentSet.all())
                .setInitialPresence(ignore -> ClientPresence.online(ClientActivity.playing("break the most stuff.")))
                .login()
                .block();

        System.out.println("Bot has started.");
        System.out.println("Iterating through event listeners.");
        for(EventListener<T> listener : eventListeners) {
            System.out.println(listener.getEventType());
            client.on(listener.getEventType())
                    .flatMap(listener::execute)
                    .onErrorResume(listener::handleError)
                    .subscribe();


        }
        System.out.println("Finished iterating, returning client.");

        return client;
    }

    @Bean
    public <T extends Event> GatewayDiscordClient commandRegistrar(GatewayDiscordClient client) {
        long applicationId = client.getRestClient().getApplicationId().block();
        long guildId = 1028801665492602880L;

        list = new ArrayList<>();

        ApplicationCommandRequest todoCmdRequest = ApplicationCommandRequest.builder()
                .name("todo")
                .description("Returns your Todo list.")
                .build();

        ApplicationCommandRequest loggingChannelCmdRequest = ApplicationCommandRequest.builder()
                .name("createloggingchannels")
                .description("Creates logging channels for member server joins and departures.")
                .build();

        ApplicationCommandRequest readOnlyCategoryCmdRequest = ApplicationCommandRequest.builder()
                .name("createreadonlycategory")
                .description("Creates read only category for logging channels.")
                .build();

        ApplicationCommandRequest myStatsCmdRequest = ApplicationCommandRequest.builder()
                .name("mystats")
                .description("View your personal stats.")
                .build();

        ApplicationCommandRequest mostactiveCmdRequest = ApplicationCommandRequest.builder()
                .name("mostactive")
                .description("View most active user.")
                .build();

        ApplicationCommandRequest scheduleRaidCmdRequest = ApplicationCommandRequest.builder()
                .name("scheduleraid")
                .description("Enter info to schedule date/time of riad.")
                .build();

        list.add(todoCmdRequest);
        list.add(loggingChannelCmdRequest);
        list.add(readOnlyCategoryCmdRequest);
        list.add(myStatsCmdRequest);
        list.add(mostactiveCmdRequest);
        list.add(scheduleRaidCmdRequest);

        for (ApplicationCommandRequest command : list) {
            client.getRestClient().getApplicationService()
                    .createGuildApplicationCommand(applicationId, guildId, command)
                    .subscribe();
        }

        return client;
    }
}