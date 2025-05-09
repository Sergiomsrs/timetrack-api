package org.tfg.timetrackapi.services;

import org.tfg.timetrackapi.dto.Last3Dto;
import org.tfg.timetrackapi.dto.TimeStampDTO;
import org.tfg.timetrackapi.dto.TimeStampDataDTO;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeStampService {

    TimeStamp addTimeStamp(User user);
    void addTimeStampWithData(Long userId, LocalDateTime newTimestamp, String isMod);
    void editTimeStampWithData(Long TimeStId, LocalDateTime newTimestamp);
    List<TimeStampDTO> getTimeStampsByEmployeeId(Long employeeId);
    List<TimeStampDTO> getTimeStampsByEmployeeIdAndMonth(Long employeeId, int year, int month);
    void deleteRecord(Long id);
    List<Last3Dto> getLastThreeTimestamps();



}
