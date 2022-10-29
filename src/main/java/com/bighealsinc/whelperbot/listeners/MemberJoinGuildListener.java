package com.bighealsinc.whelperbot.listeners;

import discord4j.core.event.domain.guild.MemberJoinEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MemberJoinGuildListener extends GuildEventListener implements EventListener<MemberJoinEvent> {

    @Override
    public Class<MemberJoinEvent> getEventType() {
        return MemberJoinEvent.class;
    }

    @Override
    public Mono<Void> execute(MemberJoinEvent event) {

        guildMemberJoin(event, "member-join");

        return Mono.empty();
    }
}
