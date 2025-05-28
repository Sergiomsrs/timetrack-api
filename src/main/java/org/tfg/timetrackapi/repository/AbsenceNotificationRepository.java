package org.tfg.timetrackapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.tfg.timetrackapi.entity.AbsenceNotification;
import org.tfg.timetrackapi.entity.EmployeeSchedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AbsenceNotificationRepository extends JpaRepository<AbsenceNotification, Long> {
    boolean existsByUserIdAndFechaAndHora(Long userId, LocalDate fecha, LocalTime hora);

    List<AbsenceNotification> findTop10ByOrderByIdDesc();

    @Modifying
    @Transactional
    @Query("DELETE FROM AbsenceNotification a WHERE a.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);



}
