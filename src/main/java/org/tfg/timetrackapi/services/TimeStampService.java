package org.tfg.timetrackapi.services;

import org.tfg.timetrackapi.dto.TimeStampDTO;
import org.tfg.timetrackapi.dto.TimeStampDataDTO;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeStampService {

    TimeStamp addTimeStamp(User user);
    void addTimeStampWithData(Long userId, LocalDateTime newTimestamp);

    void editTimeStampWithData(Long TimeStId, LocalDateTime newTimestamp);
    List<TimeStampDTO> getTimeStampsByEmployeeId(Long employeeId);

    void deleteRecord(Long id);


}
