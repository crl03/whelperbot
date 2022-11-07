package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.RaidSchedules;
import com.bighealsinc.whelperbot.entities.RaidSchedulesPK;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaidSchedulesRepository extends JpaRepository<RaidSchedules, RaidSchedulesPK> {

    List<RaidSchedules> findAllByGuildId(int guildId, Sort sort);
}