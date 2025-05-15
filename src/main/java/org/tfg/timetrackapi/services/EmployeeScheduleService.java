package org.tfg.timetrackapi.services;

import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;


public interface EmployeeScheduleService {

    public void guardarHorario(EmployeeScheduleDTO dto);

    void delete(Long id);
    EmployeeScheduleDTO update (Long id, EmployeeScheduleDTO dto);
    public void verificarFichajes();


}
