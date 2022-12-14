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
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling
public class BotConfiguration {
    @Value("${token}")
    private String token;

    private GatewayDiscordClient client;
    private List<ApplicationCommandRequest> list;


    @Lazy
    public BotConfiguration(GatewayDiscordClient client) {
        this.client = client;
    }

    @Bean
    @Primary
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
        client = DiscordClientBuilder.create(token)
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

    public GatewayDiscordClient getGatewayDiscordClient() {
        return this.client;
    }

    public List<ApplicationCommandRequest> getCommandList() {
        return this.list;
    }

    @Bean
    public <T extends Event> GatewayDiscordClient commandRegistrar(GatewayDiscordClient client) {
        long applicationId = client.getRestClient().getApplicationId().block();
        long guildId = 1028801665492602880L;

        list = new ArrayList<>();

        ApplicationCommandRequest todoCmdRequest = ApplicationCommandRequest.builder()
                .name("todo")
                .description("Returns your Todo list. (this is nostalgia for my first bot command test)")
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
                .description("Enter info to schedule date/time of riad, or update raid by entering its date and time.")
                .build();

        ApplicationCommandRequest listRaidsCmdRequest = ApplicationCommandRequest.builder()
                .name("listraids")
                .description("View list of active raids.")
                .build();

        ApplicationCommandRequest deleteRaidCmdRequest = ApplicationCommandRequest.builder()
                .name("deleteraid")
                .description("Delete raid by date and time.")
                .build();

        ApplicationCommandRequest weatherCmdRequest = ApplicationCommandRequest.builder()
                .name("weather")
                .description("Get local current weather by zip code.")
                .build();

        ApplicationCommandRequest helpCmdRequest = ApplicationCommandRequest.builder()
                .name("help")
                .description("List all available commands.")
                .build();

        list.add(helpCmdRequest);
        list.add(todoCmdRequest);
        list.add(loggingChannelCmdRequest);
        list.add(readOnlyCategoryCmdRequest);
        list.add(myStatsCmdRequest);
        list.add(mostactiveCmdRequest);
        list.add(scheduleRaidCmdRequest);
        list.add(listRaidsCmdRequest);
        list.add(deleteRaidCmdRequest);
        list.add(weatherCmdRequest);

        for (ApplicationCommandRequest command : list) {
            client.getRestClient().getApplicationService()
                    .createGuildApplicationCommand(applicationId, guildId, command)
                    .subscribe();
        }

        // Deleted an old command.  Found ID through server settings.  Kept here for future reference.
//        client.getRestClient().getApplicationService()
//                .deleteGuildApplicationCommand(applicationId,guildId,1034841232993697863L)
//                .subscribe();

        return client;
    }
}