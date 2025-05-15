package org.tfg.timetrackapi.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tfg.timetrackapi.dto.Last3Dto;
import org.tfg.timetrackapi.dto.TimeStampDTO;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface TimeStampRepository extends JpaRepository<TimeStamp, Long> {
    List<TimeStamp> findByEmployeeId(Long employeeId);

    // Método para obtener registros por employeeId y un rango de fechas
    List<TimeStamp> findByEmployeeIdAndTimestampBetweenOrderByTimestampAsc(
            Long employeeId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<TimeStamp> findByEmployeeIdAndTimestampBetween(Long employeeId, LocalDateTime start, LocalDateTime end);

    List<TimeStamp> findTop3ByOrderByIdDesc();

    void deleteAllByEmployee_Id(Long employeeId);

    boolean existsByEmployeeIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);






}
