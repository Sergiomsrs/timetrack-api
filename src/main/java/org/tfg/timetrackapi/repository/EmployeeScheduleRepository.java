package org.tfg.timetrackapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.tfg.timetrackapi.entity.EmployeeSchedule;
import org.tfg.timetrackapi.entity.User;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeScheduleRepository extends JpaRepository<EmployeeSchedule, Long> {

    List<EmployeeSchedule> findByUserIdAndDia(Long userId, DayOfWeek dia);

    List<EmployeeSchedule> findAllByUserId(Long userId);

    boolean existsByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM EmployeeSchedule a WHERE a.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);


}
