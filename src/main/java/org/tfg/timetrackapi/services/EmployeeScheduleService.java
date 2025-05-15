package org.tfg.timetrackapi.services;

import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;

public interface EmployeeScheduleService {

    public void guardarHorario(EmployeeScheduleDTO dto);
    public void verificarFichajes();


}
