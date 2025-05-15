package org.tfg.timetrackapi.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;
import org.tfg.timetrackapi.services.EmployeeScheduleService;

@RestController
@RequestMapping("/api/horarios")
public class EmployeeScheduleController {

    private final EmployeeScheduleService employeeScheduleService;


    public EmployeeScheduleController(EmployeeScheduleService employeeScheduleService) {
        this.employeeScheduleService = employeeScheduleService;
    }

    @PostMapping
    public ResponseEntity<String> guardarHorario(@RequestBody EmployeeScheduleDTO dto) {
        employeeScheduleService.guardarHorario(dto);
        return ResponseEntity.ok("Horario guardado correctamente");
    }


}
