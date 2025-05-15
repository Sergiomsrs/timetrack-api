package org.tfg.timetrackapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.entity.Workshift;

public interface WorkShiftRepository extends JpaRepository<Workshift, Long> {




}
