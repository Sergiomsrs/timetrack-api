package org.tfg.timetrackapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.timetrackapi.entity.TimeStamp;

public interface TimeStampRepository extends JpaRepository<TimeStamp, Long> {

}
