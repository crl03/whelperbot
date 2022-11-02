package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.RaidSchedules;
import com.bighealsinc.whelperbot.entities.RaidSchedulesPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaidSchedulesRepository extends JpaRepository<RaidSchedules, RaidSchedulesPK> {
}