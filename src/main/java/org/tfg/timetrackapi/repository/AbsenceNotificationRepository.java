package org.tfg.timetrackapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.timetrackapi.entity.AbsenceNotification;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AbsenceNotificationRepository extends JpaRepository<AbsenceNotification, Long> {
    boolean existsByUserIdAndFechaAndHora(Long userId, LocalDate fecha, LocalTime hora);



}
