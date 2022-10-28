package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.listeners.CommandListener;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LoggingChannels extends CommandListener implements Command {

    @Override
    public String getName() {
        return "createloggingchannels";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        Snowflake readOnlyChannels = findChannelId(event, "Read Only Channels");
        Snowflake memberJoin = findChannelId(event, "member-join");
        Snowflake memberDeparture = findChannelId(event, "member-departures");
        if (readOnlyChannels == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Please run command createreadonlycategory first.");
        }
        if (memberJoin == null) {
            event.getClient().getGuildById(Snowflake.of(1028801665492602880L))
                    .flatMap(guild -> guild.createTextChannel("member-join")
                                    .withParentId(readOnlyChannels)
                    ).subscribe();
        }

        if (memberDeparture == null) {
            event.getClient().getGuildById(Snowflake.of(1028801665492602880L))
                    .flatMap(guild -> guild.createTextChannel("member-departures")
                                    .withParentId(readOnlyChannels)
                    ).subscribe();

        }

        return event.reply()
                .withEphemeral(true)
                .withContent("Channels have been created.");
    }
}
