package org.tfg.timetrackapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.timetrackapi.dto.TimeStampDTO;
import org.tfg.timetrackapi.entity.TimeStamp;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeStampRepository extends JpaRepository<TimeStamp, Long> {
    List<TimeStamp> findByEmployeeId(Long employeeId);

    // Método para obtener registros por employeeId y un rango de fechas
    List<TimeStamp> findByEmployeeIdAndTimestampBetweenOrderByTimestampAsc(
            Long employeeId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
