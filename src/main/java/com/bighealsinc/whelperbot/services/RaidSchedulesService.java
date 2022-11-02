package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.RaidSchedules;
import com.bighealsinc.whelperbot.entities.RaidSchedulesPK;

import java.time.LocalDateTime;

public interface RaidSchedulesService {

    RaidSchedules findByCompositeId(int userId, int guildId, LocalDateTime raidDateTime);

    void save(RaidSchedules raidSchedules);

    void deleteByCompositeId(int userId, int guildId, LocalDateTime raidDateTime);
}
