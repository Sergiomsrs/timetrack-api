package org.tfg.timetrackapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.timetrackapi.entity.EmployeeSchedule;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeScheduleRepository extends JpaRepository<EmployeeSchedule, Long> {

    List<EmployeeSchedule> findByUserIdAndDia(Long userId, DayOfWeek dia);

    List<EmployeeSchedule> findAllByUserId(Long userId);


}
