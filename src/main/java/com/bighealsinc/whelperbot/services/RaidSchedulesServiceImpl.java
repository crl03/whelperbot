package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.RaidSchedules;
import com.bighealsinc.whelperbot.entities.RaidSchedulesPK;
import com.bighealsinc.whelperbot.repositories.RaidSchedulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

        return result;
    }

    @Override
    public List<RaidSchedules> findAllByGuildId(int guildId, Sort sort) {
        return raidSchedulesRepository.findAllByGuildId(guildId, sort);
    }

    @Override
    public List<RaidSchedules> findAll() {
        return raidSchedulesRepository.findAll();
    }

    @Override
    public void save(RaidSchedules raidSchedules) {
        raidSchedulesRepository.save(raidSchedules);
    }

    @Override
    public void deleteByRaidSchedulesPK(RaidSchedulesPK raidSchedulesPK) {
        raidSchedulesRepository.deleteById(raidSchedulesPK);
    }
}
