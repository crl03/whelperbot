package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.listeners.CommandListener;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReadOnlyCategory extends CommandListener implements Command {
    @Override
    public String getName() {
        return "createreadonlycategory";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        System.out.println("Inside create read only channel.");
        Snowflake readOnlyChannels = findChannelId(event, "Read Only Channels");


        if (readOnlyChannels == null) {
            event.getClient().getGuildById(Snowflake.of(1028801665492602880L))
                    .flatMap(guild -> guild.createCategory("Read Only Channels")
                            .withPermissionOverwrites(PermissionOverwrite.forRole(
                                    guild.getEveryoneRole().block().getId(),
                                    PermissionSet.none(),
                                    PermissionSet.of(Permission.SEND_MESSAGES, Permission.ADD_REACTIONS)
                            )))
                    .subscribe();
        }
        return event.reply()
                .withEphemeral(true)
                .withContent("Read Only Channels category has been created.");
    }
}
