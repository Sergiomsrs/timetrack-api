package org.tfg.timetrackapi.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.timetrackapi.dto.AbsenceDTO;
import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;
import org.tfg.timetrackapi.services.AbsenceNotificationService;
import org.tfg.timetrackapi.services.EmployeeScheduleService;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
public class EmployeeScheduleController {

    private final EmployeeScheduleService employeeScheduleService;
    private final AbsenceNotificationService absenceNotificationService;


    public EmployeeScheduleController(EmployeeScheduleService employeeScheduleService, AbsenceNotificationService absenceNotificationService) {
        this.employeeScheduleService = employeeScheduleService;
        this.absenceNotificationService = absenceNotificationService;
    }

    @PostMapping
    public ResponseEntity<String> guardarHorario(@RequestBody EmployeeScheduleDTO dto) {
        employeeScheduleService.guardarHorario(dto);
        return ResponseEntity.ok("Horario guardado correctamente");
    }

    @PostMapping("/verificar")
    public ResponseEntity<String> verificarFichajes() {
        employeeScheduleService.verificarFichajes();
        return ResponseEntity.ok("Verificación completada");
    }

    @GetMapping("/last10")
    public List<AbsenceDTO> getLast10Schedules() {
        return absenceNotificationService.getLast10SchedulesDTO();
    }

    @GetMapping("/user/{userId}")
    public List<EmployeeScheduleDTO> getSchedulesByUserId(@PathVariable Long userId) {
        return employeeScheduleService.getSchedulesByUserId(userId);
    }




}
