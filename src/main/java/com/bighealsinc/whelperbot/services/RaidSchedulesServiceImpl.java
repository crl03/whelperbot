package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.RaidSchedules;
import com.bighealsinc.whelperbot.entities.RaidSchedulesPK;
import com.bighealsinc.whelperbot.repositories.RaidSchedulesRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

public class RaidSchedulesServiceImpl implements RaidSchedulesService {

    private RaidSchedulesRepository raidSchedulesRepository;

    @Autowired
    public RaidSchedulesServiceImpl(RaidSchedulesRepository raidSchedulesRepository) {
        this.raidSchedulesRepository = raidSchedulesRepository;

    }

    @Override
    public RaidSchedules findByCompositeId(int userId, int guildId, LocalDateTime raidDateTime) {
        Optional<RaidSchedules> result = raidSchedulesRepository.findById(new RaidSchedulesPK(userId, guildId, raidDateTime));

        RaidSchedules foundRaidSchedule;
        if (result.isPresent()) {
            foundRaidSchedule = result.get();
        } else {
            throw new RuntimeException("Cannot find RaidSchedulesPK: " + userId + guildId + raidDateTime);
        }
        return foundRaidSchedule;
    }

    @Override
    public void save(RaidSchedules raidSchedules) {
        raidSchedulesRepository.save(raidSchedules);
    }

    @Override
    public void deleteByCompositeId(int userId, int guildId, LocalDateTime raidDateTime) {
        raidSchedulesRepository.deleteById(new RaidSchedulesPK(userId, guildId, raidDateTime));
    }
}
