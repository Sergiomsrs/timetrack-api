package org.tfg.timetrackapi.services;

import org.tfg.timetrackapi.dto.TimeStampDTO;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;

import java.util.List;

public interface TimeStampService {

    TimeStamp addTimeStamp(User user);
    List<TimeStampDTO> getTimeStampsByEmployeeId(Long employeeId);


}
