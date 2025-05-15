package org.tfg.timetrackapi.services;

import org.tfg.timetrackapi.dto.AbsenceDTO;
import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;

import java.util.List;

public interface AbsenceNotificationService {

    public List<AbsenceDTO> getLast10SchedulesDTO();


}
