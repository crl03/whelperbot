package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.RaidSchedules;
import com.bighealsinc.whelperbot.entities.RaidSchedulesPK;
import com.bighealsinc.whelperbot.repositories.RaidSchedulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RaidSchedulesServiceImpl implements RaidSchedulesService {

    @Autowired
    private RaidSchedulesRepository raidSchedulesRepository;

    @Autowired
    public RaidSchedulesServiceImpl(RaidSchedulesRepository raidSchedulesRepository) {
        raidSchedulesRepository = raidSchedulesRepository;

    }

    @Override
    public Optional<RaidSchedules> findByCompositeId(int userId, int guildId, LocalDateTime raidDateTime) {
        Optional<RaidSchedules> result = raidSchedulesRepository.findById(new RaidSchedulesPK(userId, guildId, raidDateTime));

//        RaidSchedules foundRaidSchedule;
//        if (result.isPresent()) {
//            foundRaidSchedule = result.get();
//        } else {
//            throw new RuntimeException("Cannot find RaidSchedulesPK: " + userId + guildId + raidDateTime);
//        }
        return result;
    }

    @Override
    public List<RaidSchedules> findAllByGuildId(int guildId) {
        return raidSchedulesRepository.findAllByGuildId(guildId);
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
