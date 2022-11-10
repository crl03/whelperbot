package com.bighealsinc.whelperbot.listeners;

import discord4j.core.event.domain.guild.MemberLeaveEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MemberDepartGuildListener extends GuildEventListener implements EventListener<MemberLeaveEvent> {

    @Override
    public Class<MemberLeaveEvent> getEventType() {
        return MemberLeaveEvent.class;
    }

    @Override
    public Mono<Void> execute(MemberLeaveEvent event) {

        guildMemberDepart(event, "member-departures");

        return Mono.empty();
    }
}
