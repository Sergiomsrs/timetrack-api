package org.tfg.timetrackapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.timetrackapi.dto.TimeStampDTO;
import org.tfg.timetrackapi.entity.TimeStamp;

import java.util.List;

public interface TimeStampRepository extends JpaRepository<TimeStamp, Long> {
    List<TimeStamp> findByEmployeeId(Long employeeId);
}
