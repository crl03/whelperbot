package com.bighealsinc.whelperbot.commands;

import com.bighealsinc.whelperbot.listeners.CommandListener;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ScheduleRaid extends CommandListener implements Command {
    @Override
    public String getName() {
        return "scheduleraid";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {

        return event.presentModal(InteractionPresentModalSpec.builder()
                .title("Schedule Raid")
                .customId("raid")
                .addComponent(
                        ActionRow.of(TextInput.small("game-name", "Game Name"))
                )
                .addComponent(
                        ActionRow.of(TextInput.small("server-name", "Server Name")
                                .required(false))
                ).addComponent(
                        ActionRow.of(TextInput.small("date", "Date (MM/DD/YY)", 8, 8))
                ).addComponent(
                        ActionRow.of(TextInput.small("time", "Time (HH:MM AM/PM)", 8, 8))
                )
//                .addComponent(
//                        ActionRow.of(SelectMenu.of("time",
//                                SelectMenu.Option.of("12:00", "12:00"),
//                                SelectMenu.Option.of("12:30", "12:30"),
//                                SelectMenu.Option.of("01:00", "01:00"),
//                                SelectMenu.Option.of("01:30", "01:30"),
//                                SelectMenu.Option.of("02:00", "02:00"),
//                                SelectMenu.Option.of("02:30", "02:30"),
//                                SelectMenu.Option.of("03:00", "03:00"),
//                                SelectMenu.Option.of("03:30", "03:30"),
//                                SelectMenu.Option.of("04:00", "04:00"),
//                                SelectMenu.Option.of("04:30", "04:30"),
//                                SelectMenu.Option.of("05:00", "05:00"),
//                                SelectMenu.Option.of("05:30", "05:30"),
//                                SelectMenu.Option.of("06:00", "06:00"),
//                                SelectMenu.Option.of("06:30", "06:30"),
//                                SelectMenu.Option.of("07:00", "07:00"),
//                                SelectMenu.Option.of("07:30", "07:30"),
//                                SelectMenu.Option.of("08:00", "08:00"),
//                                SelectMenu.Option.of("08:30", "08:30"),
//                                SelectMenu.Option.of("09:00", "09:00"),
//                                SelectMenu.Option.of("09:30", "09:30"),
//                                SelectMenu.Option.of("10:00", "10:00"),
//                                SelectMenu.Option.of("10:30", "10:30"),
//                                SelectMenu.Option.of("11:00", "11:00"),
//                                SelectMenu.Option.of("11:30", "11:30")
//                        ))
//                )
//                .addComponent(
//                        ActionRow.of(SelectMenu.of("am-pm",
//                                SelectMenu.Option.of("AM", "AM"),
//                                SelectMenu.Option.of("PM", "PM")
//                        ))
//                )
                .build());
    }
}
