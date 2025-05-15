package org.tfg.timetrackapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.timetrackapi.entity.AbsenceNotification;
import org.tfg.timetrackapi.entity.EmployeeSchedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AbsenceNotificationRepository extends JpaRepository<AbsenceNotification, Long> {
    boolean existsByUserIdAndFechaAndHora(Long userId, LocalDate fecha, LocalTime hora);

    List<AbsenceNotification> findTop10ByOrderByIdDesc();



}
