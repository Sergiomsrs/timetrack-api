package org.tfg.timetrackapi.services;

import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;
import org.tfg.timetrackapi.entity.EmployeeSchedule;

import java.util.List;


public interface EmployeeScheduleService {

    public void guardarHorario(EmployeeScheduleDTO dto);

    void delete(Long id);
    EmployeeScheduleDTO update (Long id, EmployeeScheduleDTO dto);
    public void verificarFichajes();

    List<EmployeeScheduleDTO> getSchedulesByUserId(Long userId);

    List<EmployeeScheduleDTO> updateAll(List<EmployeeScheduleDTO> dtoList);

    void guardarHorarioPorDefecto(String dni);

    void deleteByUserId(Long userId);




}
