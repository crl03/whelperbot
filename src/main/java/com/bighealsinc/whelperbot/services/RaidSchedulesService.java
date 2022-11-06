package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.RaidSchedules;
import com.bighealsinc.whelperbot.entities.RaidSchedulesPK;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RaidSchedulesService {

    Optional<RaidSchedules> findByCompositeId(int userId, int guildId, LocalDateTime raidDateTime);

    List<RaidSchedules> findAllByGuildId(int guildId);

    void save(RaidSchedules raidSchedules);

    void deleteByCompositeId(int userId, int guildId, LocalDateTime raidDateTime);
}
